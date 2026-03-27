package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

/**
 * Száraz sávállapot: az út tiszta, nincs hó vagy jég.
 * A forgalom akadálytalan. Hóesés hatására {@link SnowyState}-be vált.
 */
public class DryState extends LaneState {

    /**
     * Hóesés hatására a száraz sáv havas lesz.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return  SnowyState
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return new SnowyState();
    }

    public LaneState handleCleaning(OutdoorLane lane, Attachment head) {
        // szaraz uthoz nem kell takaritas
        return this;
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
     * @return {@code this}
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this;
    }

    /**
     * Száraz sávon a jégtörőnek nincs hatása.
     *
     * @return {@code this}
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this;
    }

    /**
     * Száraz sávon a VomittingHeadnek nincs hatása.
     *
     * @return {@code this}
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return this;
    }
}
