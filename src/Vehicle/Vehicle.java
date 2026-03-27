package Vehicle;

import map.Lane;

/**
 * Az összes játékbeli jármű közös ősét reprezentálja.
 * Tárolja az aktuális sávot és az esetleges mozgásképtelenség kezdetét.
 * Maga az osztály nem példányosítható.
 */
public abstract class Vehicle {

    /**
     * Idő ameddig a jármű mozgásképessé válik egy baleset következtében.
     */
    static final int IMMOBILE_TIME = 20;

    /**
     * Az ütközés bekövetkezésének időpillanata; ebből számítható,
     * hogy mikor válik ismét mozgásképessé a jármű.
     */
    protected int timeOutStart;

    /**
     * Az aktuális sáv, amelyen a jármű tartózkodik.
     */
    protected Lane currentLane;

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
        return l.pushVehicle(this, timestamp);
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
