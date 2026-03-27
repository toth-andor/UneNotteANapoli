package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * Havas sávállapot: a sávon hó van, a forgalom lassabb de nem akadályozott.
 * Seprű, jégtörő vagy VomittingHead hatására {@link DryState}-be vált.
 */
public class SnowyState extends LaneState {

    /**
     * További hóesés nem változtatja meg az állapotot, a sáv havas marad.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return {@code this}
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this; // már havas, marad havas
    }

    /**
     * Havas sávon a forgalom lassabb, de a járművek nem akadályozottak.
     *
     * @param v az áthaladó jármű
     */
    public void handleTraffic(Vehicle v) {
        // havas úton a forgalom lassabb, de nem akadályozott
    }

    /**
     * Seprű eltávolítja a havat a sávról.
     *
     * @return {@link DryState}
     */
    @Override
    public LaneState cleanWithSweeper() {
        return new DryState();
    }

    /**
     * Jégtörő eltávolítja a havat a sávról.
     *
     * @return {@link DryState}
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return new DryState();
    }

    /**
     * VomittingHead eltávolítja a havat a sávról.
     *
     * @return {@link DryState}
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return new DryState();
    }
}
