package com.librerandonaut.librerandonaut;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import com.librerandonaut.librerandonaut.attractor.Coordinates;
import com.librerandonaut.librerandonaut.attractor.RandomPointsProvider;
import com.librerandonaut.librerandonaut.randomness.SystemRandomSource;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RandomPointsProviderUnitTest {

    private RandomPointsProvider randomPointsProvider;
    @Before
    public void init() {
        randomPointsProvider = new RandomPointsProvider(new SystemRandomSource());
    }
    @Test
    public void getEntropyUsage_isCorrect() {
        assertEquals(1600, RandomPointsProvider.getEntropyUsage(1000));
        assertEquals(1600, RandomPointsProvider.getEntropyUsage(500));
        assertEquals(3200, RandomPointsProvider.getEntropyUsage(2000));
        assertEquals(16000, RandomPointsProvider.getEntropyUsage(10000));
    }

    @Test
    public void getRandomPoints_for1000m_isCorrect() throws Exception {
        HashSet<Coordinates> points = randomPointsProvider.getRandomPoints(new Coordinates(0, 0), 1000);
        assertNotNull(points);
        assertEquals(100, points.size());
    }

    @Test
    public void getRandomPoints_for2000m_isCorrect() throws Exception {
        HashSet<Coordinates> points = randomPointsProvider.getRandomPoints(new Coordinates(0, 0), 2000);
        assertNotNull(points);
        assertEquals(200, points.size());
    }


}
