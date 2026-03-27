package states;

import Vehicle.Vehicle;
import map.OutdoorLane;

/**
 * A LaneState leszármazottja, a sózott sávállapotot reprezentálja.
 * A sávot megsózták, a forgalom akadálytalan. A só véd a jégképződés ellen,
 * de hóesés esetén a sáv havas lesz. Lejárat után automatikusan DryState-be vált.
 */
public class SaltedState extends LaneState {

    /**
     * Az az időpont, amikor a só hatása lejár.
     */
    private final int expiresAt;

    /**
     * @param expiresAt az az időpont, amikor a só hatása lejár
     */
    public SaltedState(int expiresAt) {
        this.expiresAt = expiresAt;
    }

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

    /**
     * Ha az aktuális idő elérte a lejárati időpontot, a sáv száraz lesz.
     * Egyébként az állapot változatlan marad.
     *
     * @param timestamp az aktuális idő
     * @return DryState ha lejárt, egyébként this
     */
    @Override
    public LaneState tick(int timestamp) {
        return timestamp >= expiresAt ? new DryState() : this;
    }
}
