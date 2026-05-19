package controller.RandomizerLogic;

public interface IRandomizer {
     int randomize(int rangeStart, int rangeEnd);
     void enableSeed(int _seed);
     void disableSeed();
     boolean isSeedSet();
     int getSeed();

}
