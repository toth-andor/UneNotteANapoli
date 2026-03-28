package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a havas sávállapotot reprezentálja.
 * A sávon hó van, a forgalom lassabb de nem akadályozott.
 * Seprű, jégtörő vagy hányófej hatására DryState-be vált.
 */
public class SnowyState extends LaneState {

    /**
     * További hóesés nem változtatja meg az állapotot, a sáv havas marad.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this; // már havas, marad havas
    }

    /**
     * Az áthaladó járművek számlálója. Ha eléri a küszöbértéket, jeges állapotba vált.
     */
    private int vehicleCount = 0;

    /**
     * Az áthaladó járművek száma, amely után a sáv jeges lesz.
     */
    private static final int ICY_THRESHOLD = 5;

    /**
     * Havas sávon a forgalom lassabb, de nem akadályozott. Ha 5 jármű haladt át,
     * a sáv jeges állapotba vált.
     *
     * @param v az áthaladó jármű
     * @return IcyState ha elég jármű áthaladt, egyébként this
     */
    public LaneState handleTraffic(Vehicle v) {
        vehicleCount++;
        if (vehicleCount >= ICY_THRESHOLD) {
            return new IcyState();
        }
        return this;
    }

    /**
     * Seprű eltávolítja a havat a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithSweeper() {
        return new DryState();
    }

    /**
     * Jégtörő eltávolítja a havat a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return new DryState();
    }

    /**
     * Hányófej eltávolítja a havat a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return new DryState();
    }
}
