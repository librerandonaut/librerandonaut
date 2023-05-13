package com.github.librerandonaut.librerandonaut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.librerandonaut.librerandonaut.randomness.IRandomProvider;
import com.github.librerandonaut.librerandonaut.randomness.RandomProvider;
import com.github.librerandonaut.librerandonaut.randomness.RandomSource;

public class TestRandomProvider implements IRandomProvider {

    private RandomProvider randomProvider;

    public TestRandomProvider() throws IOException {
        Path workingDir = Paths.get("", "src/test/resources");
        Path file = workingDir.resolve("entropyForTests");
        randomProvider = new RandomProvider(Files.readAllBytes(file), RandomSource.File);
    }

    @Override
    public RandomSource getRandomSource() {
        return randomProvider.getRandomSource();
    }

    @Override
    public int nextInt(int maxValue) throws Exception {
        return randomProvider.nextInt(maxValue);
    }

    @Override
    public int getByteIndex() {
        return randomProvider.getByteIndex();
    }

    @Override
    public void setByteIndex(int value) {
        randomProvider.setByteIndex(value);
    }

    @Override
    public int getEntropyPoolSize() {
        return randomProvider.getEntropyPoolSize();
    }

    @Override
    public boolean hasEntropyLeft(int entropyUsage) {
        return randomProvider.hasEntropyLeft(entropyUsage);
    }

    @Override
    public String getUsedEntropyAsString(int byteIndex, int size) {
        return randomProvider.getUsedEntropyAsString(byteIndex, size);
    }

    @Override
    public int getHashCode() {
        return randomProvider.getHashCode();
    }
}
