package de.tim.gofind.search;

public class HistoricalImage {

    private String title;
    private String date;
    private String source;
    private double latitude;
    private double longitude;
    private String path;
    private String segmentID;
    private String objectID;

    public HistoricalImage() {}

    public HistoricalImage(String title, String date, String source, double latitude, double longitude, String path, String segmentID, String objectID) {
        this.title = title;
        this.date = date;
        this.source = source;
        this.latitude = latitude;
        this.longitude = longitude;
        this.path = path;
        this.segmentID = segmentID;
        this.objectID = objectID;
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
