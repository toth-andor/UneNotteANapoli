package controller;

import java.util.Random;

public class Randomizer implements IRandomizer {
    private boolean isSeedSet = false;
    private final Random rnd = new Random();

    public void enableSeed(int _seed) {
        this.isSeedSet = true;
        this.rnd.setSeed(_seed);
    }

    public void disableSeed() {
        this.isSeedSet = false;
        this.rnd.setSeed(System.nanoTime());
    }

    public int randomize(int _rangeStart, int _rangeEnd) {
        int min = Math.min(_rangeStart, _rangeEnd);
        int max = Math.max(_rangeStart, _rangeEnd);

        return rnd.nextInt(max - min + 1) + min;
    }
}
