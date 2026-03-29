package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

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
        CallChainLogger.printCall(this, "handleSnow(" + Skeleton.getEntityByRef(lane) + ", " + amount + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
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
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ")");
        vehicleCount++;
        if (vehicleCount >= ICY_THRESHOLD) {
            IcyState icy = new IcyState();
            if (Skeleton.ENABLE_LOGGING) {
                Skeleton.pushEntity("icy", icy);
            }
            CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(icy));
            return icy;
        }
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Seprű eltávolítja a havat a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithSweeper() {
        CallChainLogger.printCall(this, "cleanWithSweeper()");
        DryState dry = new DryState();
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("dry", dry);
        }
        CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(dry));
        return dry;
    }

    /**
     * Jégtörő havas sávon hatástalan — a hót nem képes eltávolítani, csak jeget tud törni.
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
     * Hányófej eltávolítja a havat a sávról.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        CallChainLogger.printCall(this, "cleanWithVomittingHead()");
        DryState dry = new DryState();
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("dry", dry);
        }
        CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(dry));
        return dry;
    }
}
