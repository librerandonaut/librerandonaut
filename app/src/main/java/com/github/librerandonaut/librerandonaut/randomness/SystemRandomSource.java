package com.github.librerandonaut.librerandonaut.randomness;

import java.util.Random;

public class SystemRandomSource implements IRandomProvider {

    private int byteIndex = 0;
    private Random random = new Random();

    @Override
    public int nextInt(int maxValue) {
        byteIndex += 4;
        return random.nextInt(maxValue);
    }

    @Override
    public int getByteIndex() {
        return byteIndex;
    }

    @Override
    public void setByteIndex(int value) {
        byteIndex = value;
    }

    @Override
    public int getEntropyPoolSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasEntropyLeft(int entropyUsage) {
        return true;
    }

    @Override
    public String getUsedEntropyAsString(int byteIndex, int size) {
        return "";
    }

    @Override
    public int getHashCode() {
        return 0;
    }

    @Override
    public RandomSource getRandomSource() {
        return RandomSource.System;
    }
}
