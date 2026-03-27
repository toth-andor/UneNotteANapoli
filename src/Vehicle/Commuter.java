package Vehicle;

import map.Lane;
import map.Road;

/**
 * A Bus és Car osztályok közös viselkedését foglalja össze.
 * Tárolja a két célpontot és gondoskodik a célpontváltásról végállomásokon.
 * Maga az osztály nem példányosítható.
 */
public abstract class Commuter extends Vehicle implements IRouteHandler {

    /**
     * Az egyik végállomást jelölő út.
     */
    private Road destination1;

    /**
     * A másik végállomást jelölő út.
     */
    private Road destination2;

    /**
     * Létrehoz egy új ingázót.
     *
     * @param destination1 az első végállomás
     * @param destination2 a második végállomás
     * @param currentLane az ingázó kezdeti sáva
     */
    public Commuter(Road destination1, Road destination2, Lane currentLane) {
        super(currentLane);
        this.destination1 = destination1;
        this.destination2 = destination2;
    }

    /**
     * Frissíti a bevételt egy sikeres út után.
     * A konkrét implementáció az alosztályokban van meghatározva.
     */
    protected abstract void updateIncome();

    /**
     * Megcseréli a két végállomást, ha az egyik el lett érve.
     * Meghívja az updateIncome metódust a sikeres út után járó bevétel elszámolásához.
     */
    public void turnAround() {
        if (currentLane.getRoad().equals(destination1)) {
            updateIncome();
            Road temp = destination1;
            destination1 = destination2;
            destination2 = temp;
        }
    }

    /**
     * Interakciót végez a megadott sávval.
     * Meghívja a sáv handleTraffic metódusát, amely a LaneState logikája alapján
     * kezeli a letaposást és a jégesedést.
     *
     * @param l a sáv, amelyikkel interakcióba lép
     * @param timestamp az aktuális idő
     */
    public void interactWithLane(Lane l, int timestamp) {
        l.handleTraffic(this, timestamp);
    }


    /**
     * Ütközés esetén hívódik, beállítja az timeOutStart-ot a paraméterül kapott időpillanatra.
     *
     * @param timestamp az ütközés időpontja
     */
    public void crash(int timestamp) {
        timeOutStart = timestamp;
    }
}
