package de.tim.gofind.search;

/**
 * TODO
 */
public class HistoricalMap {

    private String name;
    private String path;
    private int bearing;
    private int width;
    private int height;
    private float scaling;

    public HistoricalMap(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public HistoricalMap(String name, String path, int bearing, int width, int height, float scaling) {
        this.name = name;
        this.path = path;
        this.bearing = bearing;
        this.width = width;
        this.height = height;
        this.scaling = scaling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScaling() {
        return scaling;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }
}
