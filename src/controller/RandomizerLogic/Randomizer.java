package controller.RandomizerLogic;

import java.util.Random;

public class Randomizer implements IRandomizer {
    private boolean isSeedSet = false;
    private final Random rnd = new Random();
    private int seed = 0;

    public void enableSeed(int _seed) {
        this.isSeedSet = true;
        this.rnd.setSeed(_seed);
        this.seed = _seed;
    }

    public void disableSeed() {
        this.isSeedSet = false;
        this.rnd.setSeed(System.nanoTime());
        this.seed = 0;
    }

    public int randomize(int _rangeStart, int _rangeEnd) {
        int min = Math.min(_rangeStart, _rangeEnd);
        int max = Math.max(_rangeStart, _rangeEnd);

        return rnd.nextInt(max - min + 1) + min;
    }

    public boolean isSeedSet() { return isSeedSet; }
    public int getSeed()       { return seed; }
}
