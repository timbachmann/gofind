package de.tim.gofind.utils;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.location.Location;

public class Utils {

    public static double degreesToRadians(double angle) {
        return angle * (Math.PI / 180.0);
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3;
        double phi1 = degreesToRadians(lat1);
        double phi2 = degreesToRadians(lat2);
        double deltaPhi = degreesToRadians(lat2 - lat1);
        double deltaLambda = degreesToRadians(lon2 - lon1);

        double a = sin(deltaPhi / 2.0) * sin(deltaPhi / 2.0) + cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2.0) * sin(deltaLambda / 2.0);
        double c = 2.0 * atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static double azimuthToTarget(double lat1, double lon1, double lat2, double lon2) {
        double azi = Math.abs(Math.toDegrees(Math.atan((lon1-lon2)/(lat1-lat2))));
        if((lon1-lon2)>0&&(lat1-lat2)<0){
            azi = 180 - azi;
        }
        if((lon1-lon2)<0&&(lat1-lat2)<0){
            azi = 180 + azi;
        }
        if((lon1-lon2)<0&&(lat1-lat2)>0){
            azi = 360 - azi;
        }
        return azi;
    }

    private double calculateHeadingAngle(Location currentLocation, Location destinationLocation) {
        double currentLatitudeRadians = Math.toRadians(currentLocation.getLatitude());
        double destinationLatitudeRadians = Math.toRadians(destinationLocation.getLatitude());
        double deltaLongitude = Math.toRadians(destinationLocation.getLongitude() - currentLocation.getLongitude());

        double y = cos(currentLatitudeRadians) * sin(destinationLatitudeRadians) -
                sin(currentLatitudeRadians) * cos(destinationLatitudeRadians) * cos(deltaLongitude);
        double x = sin(deltaLongitude) * cos(destinationLatitudeRadians) ;
        double headingAngle = Math.toDegrees(atan2(x, y));

        return (headingAngle + 360) % 360;
    }
}
