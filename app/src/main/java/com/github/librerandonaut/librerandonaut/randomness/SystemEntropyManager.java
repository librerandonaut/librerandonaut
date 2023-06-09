package com.github.librerandonaut.librerandonaut.randomness;

public class SystemEntropyManager implements IEntropyManager {

    private SystemRandomSource systemRandomSource;

    public SystemEntropyManager() {
        systemRandomSource = new SystemRandomSource();
    }

    @Override
    public LoadRandomProviderResult loadRandomProvider(int entropyUsage) throws Exception {
        return new LoadRandomProviderResult(systemRandomSource, true, "");
    }
}
