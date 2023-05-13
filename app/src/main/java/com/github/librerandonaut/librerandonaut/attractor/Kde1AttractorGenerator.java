package com.github.librerandonaut.librerandonaut.attractor;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Kde1AttractorGenerator extends BaseAttractorGenerator implements IAttractorGenerator {
    private final static int STEPS = 100;
    private final static double H = 10;

    private IRandomPointsProvider randomPointsProvider;

    public Kde1AttractorGenerator(IRandomPointsProvider randomPointsProvider) {
        this.randomPointsProvider = randomPointsProvider;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Attractor getAttractor(Coordinates center, int radius) throws Exception {
        HashSet<Coordinates> points = randomPointsProvider.getRandomPoints(center, radius);
        List<Double> xData = points.stream().map(x -> x.getlongitude()).collect(Collectors.toList());
        List<Double> yData = points.stream().map(x -> x.getLatitude()).collect(Collectors.toList());
        double xMax = Collections.max(xData);
        double yMax = Collections.max(yData);
        double xMin = Collections.min(xData);
        double yMin = Collections.min(yData);
        double xRange = xMax - xMin;
        double yRange = yMax - yMin;

        double xStepSize = xRange / STEPS;
        double yStepSize = yRange / STEPS;

        double[] xi = new double[STEPS];
        double[] yi = new double[STEPS];

        for (int i = 0; i < STEPS; i++) {
            xi[i] = xMin + i * xStepSize;
            yi[i] = yMin + i * yStepSize;
        }

        int n = (int) Math.pow(points.size(), 2);


        double temp, maxX = 0, maxY = 0, maxValue = 0;
        for (int i = 0; i < STEPS; i++) {
            for (int j = 0; j < STEPS; j++) {
                temp = 0;
                for (Coordinates point : points) {
                    temp += gaussKde(xi[i], point.getlongitude(), yi[j], point.getLatitude());
                }
                double value = 1.0 / ((double)n * H) * temp;
                if(value > maxValue) {
                    maxValue = value;
                    maxX = xi[i];
                    maxY = yi[j];
                }
            }
        }
        Coordinates attractorPoint = new Coordinates(maxY, maxX);
        AttractorTest test = testAttractor(points, attractorPoint, radius);
        return new Attractor(attractorPoint, test, points, this.getClass(), this.randomPointsProvider.getRandomProvider().getRandomSource());
    }

    private double gaussKde(double xi, double x, double yi, double y) {
        double sigma = 0.0025;
        return Math.exp(-(Math.pow((x - xi) / 1, 2) / (2 * Math.pow(sigma, 2)) + Math.pow((y - yi) / 1, 2) / (2 * Math.pow(sigma, 2))));
    }
}
