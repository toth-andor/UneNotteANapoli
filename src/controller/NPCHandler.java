package controller;

import java.util.ArrayList;

import Vehicle.Car;
import map.Junction;
import map.Lane;
import map.Road;

/**
 * Az NPC autók mozgásáért felelős segédkomponens.
 */
public class NPCHandler {

    ArrayList<Car> npcCars;

    /**
     * Létrehoz egy új NPCHandler példányt.
     */
    public NPCHandler() {
        npcCars = new ArrayList<>();
    }

    public void addNPC(Road destination1, Road destination2, Lane currentLane) {
        npcCars.add(new Car(destination1, destination2, currentLane));
    }

    public ArrayList<Car> getNpcCars() {
        return npcCars;
    }

    /**
     * Mozgatja az összes NPC autót a legrövidebb úton a céljuk felé.
     * Ha egy autó eléri a célját, megfordul.
     *
     * @param mapModel a térkép modellje az útvonalkereséshez
     * @param timestamp az aktuális időpont
     */
    public void moveNPCs(IMapModel mapModel, int timestamp) {
        for (Car car : npcCars) {
            // Ha az autó a cél útján van, megfordul
            if (car.getCurrentLane().getRoad().equals(car.getCurrentDestination())) {
                car.turnAround();
            }

            // A jelenlegi sáv végpontja (csomópont), ahonnan indulunk
            Junction currentJunction = car.getCurrentLane().getDestination();

            // A cél út, amire el akarunk jutni
            Road targetRoad = car.getCurrentDestination();

            // Megkeressük a következő sávot a legrövidebb úton
            Lane nextLane = mapModel.findShortestPath(currentJunction, targetRoad);

            if (nextLane != null) {
                car.gotoLane(nextLane, timestamp);
            }
        }
    }
}
