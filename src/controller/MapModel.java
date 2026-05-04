package controller;

import map.*;

import java.lang.constant.Constable;
import java.util.*;

public class MapModel implements IMapModel {

    private final int OUTDOOR_CHANCE = 80;

    static int nextJunctionID = 1;
    static int nextRoadID = 1;
    static int nextLaneID = 1;

    private List<Road> model = new ArrayList<>();
    private List<Junction> junctions = new ArrayList<>();
    private Map<String, Junction> junctionsByName = new HashMap<>();
    private Map<String, Road> roadsByName = new HashMap<>();
    private Map<String, Lane> lanesByName = new HashMap<>();


    private boolean finalized = false;

    private final Randomizer randomizer;

    public MapModel(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public List<Junction> getJunctions() {
        return junctions;
    }

    public int getJunctionCount() {
        return junctions.size();
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public void addJunction(int count) {
        for (int i = 0; i < count; i++) {
            String name = "junction_" + nextJunctionID++;
            Junction j = new Junction(name);
            junctions.add(j);
            junctionsByName.put(name, j);
        }
    }

    public void addRoad(String j1, String j2) {
        Junction start = junctionsByName.get(j1);
        Junction end = junctionsByName.get(j2);
        if (start == null || end == null) return;

        String name = "road_" + nextRoadID++;
        Road newRoad = new Road(start, end, name);

        start.addRoad(newRoad);
        end.addRoad(newRoad);

        model.add(newRoad);
        roadsByName.put(name, newRoad);

        for (int i = 0; i < 4; i++) {
            int randomVal = randomizer.randomize(1, 100);
            String laneName = "lane_" + nextLaneID++;
            if (randomVal <= OUTDOOR_CHANCE) {
                OutdoorLane newLane = new OutdoorLane(start, end, laneName);
                newRoad.addLane(newLane);
                lanesByName.put(laneName, newLane);
            } else {
                TunnelLane newLane = new TunnelLane(start, end, laneName);
                newRoad.addLane(newLane);
                lanesByName.put(laneName, newLane);
            }
        }
    }

    public int getRoadCount() {
        return model.size();
    }

    public List<Road> getRoads() {
        return model;
    }

    @Override
    public Junction getRandomJunction() {
        int junctionIdx = randomizer.randomize(0, junctions.size() - 1);
        return junctions.get(junctionIdx);
    }

    @Override
    public Lane getRandomLane() {
        int laneIdx = randomizer.randomize(0, 3);
        int roadIdx = randomizer.randomize(0, model.size() - 1);
        return model.get(roadIdx).getLanes().get(laneIdx);
    }

    @Override
    public Road getRandomRoad() {
        int roadIdx = randomizer.randomize(0, model.size() - 1);
        return model.get(roadIdx);
    }

    @Override
    public List<Road> findShortesPath(Junction startJunction, Junction endJunction) {
        if (startJunction == null || endJunction == null) return null;
        if (startJunction.equals(endJunction)) return new ArrayList<>();

        Map<Junction, Junction> parent = new HashMap<>();
        Map<Junction, Road> parentRoad = new HashMap<>();
        Set<Junction> visited = new HashSet<>();
        Queue<Junction> queue = new LinkedList<>();

        queue.add(startJunction);
        visited.add(startJunction);

        boolean found = false;

        while (!queue.isEmpty() && !found) {
            Junction current = queue.poll();

            for (Road road : model) {

                Junction neighbor = null;

                if (road.getEnd1().equals(current)) {
                    neighbor = road.getEnd2();
                } else if (road.getEnd2().equals(current)) {
                    neighbor = road.getEnd1();
                }

                if (neighbor != null && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    parentRoad.put(neighbor, road);
                    queue.add(neighbor);

                    if (neighbor.equals(endJunction)) {
                        found = true;
                        break;
                    }
                }
            }
        }

        if (!parent.containsKey(endJunction)) {
            return null;
        }

        List<Road> path = new ArrayList<>();
        Junction current = endJunction;

        while (!current.equals(startJunction)) {
            Road road = parentRoad.get(current);
            path.add(0, road);
            current = parent.get(current);
        }

        return path;
    }


    public void eraseMapModel() {
        model.clear();
        junctions.clear();
        junctionsByName.clear();
    }

    public boolean validateMapModel() {
        if (junctions.isEmpty()) { return false; }

        Set<Junction> visited = new HashSet<>();
        Queue<Junction> queue = new LinkedList<>();

        Junction start = junctions.get(0);
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Junction current = queue.poll();

            for (Road road : model) {
                Junction next = null;

                if (road.getEnd1().equals(current)) {
                    next = road.getEnd2();
                } else if (road.getEnd2().equals(current)) {
                    next = road.getEnd1();
                }

                if (next != null && !visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        boolean result = visited.size() == junctions.size();
        this.finalized = result;
        return result;
    }

    @Override
    public void snow(int amount) {
        for (Road road : model) {
            for (Lane lane : road.getLanes()) {
                if (lane instanceof OutdoorLane) {
                    lane.snowFall(amount);
                }
            }
        }
    }
}
