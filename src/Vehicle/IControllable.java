package Vehicle;

import map.Lane;

public interface IControllable {
    boolean gotoLane(Lane l, int timestamp);
    void crash(int timestamp);
    void interactWithLane(Lane l);
}
