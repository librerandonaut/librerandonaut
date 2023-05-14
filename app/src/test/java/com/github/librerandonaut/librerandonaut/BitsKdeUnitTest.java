//package com.github.librerandonaut.librerandonaut;
//
//import com.google.gson.Gson;
//
//import org.junit.Test;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.Reader;
//
//import bits.kde.KernelDensityEstimate2d;
//import com.librerandonaut.librerandonaut.attractor.Attractor;
//import com.librerandonaut.librerandonaut.attractor.Coordinates;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public class BitsKdeUnitTest {
//    @Test
//    public void test() {
//        GetData getData = new GetData().invoke();
//        int len = getData.getLen();
//        double[] points = getData.getPoints();
//
//        KernelDensityEstimate2d k = KernelDensityEstimate2d.compute(points, 0, len, null, 0.0027, null);
//        double maxX = k.maxX();
//        double maxY = k.maxY();
//
//        double minX = k.minX();
//        double minY = k.minY();
//
//        int stepCount = 10000;
//        double stepSize = Math.min((maxX - minX) / stepCount, (maxY - minY) / stepCount);
//
//        double min = Double.POSITIVE_INFINITY;
//        double densXmax = 0, densYmax = 0;
//        double max = Double.NEGATIVE_INFINITY;
//        for(double x = minX; x < maxX; x += stepSize) {
//            for (double y = minY; y < maxY; y += stepSize) {
//                double value = k.apply(x, y);
//                if (value < min) {
//                    min = value;
//                }
//                if (value > max) {
//                    max = value;
//                    densXmax = x;
//                    densYmax = y;
//                }
//            }
//        }
//
//        double xxx = 111;
//        System.out.println(densXmax+"," +densYmax);
//    }
//
//    private class GetData {
//        private int len;
//        private double[] points;
//
//        public int getLen() {
//            return len;
//        }
//
//        public double[] getPoints() {
//            return points;
//        }
//
//        public GetData invoke() {
//            Gson gson = new Gson();
//            Attractor attractor = null;
//            try (Reader reader = new FileReader("/tmp/attractor.json")) {
//
//                // Convert JSON File to Java Object
//                attractor = gson.fromJson(reader, Attractor.class);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            assertNotNull(attractor);
//
//            len = attractor.getAllPoints().size();
//            points = new double[len * 2];
//
//            int i = 0;
//            for (Coordinates coords : attractor.getAllPoints()) {
//                points[i] = coords.getlongitude();
//                i++;
//                points[i] = coords.getLatitude();
//                i++;
//            }
//            return this;
//        }
//    }
//}
