package com.github.librerandonaut.librerandonaut.attractor;

import java.util.HashSet;

import com.github.librerandonaut.librerandonaut.randomness.IRandomProvider;

public interface IRandomPointsProvider {
    HashSet<Coordinates> getRandomPoints(Coordinates center, int radius) throws Exception;
    IRandomProvider getRandomProvider();
}
