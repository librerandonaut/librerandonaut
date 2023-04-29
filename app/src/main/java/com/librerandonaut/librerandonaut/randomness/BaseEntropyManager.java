package com.librerandonaut.librerandonaut.randomness;

public abstract class BaseEntropyManager {
    protected RandomProvider randomProvider;
    protected byte[] entropy;

    protected void CheckInit() throws Exception {
        if (randomProvider == null)
            throw new Exception("randomProvider not initialized. init(..) called?");
    }

    public int nextInt(int maxValue) throws Exception {
        CheckInit();

        return randomProvider.nextInt(maxValue);
    }

    public int getByteIndex() {
        return randomProvider.getByteIndex();
    }

    public boolean hasEntropyLeft(int size) throws Exception {
        CheckInit();

        return (randomProvider.getByteIndex() + size) <= entropy.length;
    }
}
