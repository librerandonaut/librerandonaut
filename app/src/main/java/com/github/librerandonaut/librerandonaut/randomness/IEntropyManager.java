package com.github.librerandonaut.librerandonaut.randomness;

public interface IEntropyManager {
    IRandomProvider loadRandomProvider(int entropyUsage) throws Exception;
}