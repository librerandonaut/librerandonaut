package com.github.librerandonaut.librerandonaut;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

import com.github.librerandonaut.librerandonaut.attractor.Attractor;
import com.github.librerandonaut.librerandonaut.attractor.Coordinates;
import com.github.librerandonaut.librerandonaut.attractor.IRandomPointsProvider;
import com.github.librerandonaut.librerandonaut.randomness.IRandomProvider;

import static org.junit.Assert.assertNotNull;

public class TestRandomPointsProvider implements IRandomPointsProvider {

    private Attractor attractor;
    private TestRandomProvider testRandomProvider;

    public TestRandomPointsProvider() throws IOException {
        Path workingDir = Paths.get("", "src/test/resources");
        Path file = workingDir.resolve("attractor.json");

        Gson gson = new Gson();
        try (Reader reader = new FileReader(file.toString())) {

            // Convert JSON File to Java Object
            attractor = gson.fromJson(reader, Attractor.class);


        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(attractor);
        testRandomProvider = new TestRandomProvider();
    }

    @Override
    public IRandomProvider getRandomProvider() {
        return this.testRandomProvider;
    }

    @Override
    public HashSet<Coordinates> getRandomPoints(Coordinates center, int radius) throws Exception {
        return attractor.getAllPoints();
    }

    public Coordinates getAttractor() {
        return attractor.getCoordinates();
    }
}
