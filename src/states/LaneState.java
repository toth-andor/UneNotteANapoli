package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A sávok állapotának reprezentálásáért felelős absztrakt osztály.
 * Belőle származnak le a DryState, SnowyState, IcyState, SaltedState és CrashedState osztályok.
 */
public abstract class LaneState {

    /**
     * A havazási esemény kezeléséért felelős függvény. Meghatározza, hogyan változzon
     * a hóréteg vastagsága, vagy maga a sáv állapota a lehulló hó hatására.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return az új állapot hóesés után
     */
    public abstract LaneState handleSnow(OutdoorLane lane, int amount);

    // public abstract LaneState handleCleaning(OutdoorLane lane, Attachment head); // Szerintem ez nem kell

    /**
     * Ez a függvény kezeli a sáv állapotának és a rajta átmenő járműveknek az interakcióját,
     * hiszen minden állapotban máshogy reagál a sáv a forgalomra. A függvény lefut minden
     * alkalommal, amikor egy jármű rálép a sávra, ekkor a sáv meghívja a saját állapotának
     * a handleTraffic függvényét.
     *
     * @param v az áthaladó jármű
     */
    public abstract void handleTraffic(Vehicle v);

    /**
     * A só hatásának időtartama tickekben.
     */
    static final int SALT_DURATION = 5;

    /**
     * Sózó fej hatását alkalmazza a sávra. Alapértelmezetten SaltedState-be vált,
     * amelynek lejárati ideje timestamp + SALT_DURATION.
     *
     * @param timestamp a takarítás időbélyege
     * @return az új állapot sózás után
     */
    public LaneState cleanWithSaltVomitter(int timestamp) {
        return new SaltedState(timestamp + SALT_DURATION);
    }

    /**
     * Egy game tick elteltét jelzi az állapotnak. Alapértelmezetten nincs hatás.
     * A SaltedState felüldefiniálja, hogy lejárat esetén DryState-be váltson.
     *
     * @param timestamp az aktuális idő
     * @return az új állapot a tick után
     */
    public LaneState tick(int timestamp) {
        return this;
    }

    /**
     * Seprű fej hatását alkalmazza a sávra.
     *
     * @return az új állapot seprés után
     */
    public abstract LaneState cleanWithSweeper();

    /**
     * Jégtörő fej hatását alkalmazza a sávra.
     *
     * @return az új állapot jégtörés után
     */
    public abstract LaneState cleanWithIceBreaker();

    /**
     * Hányófej hatását alkalmazza a sávra.
     *
     * @return az új állapot a kezelés után
     */
    public abstract LaneState cleanWithVomittingHead();
}
