package map;

import vehicle.Vehicle;
import attachments.Attachment;
import states.CrashedState;
import states.DryState;
import states.GraveledState;
import states.IcyState;
import states.LaneState;
import states.SaltedState;
import states.SnowyState;
import java.util.List;

/**
 * A Lane osztály leszármazottja, a kültéri sávokat valósítja meg.
 * Itt zajlik a valódi szimuláció zöme: számolja a hóréteget, jegesedést, reagál a takarításra.
 */
public class OutdoorLane extends Lane {

    /**
     * @param source      a sáv kiindulópontja
     * @param destination a sáv végpontja
     */
    public OutdoorLane(Junction source, Junction destination, String name) {
        super(name);
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
     * A hó maximális mennyiségének küszöbértéke, amíg még járható a sáv.
     */
    private static final int USABLE_THRESHOLD = 20;

     /**
     * Visszaadja a sávon lévő hó mennyiségétől függően, hogy járható e még a sáv
     */
    public boolean isNavigable() {
        return this.snowAmount <= USABLE_THRESHOLD;
    }

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
        if (currentState instanceof CrashedState) {
            return false;
        }
        super.pushVehicle(v, timestamp);
        setState(currentState.handleTraffic(v));
        return true;
    }

    /**
     * Két járművet összeütköztet a sávon. Ellenőrzi, hogy a sáv jeges állapotban van-e,
     * és hogy mindkét jármű a sávon tartózkodik-e. Ha igen, mindkét jármű immobilizálódik,
     * és a sáv balesetes állapotba kerül.
     *
     * @param v1        az első jármű
     * @param v2        a második jármű
     * @param timestamp az ütközés időpontja
     */
    public void crash(Vehicle v1, Vehicle v2, int timestamp) {
        if (!(currentState instanceof IcyState)) {
            return;
        }
        if (!getVehicles().contains(v1) || !getVehicles().contains(v2)) {
            return;
        }
        v1.crash(timestamp);
        v2.crash(timestamp);
        CrashedState crashed = new CrashedState(timestamp + Vehicle.IMMOBILE_TIME);
        setState(crashed);
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
        if (!(currentState instanceof SaltedState)) {
            snowAmount += snow;
        }
        setState(currentState.handleSnow(this, snow));
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
     * A sávra lépés hatását a pushVehicle már kezeli; ez a metódus nem végez dupla számlálást.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
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
     * Zúzalékos sávra a lángszórónak nincs hatása.
     */
    @Override
    public void cleanWithDragon() {
        if (currentState instanceof GraveledState) {
            return;
        }
        DryState dry = new DryState();
        setState(dry);
        snowAmount = 0;
    }

    /**
     * Sószóró hatása: az állapot dönt arról, hogy a sózás milyen új állapotot eredményez.
     * Eltárolja a sózás időbélyegét a lejárat nyomon követéséhez.
     * Nullázza a hómennyiséget, ha az állapot ténylegesen megváltozott
     * (pl. zúzalékos sávra só nem hat, így ott a hómennyiség nem nullázódik).
     *
     * @param timestamp a takarítás időbélyege
     */
    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        LaneState newState = currentState.cleanWithSaltVomitter(timestamp);
        boolean changed = newState != currentState;
        setState(newState);
        if (changed) {
            this.saltedTimestamp = timestamp;
            snowAmount = 0;
        }
    }

    /**
     * Söprőfej hatása: havas sávon a havat a kisebb indexű, azonos irányú szomszéd sávra tolja
     * (ha létezik ilyen), különben a hó eltűnik. Minden esetben nullázza a hómennyiséget.
     */
    @Override
    public void cleanWithSweeper() {
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
    }

    /**
     * Jégtörőfej hatása: az állapot dönt arról, hogy a jégtörés milyen új állapotot eredményez.
     * A hómennyiség nem változik.
     */
    @Override
    public void cleanWithIceBreaker() {
        LaneState previousState = getCurrentState();
        setState(currentState.cleanWithIceBreaker());
        if(previousState.getClass() != currentState.getClass()) {
            setStateWasChanged(true);
        }
    }

    /**
     * Zúzalékszóró hatása: az állapot dönt arról, hogy a zúzalékszórás milyen új állapotot eredményez.
     * Nullázza a hómennyiséget, ha az állapot ténylegesen megváltozott
     * (pl. már zúzalékos sávra újabb zúzalék szórása nem nullázza a felhalmozódott havat).
     *
     * @param timestamp a takarítás időbélyege
     */
    @Override
    public void cleanWithStoneVomitter(int timestamp) {
        LaneState newState = currentState.cleanWithStoneVomitter(timestamp);
        boolean changed = newState != currentState;
        setState(newState);
        if (changed) {
            snowAmount = 0;
        }
    }

    /**
     * Hányófej hatása: az állapot dönt arról, hogy a kezelés milyen új állapotot eredményez.
     * Nullázza a hómennyiséget.
     */
    @Override
    public void cleanWithVomittingHead() {
        setState(currentState.cleanWithVomittingHead());
        snowAmount = 0;
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
        setState(currentState.tick(timestamp));
    }
}
