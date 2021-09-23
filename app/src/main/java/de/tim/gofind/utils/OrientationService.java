package de.tim.gofind.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import de.tim.gofind.search.DataStorage;

public class OrientationService extends Service implements SensorEventListener {

    public static final String BROADCAST_ACTION = "ORIENTATION";
    private WindowManager windowManager;
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        new DataStorage();
        intent = new Intent(BROADCAST_ACTION);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
            case Sensor.TYPE_ROTATION_VECTOR:
                processSensorOrientation(event.values);
                break;
            default:
                Log.e("DeviceOrientation", "Sensor event type not supported");
                break;
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void processSensorOrientation(float[] rotation) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotation);
        final int worldAxisX;
        final int worldAxisY;

        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
                worldAxisX = SensorManager.AXIS_Z;
                worldAxisY = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                worldAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisY = SensorManager.AXIS_MINUS_Z;
                break;
            case Surface.ROTATION_270:
                worldAxisX = SensorManager.AXIS_MINUS_Z;
                worldAxisY = SensorManager.AXIS_X;
                break;
            case Surface.ROTATION_0:
            default:
                worldAxisX = SensorManager.AXIS_X;
                worldAxisY = SensorManager.AXIS_Z;
                break;
        }
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX,
                worldAxisY, adjustedRotationMatrix);

        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        double finalOrientation = ((float) Math.toDegrees(orientation[0]) + 360f) % 360f;
        intent.putExtra("orientation", finalOrientation);
        System.out.println(finalOrientation);
        sendBroadcast(intent);
    }
    /*public void onSensorChanged(SensorEvent sensorEvent) {
        // If the sensor data is unreliable return
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        // Gets the value of the sensor that has been changed
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = sensorEvent.values.clone();
                break;
        }

        // If gravity and geomagnetic have values then find rotation matrix
        if (gravity != null && geomagnetic != null) {
            // checks that the rotation matrix is found
            if (SensorManager.getRotationMatrix(inR, null, gravity, geomagnetic)) {

                float[] orientMatrix = new float[3];
                float[] remapMatrix = new float[9];

                SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, remapMatrix);
                SensorManager.getOrientation(remapMatrix, orientMatrix);

                double orientation = Math.toDegrees(orientMatrix[0]);

                intent.putExtra("orientation", orientation);
                sendBroadcast(intent);
            }
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
