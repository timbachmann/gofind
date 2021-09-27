package de.tim.gofind.utils;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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

    public static double calculateHeadingAngle(double targetLat, double targetLon, double currentLat, double currentLon) {
        double currentLatitudeRadians = Math.toRadians(currentLat);
        double destinationLatitudeRadians = Math.toRadians(targetLat);
        double deltaLongitude = Math.toRadians(targetLon - currentLon);

        double y = cos(currentLatitudeRadians) * sin(destinationLatitudeRadians) -
                sin(currentLatitudeRadians) * cos(destinationLatitudeRadians) * cos(deltaLongitude);
        double x = sin(deltaLongitude) * cos(destinationLatitudeRadians);
        double headingAngle = Math.toDegrees(atan2(x, y));

        return (headingAngle + 360) % 360;
    }

    public static Bitmap fromBase64(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


}
