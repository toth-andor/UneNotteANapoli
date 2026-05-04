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
     * Sózott sávra hulló hó nem változtatja az állapotot — a só véd a hó ellen.
     * Lejárat után a tick() DryState-be vált, onnan indulhat újra a hóakkumuláció.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this;
    }

    /**
     * Sózott sávon a forgalom akadálytalan, nincs hatás.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        return this;
    }

    /**
     * Seprű eltávolítja a sót és maradék szennyeződést a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithSweeper() {
        DryState dry = new DryState();
        return dry;
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
        DryState dry = new DryState();
        return dry;
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
        if (timestamp >= expiresAt) {
            DryState dry = new DryState();
            return dry;
        }
        return this;

    }
}
