package states;

import Vehicle.Vehicle;
import attachments.Attachment;
import map.OutdoorLane;

public abstract class LaneState {

    public abstract LaneState handleSnow(OutdoorLane lane, int amount);

    // public abstract LaneState handleCleaning(OutdoorLane lane, Attachment head); // Szerintem ez nem kell
    public abstract void handleTraffic(Vehicle v);

    public LaneState cleanWithSaltVomitter(int timestamp) {
        // TODO: megfellően inicializálni, hogy számon tudja tartani a só hatását még az is lehet,
        // hogy ezt le kell vinni a specifikus LaneState szintre (szinte biztos, akkor viszont int abstrakt)
        return new SaltedState();
    }

    public abstract LaneState cleanWithSweeper();

    public abstract LaneState cleanWithIceBreaker();

    public abstract LaneState cleanWithVomittingHead();
}
