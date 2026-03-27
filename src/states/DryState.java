package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a száraz sávállapotot reprezentálja.
 * Az út tiszta, nincs hó vagy jég, a forgalom akadálytalan.
 * Hóesés hatására SnowyState-be vált.
 */
public class DryState extends LaneState {

    /**
     * Hóesés hatására a száraz sáv havas lesz.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return SnowyState
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return new SnowyState();
    }

    /**
     * Száraz sávon a forgalom akadálytalan, nincs hatás.
     *
     * @param v az áthaladó jármű
     */
    public void handleTraffic(Vehicle v) {}

    /**
     * Száraz sávon a seprűnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this;
    }

    /**
     * Száraz sávon a jégtörőnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this;
    }

    /**
     * Száraz sávon a hányófejnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return this;
    }
}
