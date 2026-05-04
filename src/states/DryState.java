package states;

import Vehicle.Vehicle;
import map.OutdoorLane;


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
        if (lane.getSnowAmount() >= SNOW_THRESHOLD) {
            SnowyState snowy = new SnowyState();
            return snowy;
        }
        return this;
    }

    /**
     * Száraz sávon a forgalom akadálytalan, nincs hatás.
     *
     * @param v az áthaladó jármű
     * @return this
     */
    public LaneState handleTraffic(Vehicle v) {
        return this;
    }

    /**
     * Száraz sávon a seprűnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this;
    }

    /**
     * Száraz sávon a jégtörőnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return this;
    }

    /**
     * Száraz sávon a hányófejnek nincs hatása.
     *
     * @return this
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return this;
    }
}
