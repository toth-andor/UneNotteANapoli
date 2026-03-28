package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import states.DryState;
import states.LaneState;
import states.SaltedState;
import states.SnowyState;
import java.util.List;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A Lane osztály leszármazottja, a kültéri sávokat valósítja meg.
 * Itt zajlik a valódi szimuláció zöme: számolja a hóréteget, jegesedést, reagál a takarításra.
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
     * A sávon összegyűlt hó mennyisége. Ha eléri a küszöbértéket, havas állapotba vált.
     */
    private int snowAmount = 0;

    /**
     * @return a sávon összegyűlt hó mennyisége
     */
    public int getSnowAmount() {
        return snowAmount;
    }

    /**
     * Regisztrálja a járművet a sávon, majd meghívja az állapot forgalomkezelő logikáját.
     * Jeges sávon a jármű kieshet; balesetes sávon megáll; havas sávon 5 jármű után jeges lesz.
     *
     * @param v         a befogadandó jármű
     * @param timestamp az aktuális idő
     * @return true, ha a befogadás sikerült
     */
    @Override
    public boolean pushVehicle(Vehicle v, int timestamp) {
        CallChainLogger.printCall(this, "pushVehicle(" + Skeleton.getEntityByRef(v) + ", " + timestamp + ")");
        super.pushVehicle(v, timestamp);
        setState(currentState.handleTraffic(v));
        CallChainLogger.printReturn("true");
        return true;
    }

    /**
     * A függvény hívásának hatására az adott sávon paraméterként átadott snow mennyiségű hó esik.
     * Sózott sávon a hó nem halmozódik fel (a só elolvasztja). Egyéb esetben akkumulálódik;
     * ha eléri az 5-ös küszöbértéket, havas állapotba vált.
     *
     * @param snow a lehullott hó mennyisége
     */
    @Override
    public void snowFall(int snow) {
        CallChainLogger.printCall(this, "snowFall(" + snow + ")");
        if (!(currentState instanceof SaltedState)) {
            snowAmount += snow;
        }
        setState(currentState.handleSnow(this, snow));
        CallChainLogger.printReturn(null);
    }

    /**
     * Hattatja a sávra a hókotró aktív fejét, az Attachment osztály cleanLane metódusának
     * hatására hívódik.
     *
     * @param head az aktív fej
     */
    @Override
    public void cleanWithHead(Attachment head) {
        CallChainLogger.printCall(this, "cleanWithHead(" + Skeleton.getEntityByRef(head) + ")");
        head.cleanLane(this, saltedTimestamp);
        CallChainLogger.printReturn(null);
    }

    /**
     * Ez a függvény kezeli a sáv állapotának és a rajta átmenő jármű interakcióját.
     * A sávra lépés hatását a pushVehicle már kezeli; ez a metódus nem végez dupla számlálást.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ", " + timestamp + ")");
        CallChainLogger.printReturn(null);
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
        if (Skeleton.ENABLE_LOGGING) {
            CallChainLogger.printCall(this, "setState(" + Skeleton.getEntityByRef(s) + ")");
            CallChainLogger.printReturn(null);
        }
        this.currentState = s;
    }

    /**
     * Sárkányfej hatása: a sávot azonnal száraz állapotba hozza,
     * eltávolít minden havat, jeget és balesetet.
     */
    @Override
    public void cleanWithDragon() {
        CallChainLogger.printCall(this, "cleanWithDragon()");
        DryState dry = new DryState();
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("dry", dry);
        }
        setState(dry);
        snowAmount = 0;
        CallChainLogger.printReturn(null);
    }

    /**
     * Sószóró hatása: az állapot dönt arról, hogy a sózás milyen új állapotot eredményez.
     * Eltárolja a sózás időbélyegét a lejárat nyomon követéséhez.
     * Nullázza a hómennyiséget, hogy lejárat után száraz állapotból induljon a sáv.
     *
     * @param timestamp a takarítás időbélyege
     */
    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithSaltVomitter(" + timestamp + ")");
        setState(currentState.cleanWithSaltVomitter(timestamp));
        this.saltedTimestamp = timestamp;
        snowAmount = 0;
        CallChainLogger.printReturn(null);
    }

    /**
     * Söprőfej hatása: havas sávon a havat a kisebb indexű, azonos irányú szomszéd sávra tolja
     * (ha létezik ilyen), különben a hó eltűnik. Minden esetben nullázza a hómennyiséget.
     */
    @Override
    public void cleanWithSweeper() {
        CallChainLogger.printCall(this, "cleanWithSweeper()");
        boolean wasSnowy = currentState instanceof SnowyState;
        int snowToPush = this.snowAmount;
        setState(currentState.cleanWithSweeper());
        snowAmount = 0;
        if (wasSnowy) {
            Lane target = findSmallerIndexedSameDirectionLane();
            if (target != null) {
                target.snowFall(snowToPush);
            }
        }
        CallChainLogger.printReturn(null);
    }

    /**
     * Jégtörőfej hatása: az állapot dönt arról, hogy a jégtörés milyen új állapotot eredményez.
     * Nullázza a hómennyiséget.
     */
    @Override
    public void cleanWithIceBreaker() {
        CallChainLogger.printCall(this, "cleanWithIceBreaker()");
        setState(currentState.cleanWithIceBreaker());
        snowAmount = 0;
        CallChainLogger.printReturn(null);
    }

    /**
     * Hányófej hatása: az állapot dönt arról, hogy a kezelés milyen új állapotot eredményez.
     * Nullázza a hómennyiséget.
     */
    @Override
    public void cleanWithVomittingHead() {
        CallChainLogger.printCall(this, "cleanWithVomittingHead()");
        setState(currentState.cleanWithVomittingHead());
        snowAmount = 0;
        CallChainLogger.printReturn(null);
    }

    /**
     * Megkeresi a kisebb indexű, azonos menetirányú szomszéd sávot az út sávlistájában.
     * Ha nincs ilyen sáv, null-t ad vissza.
     *
     * @return a kisebb indexű azonos irányú sáv, vagy null ha nincs
     */
    private Lane findSmallerIndexedSameDirectionLane() {
        List<Lane> lanes = getRoad().getLanes();
        int myIndex = lanes.indexOf(this);
        for (int i = myIndex - 1; i >= 0; i--) {
            Lane candidate = lanes.get(i);
            if (candidate.getSource() == this.source && candidate.getDestination() == this.destination) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Egy game tick elteltét jelzi a sávnak. Továbbítja az állapotnak,
     * amely szükség esetén megváltoztatja azt (pl. só lejáratakor DryState-be vált).
     *
     * @param timestamp az aktuális idő
     */
    @Override
    public void tick(int timestamp) {
        CallChainLogger.printCall(this, "tick(" + timestamp + ")");
        setState(currentState.tick(timestamp));
        CallChainLogger.printReturn(null);
    }
}
