package controller;

import java.util.*;

import map.Junction;
import map.Lane;
import map.Road;

public interface IMapModel {

    Road getRandomRoad();
    Lane getRandomLane();
    Junction getRandomJunction();

    int getJunctionCount();
    int getRoadCount();

    void addJunction(int count);
    void addRoad(String j1, String j2);

    boolean isFinalized();

    List<Road> findShortesPath(Junction startJunction, Junction endJunction);

    List<Junction> getJunctions();
    List<Road> getRoads();

    void snow(int amount);

    public boolean validateMapModel();
}
