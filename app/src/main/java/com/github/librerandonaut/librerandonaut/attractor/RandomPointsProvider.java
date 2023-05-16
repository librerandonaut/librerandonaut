package com.github.librerandonaut.librerandonaut.attractor;

import android.util.Log;

import java.util.HashSet;

import com.github.librerandonaut.librerandonaut.randomness.IRandomProvider;

public class RandomPointsProvider implements IRandomPointsProvider {
    private final static int POINTS_PER_KM_RADIUS = 100;
    private final static int MIN_POINTS = 100;

    static final String TAG = "RandomPointsProvider";

    private IRandomProvider randomProvider;
    public IRandomProvider getRandomProvider() {
        return randomProvider;
    }

    public RandomPointsProvider(IRandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public static int getEntropyUsage(int radius) {
        int pointsCount = getPointsCount(radius);
        // 2 points, 4 byte per point for integer + 40% more
        // TODO check if this is always enough (unit test...)
        return (int)((double)pointsCount * 2.0 * 4.0 * 1.40);
    }

    public HashSet<Coordinates> getRandomPoints(Coordinates center, int radius) throws Exception {
        AttractorArea area = AttractorArea.getAttractorArea(center, radius);

        HashSet<Coordinates> points = new HashSet<>();
        int maxPoints = getPointsCount(radius);
        int counter = 0;
        while (points.size() < maxPoints) {

            Log.v(TAG, "points.size=" + points.size() + ", counter=" + counter++);

            Coordinates randomPoint = getRandomCoordinates(area);

            int distance = center.getDistance(randomPoint);
            if (distance <= radius) {
                points.add(randomPoint);
            } else {
                Log.v(TAG, "distance " + distance + " is greater than radius " + radius);
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
