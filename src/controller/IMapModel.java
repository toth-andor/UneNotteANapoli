package controller;

import java.util.*;

import map.ILane;
import map.Junction;
import map.Road;

public interface IMapModel {

    Road getRandomRoad(Randomizer randomizer);
    ILane getRandomLane(Randomizer randomizer);
    Junction getRandomJunction(Randomizer randomizer);

    void addJunction(Junction newJunction);
    void addRoad(Junction startJunction, Junction endJunction, Randomizer randomizer);

    List<Road> findShortesPath(Junction startJunction, Junction endJunction);

    void snow(int amount);
}

