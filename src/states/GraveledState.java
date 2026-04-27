package states;

import Vehicle.Vehicle;
import map.OutdoorLane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A LaneState leszármazottja, a zúzalékkal szórt sávállapotot reprezentálja.
 * A zúzalék megszünteti a jég csúszósságát: a forgalom akadálytalan, balesetek nem történnek.
 * A zúzalék nem tömörödik. Ha elegendő hó esik rá, a sáv hóval borított állapotba vált.
 * Söprő és hányófej eltávolítja; lángszóró, jégtörő és só nem hat rá.
 */
public class GraveledState extends LaneState {

    /**
     * A hóakkumuláció küszöbértéke: ennyi egység hó szükséges ahhoz, hogy a hó befedje a zúzalékot.
     */
    private static final int SNOW_THRESHOLD = 5;

    /**
     * Zúzalékos sávra hulló hó felhalmozódik. Ha eléri a küszöbértéket, a sáv hóval borítottá válik.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return SnowyState ha elég hó gyűlt össze, egyébként this
     */
    @Override
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
     * Zúzalékos sávon a forgalom akadálytalan, a zúzalék nem tömörödik jéggé.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    @Override
    public LaneState handleTraffic(Vehicle v) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ")");
        CallChainLogger.printReturn(Skeleton.getEntityByRef(this));
        return this;
    }

    /**
     * Söprő eltávolítja a zúzalékot a sávról, ugyanúgy mint a havat.
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
     * Jégtörő nem hat a zúzalékra.
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
     * Hányófej eltávolítja a zúzalékot a sávról, ugyanúgy mint a havat.
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

    /**
     * Só nem hat a zúzalékra.
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
     * Már zúzalékos sávra újabb zúzalék szórásának nincs hatása.
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
