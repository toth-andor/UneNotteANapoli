package map;

import Vehicle.Vehicle;
import attachments.Attachment;

public interface ILane {
    boolean pushVehicle(Vehicle v, int timestamp);
    void popVehicle(Vehicle v);
    void snowFall(int snow);
    void cleanWithHead(Attachment head);
}
