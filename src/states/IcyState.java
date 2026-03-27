package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

/**
 * Jeges sávállapot: a sáv felszíne jeges, a járművek megcsúsznak és balesetet szenvednek.
 * Jégtörő vagy VomittingHead hatására {@link DryState}-be vált.
 * Seprű nem tudja eltávolítani a jeget.
 */
public class IcyState extends LaneState {

    /**
     * Jeges sávra hulló hó nem változtatja az állapotot — jegesnél nem lehet jegesebb.
     *
     * @param lane   az érintett kültéri sáv
     * @param amount a lehullott hó mennyisége
     * @return {@code this}
     */
    public LaneState handleSnow(OutdoorLane lane, int amount) {
        return this; //jegesnel nem lehet jegesebb
    }

    public LaneState handleCleaning(OutdoorLane lane, Attachment head) {
        return new DryState();
    }

    /**
     * Jeges sávon az áthaladó jármű megcsúszik és kiesik a sávból.
     *
     * @param v az áthaladó jármű
     */
    public void handleTraffic(Vehicle v) {
        v.gotoLane(null, 0);
    }

    /**
     * Seprű nem képes eltávolítani a jeget, az állapot változatlan marad.
     *
     * @return {@code this}
     */
    @Override
    public LaneState cleanWithSweeper() {
        return this; // seprű nem tudja eltávolítani a jeget
    }

    /**
     * Jégtörő eltávolítja a jeget a sávról.
     *
     * @return {@link DryState}
     */
    @Override
    public LaneState cleanWithIceBreaker() {
        return new DryState();
    }

    /**
     * VomittingHead eltávolítja a jeget a sávról.
     *
     * @return {@link DryState}
     */
    @Override
    public LaneState cleanWithVomittingHead() {
        return new DryState();
    }
}
