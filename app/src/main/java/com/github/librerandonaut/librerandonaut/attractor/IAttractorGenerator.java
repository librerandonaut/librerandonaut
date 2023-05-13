package com.github.librerandonaut.librerandonaut.attractor;

public interface IAttractorGenerator {
    Attractor getAttractor(Coordinates center, int radius) throws Exception;
}
