package com.librerandonaut.librerandonaut.randomness;

public interface IRandomProvider {
    int nextInt(int maxValue) throws Exception;
    int getByteIndex();
    void setByteIndex(int value);
    int getEntropyPoolSize();
    boolean hasEntropyLeft(int entropyUsage);
    String getUsedEntropyAsString(int byteIndex, int size);
    int getHashCode();
    RandomSource getRandomSource();
}
