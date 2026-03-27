package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

public class SaltedState extends LaneState {

    public LaneState handleSnow(OutdoorLane lane, int amount) {
        // TODO: implement
        return null;
    }

    public LaneState handleCleaning(OutdoorLane lane, Attachment head) {
        // TODO: implement
        return null;
    }

    public void handleTraffic(Vehicle v) {
        // TODO: implement
    }

    @Override
    public LaneState cleanWithSweeper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithSweeper'"
        );
    }

    @Override
    public LaneState cleanWithIceBreaker() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithIceBreaker'"
        );
    }

    @Override
    public LaneState cleanWithVomittingHead() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithVomittingHead'"
        );
    }
}
