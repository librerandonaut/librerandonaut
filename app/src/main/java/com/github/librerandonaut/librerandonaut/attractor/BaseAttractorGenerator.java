package com.github.librerandonaut.librerandonaut.attractor;

import java.util.HashSet;

public abstract class BaseAttractorGenerator {
    protected AttractorTest testAttractor(HashSet<Coordinates> points, Coordinates attractor, int radius) {

        AttractorTest result = new AttractorTest(radius, 0, 0);
        double[] attractorDistances = new double[points.size()];

        int index = 0;
        for (Coordinates point : points) {
            int distance = attractor.getDistance(point);
            if (distance < result.getNearestDistance() ) {
                result.setNearestDistance(distance);
            }
            attractorDistances[index] = distance;
            index++;
        }

        result.setApproximateRadius(result.getNearestDistance() * 2);

        int pointsCount = 0;
        double sum = 0;
        while (pointsCount < 10) {
            sum = 0;
            pointsCount = 0;
            for (int i=0; i < attractorDistances.length; i++) {
                if (attractorDistances[i] <= result.getApproximateRadius()) {
                    sum += attractorDistances[i];
                    pointsCount++;
                }
                if (pointsCount < 10) {
                    result.setApproximateRadius(result.getApproximateRadius() + result.getNearestDistance());
                }
            }
        }

        result.setApproximateRadius(sum / pointsCount);
        sum = 0;
        for (int i=0; i < attractorDistances.length; i++) {
            if (attractorDistances[i] <= result.getApproximateRadius()) {
                sum++;
            }
        }

        result.setRelativeDensity((sum * Math.pow(radius, 2)) / (points.size() * Math.pow(result.getApproximateRadius(), 2)));
        return result;
    }

}
