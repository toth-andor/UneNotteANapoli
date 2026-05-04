package controller;

import java.util.ArrayList;

import Vehicle.Car;
import map.Lane;
import map.Road;

/**
 * Az NPC autók mozgásáért felelős segédkomponens.
 */
public class NPCHandler {

   public  ArrayList<Car> npcCars;

   static int nextCarID = 1;

    /**
     * Létrehoz egy új NPCHandler példányt.
     */
    public NPCHandler() {
        npcCars = new ArrayList<>();
    }

    public void addNPC(Road destination1, Road destination2, Lane currentLane) {
        String name = "car_" + nextCarID++;
        npcCars.add(new Car(destination1, destination2, currentLane, name));
    }

    public ArrayList<Car> getNpcCars() {
        return npcCars;
    }

    public int getCarCount() {
        return npcCars.size();
    }

    /**
     * Mozgatja az összes NPC autót.
     */
    public void moveNPCs() {
        // TODO: Implementálás az NPC autók mozgásának logikája
        // A MapModel és Lane osztályok elérése után
    }
}
