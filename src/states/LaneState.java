package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

/**
 * Egy sáv állapotát reprezentáló absztrakt osztály (State pattern).
 * Az alosztályok határozzák meg, hogyan reagál a sáv a hóesésre,
 * a forgalomra és a különböző takarítóeszközökre.
 */
public abstract class LaneState {

    /**
     * Hóesés hatását kezeli a sávon.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return az új állapot hóesés után
     */
    public abstract LaneState handleSnow(OutdoorLane lane, int amount);

    // public abstract LaneState handleCleaning(OutdoorLane lane, Attachment head); // Szerintem ez nem kell

    /**
     * A sávon áthaladó jármű viselkedését kezeli az aktuális állapot szerint.
     *
     * @param v az áthaladó jármű
     */
    public abstract void handleTraffic(Vehicle v);

    /**
     * Sóhintő attachment hatását alkalmazza a sávra.
     * Alapértelmezetten {@link SaltedState}-be vált.
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
     * Seprű attachment hatását alkalmazza a sávra.
     *
     * @return az új állapot seprés után
     */
    public abstract LaneState cleanWithSweeper();

    /**
     * Jégtörő attachment hatását alkalmazza a sávra.
     *
     * @return az új állapot jégtörés után
     */
    public abstract LaneState cleanWithIceBreaker();

    /**
     * VomittingHead attachment hatását alkalmazza a sávra.
     *
     * @return az új állapot a kezelés után
     */
    public abstract LaneState cleanWithVomittingHead();
}
