package de.tim.gofind.search;

import java.util.List;

/**
 * TODO
 */
public class DataStorage {

    private List<HistoricalImage> imageList = null;
    private static DataStorage instance;

    public DataStorage() {
        instance = this;
    }

    public static DataStorage getInstance() {
        return instance;
    }

    public List<HistoricalImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<HistoricalImage> imageList) {
        this.imageList = imageList;
    }
}
