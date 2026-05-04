package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a balesetes sávállapotot reprezentálja.
 * A sávon baleset történt, amely blokkolja a forgalmat.
 * Egyetlen takarítóeszköz sem képes eltávolítani a balesetet — csak a Dragon
 * (lángszóró) tudja megtisztítani a sávot, közvetlenül DryState-be váltva.
 * A baleset automatikusan is feloldódik IMMOBILE_TIME telt el után, IcyState-be visszaváltva.
 */
public class CrashedState extends LaneState {

    /**
     * Az az időpont, amikor a baleset feloldódik és a sáv ismét járható lesz.
     */
    private final int expiresAt;

    /**
     * @param expiresAt az az időpont, amikor a sáv feloldódik
     */
    public CrashedState(int expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Ha az aktuális idő elérte a lejárati időpontot, a sáv jeges állapotba kerül vissza.
     *
     * @param timestamp az aktuális idő
     * @return IcyState ha lejárt, egyébként this
     */
    @Override
    public LaneState tick(int timestamp) {
        if (timestamp >= expiresAt) {
            IcyState icy = new IcyState();
            return icy;
        }
        return this;
    }

    /**
     * Balesetes sávra hulló hó nem változtatja az állapotot.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this;
    }

    /**
     * A balesetes sávra érkező jármű kénytelen elhagyni a sávot.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {

        // TODO JAVITANI KELL
        v.gotoLane(null, 0); // baleset blokkolja a forgalmat, jármű megáll 
        return this;
    }

    /**
     * Balesetet nem lehet sózással eltávolítani.
     *
     * @param timestamp a takarítás időbélyege
     * @return this
     */
    @Override
    public LaneState cleanWithSaltVomitter(int timestamp) {
        return this;
    }

    /**
     * Seprű nem képes eltakarítani a balesetet.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this;
    }

    /**
     * Jégtörő nem képes eltakarítani a balesetet.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this;
    }

    /**
     * Hányófej nem képes eltakarítani a balesetet.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return this;

    }

    /**
     * Zúzalékszóró nem képes eltakarítani a balesetet.
     *
     * @param timestamp a takarítás időbélyege
     * @return this
     */
    @Override
    public LaneState cleanWithStoneVomitter(int timestamp) {
        return this;
    }
}
