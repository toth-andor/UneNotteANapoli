package controller;

import map.*;
import java.util.*;

public class MapModel implements IMapModel {

    private final int OUTDOOR_CHANCE = 80;

    private List<Road> model = new ArrayList<>();
    private List<Junction> junctions = new ArrayList<>();
    private Map<String, Junction> junctionsByName = new HashMap<>();

    private int roadCounter = 0;
    private int laneCounter = 0;

    private final Randomizer randomizer;

    public MapModel(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    @Override
    public void addJunction(String name) {
        Junction j = new Junction(name);
        junctions.add(j);
        junctionsByName.put(name, j);
    }

    @Override
    public void addRoad(String j1, String j2) {
        Junction start = junctionsByName.get(j1);
        Junction end = junctionsByName.get(j2);
        if (start == null || end == null) return;

        Road newRoad = new Road(start, end);
        newRoad.setName("road_" + (++roadCounter));
        start.addRoad(newRoad);
        end.addRoad(newRoad);
        model.add(newRoad);

        for (int i = 0; i < 4; i++) {
            int randomVal = randomizer.randomize(1, 100);
            Lane newLane;
            if (randomVal <= OUTDOOR_CHANCE) {
                newLane = new OutdoorLane(start, end);
            } else {
                newLane = new TunnelLane(start, end);
            }
            newLane.setName("lane_" + (++laneCounter));
            newRoad.addLane(newLane);
        }
    }

    @Override
    public Junction getRandomJunction() {
        int junctionIdx = randomizer.randomize(0, junctions.size() - 1);
        return junctions.get(junctionIdx);
    }

    @Override
    public Lane getRandomLane() {
        if (model.isEmpty()) return null;
        int laneIdx = randomizer.randomize(0, 3);
        int roadIdx = randomizer.randomize(0, model.size() - 1);
        return model.get(roadIdx).getLanes().get(laneIdx);
    }

    @Override
    public Road getRandomRoad() {
        if (model.isEmpty()) return null;
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

    public List<Road> getMapModel() {
        return new ArrayList<>(model);
    }

    @Override
    public List<Road> getAllRoads() {
        return new ArrayList<>(model);
    }

    public void eraseMapModel() {
        model.clear();
        junctions.clear();
        junctionsByName.clear();
        roadCounter = 0;
        laneCounter = 0;
    }

    private boolean validateMapModel() {
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

        return visited.size() == junctions.size();
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
