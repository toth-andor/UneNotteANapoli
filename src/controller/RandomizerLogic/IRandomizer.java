package controller.RandomizerLogic;

public interface IRandomizer {
    public int randomize(int rangeStart, int rangeEnd);
    public void enableSeed(int _seed);
    public void disableSeed();
}
