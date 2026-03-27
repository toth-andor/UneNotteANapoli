package map;

import attachments.Attachment;
import states.DryState;
import states.LaneState;

public class OutdoorLane extends Lane {

    private int saltedTimestamp;
    private LaneState currentState = new DryState();

    public void snowFall(int snow) {
        currentState = currentState.handleSnow(this, snow);
    }

    public void cleanWithHead(Attachment head) {
        // TODO: implement
    }

    public void setState(LaneState s) {
        this.currentState = s;
    }

    @Override
    public void cleanWithDragon() {
        currentState = new DryState();
    }

    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        currentState = currentState.cleanWithSaltVomitter(timestamp);
        this.saltedTimestamp = timestamp;
    }

    @Override
    public void cleanWithSweeper() {
        currentState = currentState.cleanWithSweeper();
    }

    @Override
    public void cleanWithIceBreaker() {
        currentState = currentState.cleanWithIceBreaker();
    }

    @Override
    public void cleanWithVomittingHead() {
        currentState = currentState.cleanWithVomittingHead();
    }
}
