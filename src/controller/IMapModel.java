package controller;

import map.Lane;
import map.Road;

public interface IMapModel {

    Road getRandomRoad();
    Lane getRandomLane();

    void addJunction(String name);
    void addRoad(String j1, String j2);

    void snow(int amount);
}
