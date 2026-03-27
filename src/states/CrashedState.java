package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a balesetes sávállapotot reprezentálja.
 * A sávon baleset történt, amely blokkolja a forgalmat.
 * Egyetlen takarítóeszköz sem képes eltávolítani a balesetet — csak a Dragon
 * (lángszóró) tudja megtisztítani a sávot, közvetlenül DryState-be váltva.
 */
public class CrashedState extends LaneState {

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
     */
    public void handleTraffic(Vehicle v) {
        v.gotoLane(null, 0); // baleset blokkolja a forgalmat, jármű megáll
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
}
