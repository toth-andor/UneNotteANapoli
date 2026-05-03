package controller;

import java.util.*;

import map.Junction;
import map.Lane;
import map.Road;

public interface IMapModel {

    Road getRandomRoad();
    Lane getRandomLane();
    Junction getRandomJunction();

    void addJunction(String name);
    void addRoad(String j1, String j2);

    Lane findShortestPath(Junction src, Road dst);

    void snow(int amount);
}
