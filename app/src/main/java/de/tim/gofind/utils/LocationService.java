package de.tim.gofind.utils;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import de.tim.gofind.R;
import de.tim.gofind.ar.ARActivity;
import de.tim.gofind.search.DataStorage;
import de.tim.gofind.search.HistoricalImage;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LOCATION";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private LocationManager locationManager;
    private MyLocationListener listener;
    private Location previousBestLocation = null;
    private final ArrayList<Integer> notificationIds = new ArrayList<>();
    private static LocationService instance;

    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new DataStorage();
        intent = new Intent(BROADCAST_ACTION);
    }

    public static LocationService getInstance() {
        return instance;
    }

    public ArrayList<Integer> getNotificationIds() {
        return notificationIds;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    private void checkDistance(HistoricalImage image, int id, double latitude, double longitude) {

        int distance = (int) Utils.haversineDistance(image.getLatitude(), image.getLongitude(), latitude, longitude);
        if (distance < getResources().getInteger(R.integer.ar_distance)) {
            if (!notificationIds.contains(id)) {
                notificationIds.add(id);
                notifyUser(image, distance, id);
            }
        } else {
            if (notificationIds.contains(id)) {
                //NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                //notificationManager.cancel(id);
                //notificationIds.remove(id);
            }
        }
    }

    public void notifyUser(HistoricalImage image, int distance, int id) {

        Intent intent = new Intent(this, ARActivity.class);
        intent.putExtra("path", image.getTitle());
        intent.putExtra("lat", image.getLatitude());
        intent.putExtra("lon", image.getLongitude());
        intent.putExtra("bearing", (int) image.getMarker().getTag());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notificationID = "CHANNEL_GO_FIND";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getBaseContext(), notificationID)
                .setSmallIcon(R.drawable.ic_twotone_my_location_24)
                .setContentTitle(image.getTitle())
                .setContentIntent(pendingIntent)
                .setContentText(String.format("You are within %sm of this Target!", distance))
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        Picasso.get().load(image.getPath()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                notification.setLargeIcon(bitmap);
                notification.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null));
                notificationManager.notify(id, notification.build());
                image.setNotified(true);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            if (isBetterLocation(loc, previousBestLocation)) {
                Log.i("***", "Location changed");
                int id = 0;
                if (DataStorage.getInstance().getImageList() != null) {
                    for (HistoricalImage image : DataStorage.getInstance().getImageList()) {
                        checkDistance(image, id, loc.getLatitude(), loc.getLongitude());
                        id++;
                    }
                }

                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                sendBroadcast(intent);
            }
        }


    }
}