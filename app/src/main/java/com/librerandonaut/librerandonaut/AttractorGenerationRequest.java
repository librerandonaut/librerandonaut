package com.librerandonaut.librerandonaut;

import com.librerandonaut.librerandonaut.attractor.AttractorGeneratorType;
import com.librerandonaut.librerandonaut.attractor.Coordinates;
import com.librerandonaut.librerandonaut.randomness.RandomSource;

public class AttractorGenerationRequest {
    public Coordinates coordinates;
    public int radius;
    public RandomSource randomSource;
    public AttractorGeneratorType attractorGeneratorType;
}
