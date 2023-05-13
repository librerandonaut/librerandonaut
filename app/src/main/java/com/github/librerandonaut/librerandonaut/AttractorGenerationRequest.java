package com.github.librerandonaut.librerandonaut;

import com.github.librerandonaut.librerandonaut.attractor.AttractorGeneratorType;
import com.github.librerandonaut.librerandonaut.attractor.Coordinates;
import com.github.librerandonaut.librerandonaut.randomness.RandomSource;

public class AttractorGenerationRequest {
    public Coordinates coordinates;
    public int radius;
    public RandomSource randomSource;
    public AttractorGeneratorType attractorGeneratorType;
}
