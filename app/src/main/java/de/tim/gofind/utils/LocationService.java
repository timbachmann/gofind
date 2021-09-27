package de.tim.gofind.utils;

import android.Manifest;
import android.app.Notification;
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
    private static final String TAG = "LocationService";
    private MyLocationListener listener;
    private Location previousBestLocation = null;
    private final ArrayList<Integer> notificationIds = new ArrayList<>();
    private static LocationService instance;
    private final String NOTIFICATION_ID = "CHANNEL_GO_FIND";
    private DataStorage dataStorage;

    Intent intent;

    public static boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        startServiceWithNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals("START")) {
            startServiceWithNotification();
        } else {
            stopMyService();
        }

        instance = this;
        if (DataStorage.getInstance() == null) {
            dataStorage = new DataStorage();
        } else {
            dataStorage = DataStorage.getInstance();
        }

        this.intent = new Intent(BROADCAST_ACTION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
        return START_STICKY;
    }

    // In case the service is deleted or crashes some how
    @Override
    public void onDestroy() {
        locationManager.removeUpdates(listener);
        isServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent locationService = new Intent(this, LocationService.class);
        PendingIntent stopLocationService = PendingIntent.getService(this, 0, locationService, 0);

        Notification notification = new NotificationCompat.Builder(getBaseContext(), NOTIFICATION_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setDefaults(NotificationCompat.FLAG_FOREGROUND_SERVICE)
                .setContentText("Location Service running...")
                .setSmallIcon(R.drawable.ic_twotone_my_location_24)
                .setOngoing(true)
                .addAction(R.drawable.ic_baseline_center_focus_weak_24, "Stop", stopLocationService)
                //.setDeleteIntent(contentPendingIntent) // if needed
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(1, notification);
    }

    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    public static LocationService getInstance() {
        return instance;
    }

    public ArrayList<Integer> getNotificationIds() {
        return notificationIds;
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

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_ID)
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
                Log.i(TAG, "Location changed");
                int id = 2;
                if (dataStorage.getImageList() != null) {
                    for (HistoricalImage image : dataStorage.getImageList()) {
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