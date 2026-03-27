package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import states.LaneState;
import java.util.ArrayList;
import java.util.List;

public abstract class Lane implements ILane {

    private Road road;
    protected LaneState currentState;
    private List<Vehicle> vehicles;

    public Lane() {
        this.vehicles = new ArrayList<>();
    }

    public boolean pushVehicle(Vehicle v, int timestamp) {
        vehicles.add(v);
        return true;
    }

    public void popVehicle(Vehicle v) {
        vehicles.remove(v);
    }

    public abstract void snowFall(int snow);

    public abstract void cleanWithHead(Attachment head);

    public Road getRoad() {
        return road;
    }

    public void handleTraffic(Vehicle v, int timestamp) {
        // Nem biztos, de lehet szükség van a timestampre
        currentState.handleTraffic(v);
    }

    // TODO: ezeket a TunnelLane-ben üresek az OutdoorLane-ben pedig a legtöbbet egyenesen tovább kell passzolni a fejnek
    public abstract void cleanWithDragon();

    public abstract void cleanWithSaltVomitter(int timestamp);

    public abstract void cleanWithSweeper();

    public abstract void cleanWithIceBreaker();

    public abstract void cleanWithVomittingHead();
}
