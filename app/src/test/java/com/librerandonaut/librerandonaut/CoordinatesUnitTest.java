package com.librerandonaut.librerandonaut;

import org.junit.Test;

import com.librerandonaut.librerandonaut.attractor.Coordinates;

import static org.junit.Assert.assertEquals;

public class CoordinatesUnitTest {
    @Test
    public void getDistance_isCorrect() throws Exception {
        assertEquals(111194, new Coordinates(0, 0).getDistance(new Coordinates(0, 1)));
        assertEquals(111194, new Coordinates(0, 0).getDistance(new Coordinates(1, 0)));
        assertEquals(516575, new Coordinates(26, -74).getDistance(new Coordinates(29, -70)));
    }
}
