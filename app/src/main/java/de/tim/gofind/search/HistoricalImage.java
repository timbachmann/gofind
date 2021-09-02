package de.tim.gofind.search;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ParagraphStyle;

public class HistoricalImage implements Parcelable {

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

    public HistoricalImage(Parcel in){
        readFromParcel(in);
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

    private void readFromParcel(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.source = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.path = in.readString();
        this.segmentID = in.readString();
        this.objectID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(source);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(path);
        parcel.writeString(segmentID);
        parcel.writeString(objectID);
    }

    public static final Parcelable.Creator<HistoricalImage> CREATOR = new Parcelable.Creator<HistoricalImage>() {
        @Override
        public HistoricalImage createFromParcel(Parcel source) {
            return new HistoricalImage(source);
        }

        @Override
        public HistoricalImage[] newArray(int size) {
            return new HistoricalImage[size];
        }
    };
}
