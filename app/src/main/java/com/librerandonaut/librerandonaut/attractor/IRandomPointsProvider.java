package com.librerandonaut.librerandonaut.attractor;

import java.util.HashSet;

import com.librerandonaut.librerandonaut.randomness.IRandomProvider;

public interface IRandomPointsProvider {
    HashSet<Coordinates> getRandomPoints(Coordinates center, int radius) throws Exception;
    IRandomProvider getRandomProvider();
}
