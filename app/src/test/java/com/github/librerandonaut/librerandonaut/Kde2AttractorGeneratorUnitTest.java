/*
package com.github.librerandonaut.librerandonaut;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import com.github.librerandonaut.librerandonaut.attractor.Attractor;
import com.github.librerandonaut.librerandonaut.attractor.Coordinates;
import com.github.librerandonaut.librerandonaut.attractor.IAttractorGenerator;
import com.github.librerandonaut.librerandonaut.attractor.Kde2AttractorGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Kde2AttractorGeneratorUnitTest {
    private IAttractorGenerator attractorGenerator;
    private TestRandomPointsProvider randomPointsProvider;

    public Kde2AttractorGeneratorUnitTest() throws IOException {
        randomPointsProvider = new TestRandomPointsProvider();
    }

    @Before
    public void init() {
        attractorGenerator = new Kde2AttractorGenerator(randomPointsProvider);
    }


    @Test
    public void getAttractor_withPredefinedRandomPoints_returnsCorrectAttractor() throws Exception {
        int radius = 1000;
        Attractor attractor = attractorGenerator.getAttractor(new Coordinates(0, 0), radius);
        assertNotNull(attractor);
        assertEquals(24.37830769688498, attractor.getCoordinates().getLatitude(), 0);
        assertEquals(-72.86934101737147, attractor.getCoordinates().getlongitude(), 0);
    }
}
*/