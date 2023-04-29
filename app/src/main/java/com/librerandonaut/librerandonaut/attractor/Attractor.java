package com.librerandonaut.librerandonaut.attractor;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.util.HashSet;

import com.librerandonaut.librerandonaut.randomness.RandomSource;

public class Attractor {
    private AttractorTest attractorTest;
    private Coordinates coordinates;
    private HashSet<Coordinates> allPoints;
    private Instant generationTimestamp;
    private String identifier;
    private String generatorType;
    private String randomSourceName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Attractor(Coordinates coordinates, AttractorTest attractorTest, HashSet<Coordinates> allPoints, Class generatorType,
                     RandomSource randomSource){
        this.randomSourceName = randomSource.name();
        this.generatorType = generatorType.toString();
        this.coordinates = coordinates;
        this.attractorTest = attractorTest;
        this.allPoints = allPoints;
        generationTimestamp = Instant.now();

        identifier = "attractor_" + generationTimestamp.toString().replace(":", "") + ".json";
    }

    public AttractorTest getAttractorTest(){
        return attractorTest;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public HashSet<Coordinates> getAllPoints(){
        return allPoints;
    }
    public String getGenerationTimestamp(){
        return generationTimestamp.toString();
    }
    public String getIdentifier() {
        return identifier;
    }
    public String getGeneratorType() {
        return generatorType;
    }
    public String getRandomSourceName() {
        return randomSourceName;
    }
}
