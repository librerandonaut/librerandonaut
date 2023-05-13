package com.github.librerandonaut.librerandonaut.attractor;

import java.util.HashSet;

public class FatumAttractorGenerator extends BaseAttractorGenerator implements IAttractorGenerator {
    private final static int MIN_SEARCH_CIRCLE_RADIUS = 50;
    private IRandomPointsProvider randomPointsProvider;

    public FatumAttractorGenerator(IRandomPointsProvider randomPointsProvider) {
        this.randomPointsProvider = randomPointsProvider;
    }


    public Attractor getAttractor(Coordinates center, int radius) throws Exception {

        HashSet<Coordinates> points = randomPointsProvider.getRandomPoints(center, radius);
        HashSet<Coordinates> pointsOriginal = (HashSet<Coordinates>)points.clone();
        Coordinates averageCoords = getAverageCoordinates(points);

        int searchCircleRadius = radius;

        while (searchCircleRadius > MIN_SEARCH_CIRCLE_RADIUS && points.size() > 1) {
            averageCoords = getAverageCoordinates(points);
            searchCircleRadius--;

            HashSet<Coordinates> pointsCopy = (HashSet<Coordinates>)points.clone();
            for(Coordinates point : pointsCopy) {
                int distance = averageCoords.getDistance(point);
                if (distance > searchCircleRadius) {
                    points.remove(point);
                }
            }
        }

        AttractorTest test = testAttractor(pointsOriginal, averageCoords, radius);
        return new Attractor(averageCoords, test, pointsOriginal, this.getClass(), this.randomPointsProvider.getRandomProvider().getRandomSource());
    }

    private Coordinates getAverageCoordinates(HashSet<Coordinates> coordinatesHashSet) {
        double latAvg = 0, lonAvg = 0;
        for(Coordinates coordinates : coordinatesHashSet) {
            latAvg += coordinates.getLatitude();
            lonAvg += coordinates.getlongitude();
        }
        int size = coordinatesHashSet.size();
        return new Coordinates(latAvg / size, lonAvg / size);
    }
}
