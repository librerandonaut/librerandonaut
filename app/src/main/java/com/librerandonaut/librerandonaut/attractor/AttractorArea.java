package com.librerandonaut.librerandonaut.attractor;

public class AttractorArea {

    private double longituteDelta;
    private double latitudeDelta;
    private Coordinates center;
    private Coordinates corner;

    public Coordinates getCenter(){
        return center;
    }
    public Coordinates getCorner(){
        return corner;
    }

    public double getLongituteDelta(){
        return longituteDelta;
    }
    public double getLatitudeDelta(){
        return latitudeDelta;
    }

    public AttractorArea(Coordinates center, Coordinates corner, double latitudeDelta, double longituteDelta) {
        this.center = center;
        this.corner = corner;
        this.longituteDelta = longituteDelta;
        this.latitudeDelta = latitudeDelta;
    }

    public static AttractorArea getAttractorArea(Coordinates center, int radius) {
        double lat = center.getLatitude();
        double lon = center.getlongitude();
        double lat01 = lat + radius * Math.cos(180 * Math.PI / 180) / (Coordinates.EARTH_RADIUS * Math.PI / 180);
        double dlat = ((lat + radius / (Coordinates.EARTH_RADIUS * Math.PI / 180)) - lat01) * 1000000;
        double lon01 = lon + radius * Math.sin(270 * Math.PI / 180) / Math.cos(lat * Math.PI / 180) / (Coordinates.EARTH_RADIUS * Math.PI / 180);
        double dlon = ((lon + radius * Math.sin(90 * Math.PI / 180) / Math.cos(lat * Math.PI / 180) / (Coordinates.EARTH_RADIUS * Math.PI / 180)) - lon01) * 1000000;
        return new AttractorArea(center, new Coordinates(lat01, lon01), dlat, dlon);
    }
}
