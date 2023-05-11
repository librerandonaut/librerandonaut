package com.librerandonaut.librerandonaut.attractor;

import java.util.HashSet;

import com.librerandonaut.librerandonaut.randomness.IRandomProvider;

public class RandomPointsProvider implements IRandomPointsProvider {
    private final static int POINTS_PER_KM_RADIUS = 100;
    private final static int MIN_POINTS = 100;

    private IRandomProvider randomProvider;
    public IRandomProvider getRandomProvider() {
        return randomProvider;
    }

    public RandomPointsProvider(IRandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public static int getEntropyUsage(int radius) {
        int pointsCount = getPointsCount(radius);
        // 2 points, 4 byte per point for integer
        // TODO check if this is always enough (unit test...)
        return pointsCount * 2 * 4;
    }

    public HashSet<Coordinates> getRandomPoints(Coordinates center, int radius) throws Exception {
        AttractorArea area = AttractorArea.getAttractorArea(center, radius);

        HashSet<Coordinates> points = new HashSet<>();
        int maxPoints = getPointsCount(radius);
        while (points.size() < maxPoints) {
            Coordinates randomPoint = getRandomCoordinates(area);

            int distance = center.getDistance(randomPoint);
            if (distance <= radius) {
                points.add(randomPoint);
            }
        }
        return points;
    }

    private static int getPointsCount(int radius) {
        int count = radius * POINTS_PER_KM_RADIUS / 1000;
        if (count < MIN_POINTS) {
            count = MIN_POINTS;
        }
        return count;
    }

    private Coordinates getRandomCoordinates(AttractorArea area) throws Exception {
        Coordinates corner = area.getCorner();
        double lat = corner.getLatitude() + (double)this.randomProvider.nextInt((int)area.getLatitudeDelta()) / 1000000.0;
        double lon = corner.getlongitude() + (double)this.randomProvider.nextInt((int)area.getLongituteDelta()) / 1000000.0;
        return new Coordinates(lat, lon);
    }
}
