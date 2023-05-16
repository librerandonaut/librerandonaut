package com.github.librerandonaut.librerandonaut.randomness;

public interface IEntropyManager {
    LoadRandomProviderResult loadRandomProvider(int entropyUsage) throws Exception;
}
