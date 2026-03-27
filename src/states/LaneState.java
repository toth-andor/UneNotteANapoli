package states;

import Vehicle.Vehicle;
import attachments.Attachment;
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
     * Sóhintő fej hatását alkalmazza a sávra. Alapértelmezetten SaltedState-be vált.
     *
     * @param timestamp a takarítás időbélyege
     * @return az új állapot sózás után
     */
    public LaneState cleanWithSaltVomitter(int timestamp) {
        // TODO: megfellően inicializálni, hogy számon tudja tartani a só hatását még az is lehet,
        // hogy ezt le kell vinni a specifikus LaneState szintre (szinte biztos, akkor viszont int abstrakt)
        return new SaltedState();
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
