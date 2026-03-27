package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import states.DryState;
import states.LaneState;

/**
 * Kültéri sávot reprezentáló osztály — a {@link Lane} leszármazottja.
 * Itt zajlik a szimuláció zöme: számolja a hóréteget, kezeli a jegesedést,
 * és reagál a különböző takarítóeszközökre. Állapotát egy {@link LaneState}
 * példány írja le (State pattern).
 */
public class OutdoorLane extends Lane {

    /**
     * A sáv aktuális állapota (pl. száraz, havas, jeges, sózott, balesetes).
     */
    private LaneState currentState = new DryState();

    /**
     * A legutóbbi sózás időbélyege — a só lejárati idejének kiszámításához szükséges.
     */
    private int saltedTimestamp;

    /**
     * Regisztrálja a járművet a sávon, majd meghívja az állapot forgalomkezelő logikáját.
     * Jeges sávon a jármű kieshet; balesetes sávon megáll.
     *
     * @param v         a befogadandó jármű
     * @param timestamp az aktuális idő
     * @return true, ha a befogadás sikerült
     */
    @Override
    public boolean pushVehicle(Vehicle v, int timestamp) {
        super.pushVehicle(v, timestamp);
        currentState.handleTraffic(v);
        return true;
    }

    /**
     * Hóesés hatását kezeli: az állapot meghatározza, hogy a hó hogyan változtatja
     * meg a sáv állapotát (pl. szárazból havas lesz).
     *
     * @param snow a lehullott hó mennyisége
     */
    @Override
    public void snowFall(int snow) {
        currentState = currentState.handleSnow(this, snow);
    }

    /**
     * A hókotró aktív fejének hatását alkalmazza a sávra.
     * A fej saját {@code cleanLane} metódusa hívja meg a sáv típusspecifikus
     * takarítási metódusát (pl. {@link #cleanWithSweeper()}).
     *
     * @param head az aktív fej
     */
    @Override
    public void cleanWithHead(Attachment head) {
        head.cleanLane(this, saltedTimestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
        currentState.handleTraffic(v);
    }

    /**
     * Beállítja a sáv állapotát a megadott {@link LaneState} példányra.
     *
     * @param s az új állapot
     */
    public void setState(LaneState s) {
        this.currentState = s;
    }

    /**
     * Sárkányfej hatása: a sávot azonnal száraz állapotba hozza,
     * eltávolít minden havat, jeget és balesetet.
     */
    @Override
    public void cleanWithDragon() {
        currentState = new DryState();
    }

    /**
     * Sószóró hatása: az állapot dönt arról, hogy a sózás milyen új állapotot eredményez.
     * Eltárolja a sózás időbélyegét a lejárat nyomon követéséhez.
     *
     * @param timestamp a takarítás időbélyege
     */
    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        currentState = currentState.cleanWithSaltVomitter(timestamp);
        this.saltedTimestamp = timestamp;
    }

    /**
     * Söprőfej hatása: az állapot dönt arról, hogy a seprés milyen új állapotot eredményez.
     */
    @Override
    public void cleanWithSweeper() {
        currentState = currentState.cleanWithSweeper();
    }

    /**
     * Jégtörőfej hatása: az állapot dönt arról, hogy a jégtörés milyen új állapotot eredményez.
     */
    @Override
    public void cleanWithIceBreaker() {
        currentState = currentState.cleanWithIceBreaker();
    }

    /**
     * Hányófej hatása: az állapot dönt arról, hogy a kezelés milyen új állapotot eredményez.
     */
    @Override
    public void cleanWithVomittingHead() {
        currentState = currentState.cleanWithVomittingHead();
    }
}
