package vehicle;

import map.Lane;
import map.OutdoorLane;
import states.SnowyState;

/**
 * Az összes játékbeli jármű közös ősét reprezentálja.
 * Tárolja az aktuális sávot és az esetleges mozgásképtelenség kezdetét.
 * Maga az osztály nem példányosítható.
 */
public abstract class Vehicle {

    private String name;

    /**
     * Idő ameddig a jármű mozgásképessé válik egy baleset következtében.
     */
    public static final int IMMOBILE_TIME = 20;

    /**
     * Az ütközés bekövetkezésének időpillanata; ebből számítható,
     * hogy mikor válik ismét mozgásképessé a jármű.
     */
    protected int timeOutStart;

    /**
     * Az aktuális sáv, amelyen a jármű tartózkodik.
     */
    protected Lane currentLane;

    public String getName() {
        return name;
    }

    /**
     * Létrehoz egy új járművet.
     *
     * @param currentLane a jármű kezdeti sáva
     */
    public Vehicle(Lane currentLane, String name) {
        this.currentLane = currentLane;
        this.timeOutStart = -1; // -1 jelzi, hogy nincs időzítés aktív
        this.name = name;
        currentLane.pushVehicle(this, 0);
    }

    /**
     * Ha nem rég balesetezett a jármű, akkor egyből visszatér hamis értékkel
     * Megpróbálja befogadtatni a járművet a megadott sávval.
     * Sikeres esetben frissíti az aktuális sávot és meghívja az interactWithLane metódust.
     *
     * @param l a sáv, amelyre a jármű át szeretne lépni
     * @param timestamp az aktuális idő
     * @return true, ha a művelet sikerült, egyébként false
     */
    public boolean gotoLane(Lane l, int timestamp) {
        if (timeOutStart != -1 && timestamp - timeOutStart < IMMOBILE_TIME) {
            return false;
        }
        if (l instanceof OutdoorLane) {
            // Ha a cél sáv havas állapotban van, nem járható és a jármű nem hókotró
            if((((OutdoorLane) l).getCurrentState() instanceof SnowyState) && !(((OutdoorLane) l).isNavigable()) && !(this instanceof SnowPlow)) {
                return false;
            }
        }
        boolean result = l.pushVehicle(this, timestamp);
        if (result) {
            currentLane.popVehicle(this);
            currentLane = l;
            interactWithLane(l, timestamp);
        }
        return result;
    }

    /**
     * @return az aktuális sáv, amelyen a jármű tartózkodik
     */
    public Lane getCurrentLane() {
        return currentLane;
    }

    /**
     * Balesetet szenved a jármű: immobilizálódik a megadott időponttól kezdve.
     *
     * @param timestamp a baleset időpontja
     */
    public void crash(int timestamp) {
        timeOutStart = timestamp;
    }

    /**
     * Interakciót végez a megadott sávval.
     * A konkrét interakciós logika az alosztályokban van meghatározva.
     *
     * @param l a sáv, amelyikkel interakcióba lép
     * @param timestamp az aktuális idő
     */
    public abstract void interactWithLane(Lane l, int timestamp);
}
