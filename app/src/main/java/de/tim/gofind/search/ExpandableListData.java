package de.tim.gofind.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> location = new ArrayList<>();
        location.add("location");

        List<String> time = new ArrayList<>();
        time.add("time");

        List<String> photo = new ArrayList<>();
        photo.add("photo");

        expandableListDetail.put("Location", location);
        expandableListDetail.put("Time", time);
        expandableListDetail.put("Photo", photo);
        return expandableListDetail;
    }
}
