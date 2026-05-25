package states;

import vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a jeges sávállapotot reprezentálja.
 * A sáv felszíne jeges, a járművek megcsúsznak és balesetet szenvednek.
 * Jégtörő hatásásra snovyba, hányófej nem csinál semmit; seprű nem tudja eltávolítani a jeget.
 */
public class IcyState extends LaneState {

    /**
     * Jeges sávra hulló hó nem változtatja az állapotot — jegesnél nem lehet jegesebb.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this; //jegesnel nem lehet jegesebb

    }

    /**
     * Jeges sávon az áthaladó jármű megcsúszik és kiesik a sávból.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        return this;
    }

    /**
     * Seprű nem képes eltávolítani a jeget, az állapot változatlan marad.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this; // seprű nem tudja eltávolítani a jeget
    }

    /**
     * Jégtörő eltávolítja a jeget a sávról, hó marad utána.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        SnowyState snow = new SnowyState();
        return snow;
    }

    /**
     * Hányófej eltávolítja a jeget a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithVomittingHead() {

        return this;
    }
}
