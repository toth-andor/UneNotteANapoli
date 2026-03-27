package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a sózott sávállapotot reprezentálja.
 * A sávot megsózták, a forgalom akadálytalan. A só véd a jégképződés ellen,
 * de hóesés esetén a sáv havas lesz. A sózás időbélyegét az OutdoorLane tartja számon.
 */
public class SaltedState extends LaneState {

    /**
     * Sózott sávra hulló hó havas állapotot eredményez.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return SnowyState
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
         return new SnowyState();
    }

    public LaneState handleCleaning(OutdoorLane lane, Attachment head) {
        return new DryState();
    }

    /**
     * Sózott sávon a forgalom akadálytalan, nincs hatás.
     *
     * @param v az áthaladó jármű
     */
    public void handleTraffic(Vehicle v) {}

    /**
     * Seprű eltávolítja a sót és maradék szennyeződést a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithSweeper() {
        return new DryState();
    }

    /**
     * Sózott sávon nincs jég, a jégtörőnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this; // nincs jég amit törni kellene
    }

    /**
     * Hányófej eltávolítja a sót a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return new DryState();
    }
}
