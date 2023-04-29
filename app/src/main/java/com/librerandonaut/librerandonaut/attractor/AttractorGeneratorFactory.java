package com.librerandonaut.librerandonaut.attractor;

import com.librerandonaut.librerandonaut.randomness.IRandomProvider;

public class AttractorGeneratorFactory {
    public static IAttractorGenerator getAttractorGenerator(AttractorGeneratorType type, IRandomProvider randomProvider) {
        switch (type) {
            default:
            case Fatum:
                return new FatumAttractorGenerator(new RandomPointsProvider((randomProvider)));
            case Kde1:
                return new Kde1AttractorGenerator(new RandomPointsProvider((randomProvider)));
            case Kde2:
                return new Kde2AttractorGenerator(new RandomPointsProvider((randomProvider)));
        }
    }
}
