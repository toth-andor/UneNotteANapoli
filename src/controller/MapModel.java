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
    public Lane findShortestPath(Junction src, Road dst) {
        if (src == null || dst == null) return null;

        Junction target1 = dst.getEnd1();
        Junction target2 = dst.getEnd2();

        // Ha src már az egyik végpontja a cél útnak, keressünk egy olyan sávot rajta, ami src-ből indul
        if (src.equals(target1) || src.equals(target2)) {
            for (Lane lane : dst.getLanes()) {
                if (lane.getSource().equals(src)) {
                    return lane;
                }
            }
        }

        // BFS a legrövidebb út megtalálásához bármelyik cél-csomóponthoz (target1 vagy target2)
        Map<Junction, Junction> parent = new HashMap<>();
        Map<Junction, Road> parentRoad = new HashMap<>();
        Set<Junction> visited = new HashSet<>();
        Queue<Junction> queue = new LinkedList<>();

        queue.add(src);
        visited.add(src);

        Junction foundTarget = null;

        while (!queue.isEmpty()) {
            Junction current = queue.poll();

            if (current.equals(target1) || current.equals(target2)) {
                foundTarget = current;
                break;
            }

            for (Road road : current.getRoads()) {
                Junction neighbor = (road.getEnd1().equals(current)) ? road.getEnd2() : road.getEnd1();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    parentRoad.put(neighbor, road);
                    queue.add(neighbor);
                }
            }
        }

        if (foundTarget == null) return null;

        // Visszakövetés az első útig, ami src-ből indul
        Junction curr = foundTarget;
        Road firstRoad = parentRoad.get(curr);
        
        while (parent.get(curr) != null && !parent.get(curr).equals(src)) {
            curr = parent.get(curr);
            firstRoad = parentRoad.get(curr);
        }
        
        // Visszatérünk az első olyan sávval ezen az úton, ami src-ből indul
        for (Lane lane : firstRoad.getLanes()) {
            if (lane.getSource().equals(src)) {
                return lane;
            }
        }

        return null;
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
