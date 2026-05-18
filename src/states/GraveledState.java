package states;

import vehicle.Vehicle;
import map.OutdoorLane;

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
        if (lane.getSnowAmount() >= SNOW_THRESHOLD) {
            SnowyState snowy = new SnowyState();
            return snowy;
        }
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
        return this;


    }

    /**
     * Söprő eltávolítja a zúzalékot a sávról, ugyanúgy mint a havat.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithSweeper() {
        DryState dry = new DryState();
        return dry;
    }

    /**
     * Jégtörő nem hat a zúzalékra.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this;
    }

    /**
     * Hányófej eltávolítja a zúzalékot a sávról, ugyanúgy mint a havat.
     *
     * @return DryState
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        DryState dry = new DryState();
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
        return this;
    }
}
