package com.librerandonaut.librerandonaut.attractor;

public class AttractorTest {
    private double nearestDistance;
    private double approximateRadius;
    private double relativeDensity;

    public AttractorTest(double nearestDistance, double approximateRadius, double relativeDensity) {
        this.nearestDistance = nearestDistance;
        this.approximateRadius = approximateRadius;
        this.relativeDensity = relativeDensity;
    }

    public double getNearestDistance(){
        return nearestDistance;
    }

    public void setNearestDistance(double value){
        nearestDistance = value;
    }
    public double getApproximateRadius(){
        return approximateRadius;
    }
    public double getRelativeDensity(){
        return relativeDensity;
    }

    public void setApproximateRadius(double value) {
        approximateRadius = value;
    }

    public void setRelativeDensity(double value) {
        relativeDensity = value;
    }
}
