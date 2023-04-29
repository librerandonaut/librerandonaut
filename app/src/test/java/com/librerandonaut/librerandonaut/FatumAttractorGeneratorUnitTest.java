package com.librerandonaut.librerandonaut;

import org.junit.Test;
import org.junit.Before;

import java.io.IOException;

import com.librerandonaut.librerandonaut.attractor.Attractor;
import com.librerandonaut.librerandonaut.attractor.FatumAttractorGenerator;
import com.librerandonaut.librerandonaut.attractor.Coordinates;
import com.librerandonaut.librerandonaut.attractor.IAttractorGenerator;
import com.librerandonaut.librerandonaut.attractor.RandomPointsProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FatumAttractorGeneratorUnitTest {
    private IAttractorGenerator attractorGenerator;
    private TestRandomPointsProvider randomPointsProvider;

    public FatumAttractorGeneratorUnitTest() throws IOException {
        randomPointsProvider = new TestRandomPointsProvider();
    }

    @Before
    public void init() {
        attractorGenerator = new FatumAttractorGenerator(randomPointsProvider);
    }

    @Test
    public void testRandomPointsProvider_returnsCorrectData() throws Exception {
        assertEquals(100, randomPointsProvider.getRandomPoints(null, 0).size());
        assertEquals(24.37909917688498, randomPointsProvider.getAttractor().getLatitude(), 0);
        assertEquals(-72.86948031737148, randomPointsProvider.getAttractor().getlongitude(), 0);
    }

    @Test
    public void getAttractor_withPredefinedRandomPoints_returnsCorrectAttractor() throws Exception {
        int radius = 1000;
        Attractor attractor = attractorGenerator.getAttractor(new Coordinates(0, 0), radius);
        assertNotNull(attractor);
        assertEquals(24.37909917688498, attractor.getCoordinates().getLatitude(), 0);
        assertEquals(-72.86948031737148, attractor.getCoordinates().getlongitude(), 0);
    }

    @Test
    public void getAttractor_withRandomPointsGeneratedFromEntropyFile_returnsCorrectAttractor() throws Exception {
        TestRandomProvider randomProvider = new TestRandomProvider();
        IAttractorGenerator attractorGenerator = new FatumAttractorGenerator(new RandomPointsProvider(randomProvider));
        Attractor attractor = attractorGenerator.getAttractor(new Coordinates(0, 0), 1000);
        assertNotNull(attractor);
        assertEquals(0.006977450607479361, attractor.getCoordinates().getLatitude(), 0);
        assertEquals(0.0017474506074793602, attractor.getCoordinates().getlongitude(), 0);
    }
}
