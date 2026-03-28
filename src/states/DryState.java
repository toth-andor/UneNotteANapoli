package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A LaneState leszármazottja, a száraz sávállapotot reprezentálja.
 * Az út tiszta, nincs hó vagy jég, a forgalom akadálytalan.
 * Hóesés hatására SnowyState-be vált.
 */
public class DryState extends LaneState {

    /**
     * A hóakkumuláció küszöbértéke: ennyi egység hó szükséges a havas állapothoz.
     */
    private static final int SNOW_THRESHOLD = 5;

    /**
     * Hóesés hatására a száraz sáv havas lesz, ha az összegyűlt hó eléri a küszöbértéket.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return SnowyState ha elég hó gyűlt össze, egyébként this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        CallChainLogger.printCall(this, "handleSnow(" + Skeleton.getEntityByRef(lane) + ", " + amount + ")");
        if (lane.getSnowAmount() >= SNOW_THRESHOLD) {
            SnowyState snowy = new SnowyState();
            if (Skeleton.ENABLE_LOGGING) {
                Skeleton.pushEntity("snowy", snowy);
            }
            CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(snowy));
            return snowy;
        }
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Száraz sávon a forgalom akadálytalan, nincs hatás.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Száraz sávon a seprűnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        CallChainLogger.printCall(this, "cleanWithSweeper()");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Száraz sávon a jégtörőnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        CallChainLogger.printCall(this, "cleanWithIceBreaker()");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Száraz sávon a hányófejnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        CallChainLogger.printCall(this, "cleanWithVomittingHead()");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }
}
