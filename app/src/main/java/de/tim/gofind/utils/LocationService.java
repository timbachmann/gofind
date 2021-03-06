package de.tim.gofind.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.tim.gofind.R;
import de.tim.gofind.ar.ARActivity;
import de.tim.gofind.search.DataStorage;
import de.tim.gofind.search.HistoricalImage;
import de.tim.gofind.search.SearchActivity;

/**
 * TODO
 */
public class LocationService extends Service {

    public static final String BROADCAST_LOCATION = "LOCATION";
    public static final String ACTION_START = "START";
    public static boolean isServiceRunning = false;
    private static final int TWO_MINUTES = 120000;
    private static final String TAG = "LocationService";
    private final ArrayList<Integer> notificationIds = new ArrayList<>();
    private static LocationService instance;
    private LocationManager locationManager;
    private MyLocationListener listener;
    private Location previousBestLocation = null;
    private SharedPreferences sharedPref;
    private Intent intent;

    /**
     * TODO
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startServiceWithNotification();
    }

    /**
     * TODO
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals(ACTION_START)) {
            startServiceWithNotification();
        } else {
            stopMyService();
        }

        instance = this;
        this.intent = new Intent(BROADCAST_LOCATION);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);

        return START_STICKY;
    }

    /**
     * TODO
     */
    @Override
    public void onDestroy() {
        locationManager.removeUpdates(listener);
        isServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * TODO
     */
    void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent locationService = new Intent(this, LocationService.class);
        PendingIntent stopLocationService = PendingIntent.getService(this, 0, locationService, 0);

        Notification notification = new NotificationCompat.Builder(getBaseContext(), SearchActivity.NOTIFICATION_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setDefaults(NotificationCompat.FLAG_FOREGROUND_SERVICE)
                .setContentText("Location Service running...")
                .setSmallIcon(R.drawable.ic_twotone_my_location_24)
                .setOngoing(true)
                .addAction(R.drawable.ic_twotone_close_24, "Stop", stopLocationService)
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);
    }

    /**
     * TODO
     */
    private void stopMyService() {
        isServiceRunning = false;
        stopForeground(true);
        stopSelf();
    }

    public static LocationService getInstance() {
        return instance;
    }

    public ArrayList<Integer> getNotificationIds() {
        return notificationIds;
    }

    /**
     * TODO
     * @param location
     * @param currentBestLocation
     * @return
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        // Check time
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check accuracy
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality
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

    /**
     * TODO
     * @param image
     * @param id
     * @param latitude
     * @param longitude
     */
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

    /**
     * TODO
     * @param image
     * @param distance
     * @param id
     */
    public void notifyUser(HistoricalImage image, int distance, int id) {

        if (image.getMarker() == null) {
            return;
        }

        Intent intent = new Intent(this, ARActivity.class);
        intent.putExtra("image", image.serialize());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), SearchActivity.NOTIFICATION_ID)
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
                Log.i(TAG, "Couldn't load notification bitmap!");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    /**
     * TODO
     */
    class MyLocationListener implements LocationListener {

        /**
         * TODO
         * @param loc
         */
        public void onLocationChanged(final Location loc) {
            if (isBetterLocation(loc, previousBestLocation)) {
                Log.i(TAG, "Location changed!");
                previousBestLocation = loc;
                int id = 2;

                Set<String> serializedImages = sharedPref.getStringSet(getString(R.string.shared_preferences_key), null);
                if (serializedImages != null) {
                    List<HistoricalImage> imageList = serializedImages.stream().map(HistoricalImage::new).collect(Collectors.toList());
                    for (HistoricalImage image : imageList) {
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