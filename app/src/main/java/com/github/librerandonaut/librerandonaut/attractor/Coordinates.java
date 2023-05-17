package com.github.librerandonaut.librerandonaut.attractor;

public class Coordinates {
    public final static int EARTH_RADIUS = 6371000;

    private double latitude;
    private double longitude;

    public double getLatitude(){
        return latitude;
    }
    public double getlongitude(){
        return longitude;
    }

    public Coordinates(double latitute, double longitute){
        this.latitude = latitute;
        this.longitude = longitute;
    }

    public int getDistance(Coordinates other) {
        double lon0 = this.getlongitude();
        double lat0 = this.getLatitude();
        double lon1 = other.getlongitude();
        double lat1 = other.getLatitude();

        double dlon = (lon1 - lon0) * Math.PI / 180;
        double dlat = (lat1 - lat0) * Math.PI / 180;

        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) +
                Math.cos(lat0 * Math.PI / 180) * Math.cos(lat1 * Math.PI / 180) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int)(angle * EARTH_RADIUS);
    }
}
