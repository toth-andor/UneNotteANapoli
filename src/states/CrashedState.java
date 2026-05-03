package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A LaneState leszármazottja, a balesetes sávállapotot reprezentálja.
 * A sávon baleset történt, amely blokkolja a forgalmat.
 * Egyetlen takarítóeszköz sem képes eltávolítani a balesetet — csak a Dragon
 * (lángszóró) tudja megtisztítani a sávot, közvetlenül DryState-be váltva.
 * A baleset automatikusan is feloldódik IMMOBILE_TIME telt el után, IcyState-be visszaváltva.
 */
public class CrashedState extends LaneState {

    /**
     * Az az időpont, amikor a baleset feloldódik és a sáv ismét járható lesz.
     */
    private final int expiresAt;

    /**
     * @param expiresAt az az időpont, amikor a sáv feloldódik
     */
    public CrashedState(int expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Ha az aktuális idő elérte a lejárati időpontot, a sáv jeges állapotba kerül vissza.
     *
     * @param timestamp az aktuális idő
     * @return IcyState ha lejárt, egyébként this
     */
    @Override
    public LaneState tick(int timestamp) {
        CallChainLogger.printCall(this, "tick(" + timestamp + ")");
        if (timestamp >= expiresAt) {
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
     * Balesetes sávra hulló hó nem változtatja az állapotot.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return this
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        CallChainLogger.printCall(this, "handleSnow(" + Skeleton.getEntityByRef(lane) + ", " + amount + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * A balesetes sávra érkező jármű kénytelen elhagyni a sávot.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ")");
        
        // TODO JAVITANI KELL
        v.gotoLane(null, 0); // baleset blokkolja a forgalmat, jármű megáll 
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Balesetet nem lehet sózással eltávolítani.
     *
     * @param timestamp a takarítás időbélyege
     * @return this
     */
    @Override
    public LaneState cleanWithSaltVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithSaltVomitter(" + timestamp + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Seprű nem képes eltakarítani a balesetet.
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
     * Jégtörő nem képes eltakarítani a balesetet.
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
     * Hányófej nem képes eltakarítani a balesetet.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        CallChainLogger.printCall(this, "cleanWithVomittingHead()");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Zúzalékszóró nem képes eltakarítani a balesetet.
     *
     * @param timestamp a takarítás időbélyege
     * @return this
     */
    @Override
    public LaneState cleanWithStoneVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithStoneVomitter(" + timestamp + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }
}
