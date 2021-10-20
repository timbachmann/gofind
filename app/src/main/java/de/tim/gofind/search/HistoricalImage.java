package de.tim.gofind.search;

import com.google.android.gms.maps.model.Marker;

/**
 * TODO
 */
public class HistoricalImage {

    private String title;
    private String date;
    private String source;
    private double latitude;
    private double longitude;
    private String path;
    private String segmentID;
    private String objectID;
    private int bearing = 0;
    private boolean notified = false;
    private Marker marker;
    private int distance;
    public static final String DIVIDER = "%%";

    public HistoricalImage() {}

    public HistoricalImage(String serializedString) {
        String[] attributes = serializedString.split(DIVIDER);
        title = attributes[0];
        date = attributes[1];
        source = attributes[2];
        latitude = Double.parseDouble(attributes[3]);
        longitude = Double.parseDouble(attributes[4]);
        path = attributes[5];
        segmentID = attributes[6];
        objectID = attributes[7];
        bearing = Integer.parseInt(attributes[8]);
        distance = Integer.parseInt(attributes[9]);
    }

    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title).append(DIVIDER);
        stringBuilder.append(date).append(DIVIDER);
        stringBuilder.append(source).append(DIVIDER);
        stringBuilder.append(latitude).append(DIVIDER);
        stringBuilder.append(longitude).append(DIVIDER);
        stringBuilder.append(path).append(DIVIDER);
        stringBuilder.append(segmentID).append(DIVIDER);
        stringBuilder.append(objectID).append(DIVIDER);
        stringBuilder.append(bearing).append(DIVIDER);
        stringBuilder.append(distance);

        return stringBuilder.toString();
    }

    public void print() {
        System.out.println("HistoricalImage {");
        System.out.println(String.format("title: %s", title));
        System.out.println(String.format("date: %s", date));
        System.out.println(String.format("source: %s", source));
        System.out.println(String.format("latitude: %s", latitude));
        System.out.println(String.format("longitude: %s", longitude));
        System.out.println(String.format("path: %s", path));
        System.out.println(String.format("segmentID: %s", segmentID));
        System.out.println(String.format("objectID: %s", objectID));
        System.out.println(String.format("bearing: %s", bearing));
        System.out.println(String.format("distance: %s", distance));
        System.out.println("}");
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSegmentID() {
        return segmentID;
    }

    public void setSegmentID(String segmentID) {
        this.segmentID = segmentID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
