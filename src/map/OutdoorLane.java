package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import states.DryState;
import states.LaneState;

/**
 * A Lane osztály leszármazottja, a kültéri sávokat valósítja meg.
 * Itt zajlik a valódi szimuláció zöme: számolja a hóréteget, jegesedést, reagál a takarításra.
 * Állapotát egy LaneState példány írja le (State pattern).
 */
public class OutdoorLane extends Lane {

    /**
     * @param source      a sáv kiindulópontja
     * @param destination a sáv végpontja
     */
    public OutdoorLane(Junction source, Junction destination) {
        super();
        this.source = source;
        this.destination = destination;
    }

    public OutdoorLane(LaneState initialState) {
        super();
        this.currentState = initialState;
    }

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
     * A függvény hívásának hatására az adott sávon paraméterként átadott snow mennyiségű hó esik.
     * Az állapot meghatározza, hogy a hó hogyan változtatja meg a sáv állapotát.
     *
     * @param snow a lehullott hó mennyisége
     */
    @Override
    public void snowFall(int snow) {
        currentState = currentState.handleSnow(this, snow);
    }

    /**
     * Hattatja a sávra a hókotró aktív fejét, az Attachment osztály cleanLane metódusának
     * hatására hívódik.
     *
     * @param head az aktív fej
     */
    @Override
    public void cleanWithHead(Attachment head) {
        head.cleanLane(this, saltedTimestamp);
    }

    /**
     * Ez a függvény kezeli a sáv állapotának és a rajta átmenő jármű interakcióját.
     * A sáv meghívja a saját állapotának a handleTraffic függvényét.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
        currentState.handleTraffic(v);
    }

    /**
     * Visszaadja az út aktuális állapotát.
     */
    public LaneState getCurrentState() {
        return currentState;
    }

    /**
     * A sáv állapotát a paraméterként kapott LaneState példányra állítja.
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

    /**
     * Egy game tick elteltét jelzi a sávnak. Továbbítja az állapotnak,
     * amely szükség esetén megváltoztatja azt (pl. só lejáratakor DryState-be vált).
     *
     * @param timestamp az aktuális idő
     */
    @Override
    public void tick(int timestamp) {
        currentState = currentState.tick(timestamp);
    }
}
