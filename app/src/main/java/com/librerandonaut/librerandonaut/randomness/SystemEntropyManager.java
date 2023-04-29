package com.librerandonaut.librerandonaut.randomness;

public class SystemEntropyManager implements IEntropyManager {

    private SystemRandomSource systemRandomSource;

    public SystemEntropyManager() {
        systemRandomSource = new SystemRandomSource();
    }

    @Override
    public IRandomProvider loadRandomProvider(int entropyUsage) throws Exception {
        return systemRandomSource;
    }
}
