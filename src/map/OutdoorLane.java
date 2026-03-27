package map;

import attachments.Attachment;
import states.DryState;
import states.LaneState;

public class OutdoorLane extends Lane {

    protected LaneState currentState;
    private int saltedTimestamp;

    public void snowFall(int snow) {
        // TODO: implement
    }

    public void cleanWithHead(Attachment head) {
        // TODO: implement
    }

    public void setState(LaneState s) {
        // TODO: implement
    }

    @Override
    public void cleanWithDragon() {
        currentState = new DryState();
    }

    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        // TODO Auto-generated method stub
        currentState = currentState.cleanWithSaltVomitter(timestamp);
    }

    @Override
    public void cleanWithSweeper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithSweeper'"
        );
    }

    @Override
    public void cleanWithIceBreaker() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithIceBreaker'"
        );
    }

    @Override
    public void cleanWithVomittingHead() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'cleanWithVomittingHead'"
        );
    }
}
