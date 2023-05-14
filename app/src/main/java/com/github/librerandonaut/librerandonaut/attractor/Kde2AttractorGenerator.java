/*
package com.github.librerandonaut.librerandonaut.attractor;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import bits.kde.KernelDensityEstimate2d;

public class Kde2AttractorGenerator extends BaseAttractorGenerator implements IAttractorGenerator {
    private final static int STEPS = 100;
    private final static int H = 10;

    private IRandomPointsProvider randomPointsProvider;

    public Kde2AttractorGenerator(IRandomPointsProvider randomPointsProvider) {
        this.randomPointsProvider = randomPointsProvider;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Attractor getAttractor(Coordinates center, int radius) throws Exception {
        HashSet<Coordinates> points = randomPointsProvider.getRandomPoints(center, radius);
        List<Double> xData = points.stream().map(x -> x.getlongitude()).collect(Collectors.toList());
        List<Double> yData = points.stream().map(x -> x.getLatitude()).collect(Collectors.toList());

        double[] data = new double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            data[i * 2] = xData.get(i);
            data[(i * 2) + 1] = yData.get(i);
        }

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
        double cellsize = Math.min(xRange, yRange) / (STEPS * 2.0);


        KernelDensityEstimate2d kde = KernelDensityEstimate2d.compute( data,0,data.length / 2,null, cellsize,null);

        double maxDensX = 0, maxDensY = 0, maxdensvalue = 0;
        for (int x = 0; x < STEPS; x++) {
            for (int y = 0; y < STEPS; y++) {
                double d = kde.apply(xi[x], yi[y]);
                if( d > maxdensvalue ) {
                    maxdensvalue = d;
                    maxDensX = xi[x];
                    maxDensY = yi[y];
                }
            }
        }

        Coordinates attractorPoint = new Coordinates(maxDensY, maxDensX);
        AttractorTest test = testAttractor(points, attractorPoint, radius);
        return new Attractor(attractorPoint, test, points, this.getClass(), this.randomPointsProvider.getRandomProvider().getRandomSource());
    }
}
*/