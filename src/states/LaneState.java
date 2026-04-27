package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

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
     * @return az új állapot a forgalom kezelése után
     */
    public abstract LaneState handleTraffic(Vehicle v);

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
        CallChainLogger.printCall(this, "cleanWithSaltVomitter(" + timestamp + ")");
        SaltedState salted = new SaltedState(timestamp + SALT_DURATION);
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("salted", salted);
        }
        CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(salted));
        return salted;
    }

    /**
     * Egy game tick elteltét jelzi az állapotnak. Alapértelmezetten nincs hatás.
     * A SaltedState felüldefiniálja, hogy lejárat esetén DryState-be váltson.
     *
     * @param timestamp az aktuális idő
     * @return az új állapot a tick után
     */
    public LaneState tick(int timestamp) {
        CallChainLogger.printCall(this, "tick(" + timestamp + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Zúzalékszóró fej hatását alkalmazza a sávra. Alapértelmezetten GraveledState-be vált.
     * A GraveledState felüldefiniálja, hogy ne változzon az állapot.
     *
     * @param timestamp a takarítás időbélyege
     * @return az új állapot zúzalékszórás után
     */
    public LaneState cleanWithStoneVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithStoneVomitter(" + timestamp + ")");
        GraveledState graveled = new GraveledState();
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("graveled", graveled);
        }
        CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(graveled));
        return graveled;
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
