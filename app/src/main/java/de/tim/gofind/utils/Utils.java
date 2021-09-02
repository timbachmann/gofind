package de.tim.gofind.utils;

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

        double a = Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0) + Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
