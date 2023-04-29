package com.librerandonaut.librerandonaut;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import com.librerandonaut.librerandonaut.attractor.Attractor;
import com.librerandonaut.librerandonaut.attractor.Coordinates;
import com.librerandonaut.librerandonaut.attractor.IAttractorGenerator;
import com.librerandonaut.librerandonaut.attractor.Kde1AttractorGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Kde1AttractorGeneratorUnitTest {
    private IAttractorGenerator attractorGenerator;
    private TestRandomPointsProvider randomPointsProvider;

    public Kde1AttractorGeneratorUnitTest() throws IOException {
        randomPointsProvider = new TestRandomPointsProvider();
    }

    @Before
    public void init() {
        attractorGenerator = new Kde1AttractorGenerator(randomPointsProvider);
    }


    @Test
    public void getAttractor_withPredefinedRandomPoints_returnsCorrectAttractor() throws Exception {
        int radius = 1000;
        Attractor attractor = attractorGenerator.getAttractor(new Coordinates(0, 0), radius);
        assertNotNull(attractor);
        assertEquals(24.37847285688498, attractor.getCoordinates().getLatitude(), 0);
        assertEquals(-72.86952193737147, attractor.getCoordinates().getlongitude(), 0);
    }
}
