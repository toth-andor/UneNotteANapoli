package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A LaneState leszármazottja, a jeges sávállapotot reprezentálja.
 * A sáv felszíne jeges, a járművek megcsúsznak és balesetet szenvednek.
 * Jégtörő vagy hányófej hatására DryState-be vált; seprű nem tudja eltávolítani a jeget.
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
        CallChainLogger.printCall(this, "handleSnow(" + Skeleton.getEntityByRef(lane) + ", " + amount + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this; //jegesnel nem lehet jegesebb
    }

    /**
     * Jeges sávon az áthaladó jármű megcsúszik és kiesik a sávból.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ")");
        v.gotoLane(null, 0);
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Seprű nem képes eltávolítani a jeget, az állapot változatlan marad.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        CallChainLogger.printCall(this, "cleanWithSweeper()");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this; // seprű nem tudja eltávolítani a jeget
    }

    /**
     * Jégtörő eltávolítja a jeget a sávról, hó marad utána.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        CallChainLogger.printCall(this, "cleanWithIceBreaker()");
        SnowyState snow = new SnowyState();
        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("snow", snow);
        }
        CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(snow));
        return snow;
    }

    /**
     * Hányófej eltávolítja a jeget a sávról.
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
