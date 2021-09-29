package de.tim.gofind.ar;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.squareup.picasso.Picasso;

import de.tim.gofind.R;
import de.tim.gofind.databinding.ActivityArBinding;
import de.tim.gofind.utils.LocationService;
import de.tim.gofind.utils.OrientationService;
import de.tim.gofind.utils.Utils;

public class ARActivity extends AppCompatActivity implements Scene.OnUpdateListener {

    private ArFragment arFragment;
    private BroadcastReceiver mBroadcastReceiver;
    private BroadcastReceiver mBroadcastReceiverOrientation;
    private ActivityArBinding binding;
    private Renderable testViewRenderable;
    private AnchorNode mAnchorNode;
    private double orientation;
    private double latitude;
    private double longitude;
    private TextView orientationText;
    private TextView targetText;
    private double targetLat;
    private double targetLon;
    private int bearing;
    private double azimuthToTarget;
    private ImageView arrowView;
    private ImageView overlayView;
    private FloatingActionButton overlayFab;
    private Slider overlaySlider;
    private boolean imageDrawn = false;
    private boolean overlayViewEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ImageView arImageView = new ImageView(getApplicationContext());
        String imagePath = intent.getStringExtra("path");
        String fullPath = String.format("http://city-stories.dmi.unibas.ch:5555/objects/%s", imagePath);
        targetLat = intent.getDoubleExtra("lat", 0);
        targetLon = intent.getDoubleExtra("lon", 0);
        bearing = intent.getIntExtra("bearing", 0);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        orientationText = findViewById(R.id.orientation_text);
        targetText = findViewById(R.id.target_text);
        arrowView = findViewById(R.id.arrow_view);
        overlayView = findViewById(R.id.overlay_image);
        overlaySlider = findViewById(R.id.overlay_slider);
        overlayFab = findViewById(R.id.overlay_fab);
        startLocationServiceAndBind();

        arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        Picasso.get().load(fullPath).into(overlayView);
        Picasso.get().load(fullPath).into(arImageView);

        ViewRenderable.builder().setView(this, arImageView).build().thenAccept(renderable -> testViewRenderable = renderable);

        overlayView.setAlpha(0.5f);
        overlayFab.setOnClickListener(view -> onOverlayFabClick());
        overlaySlider.addOnChangeListener((slider, value, fromUser) -> overlayView.setAlpha(value));
    }

    private void onOverlayFabClick() {
        if (!overlayViewEnabled) {
            overlayView.setVisibility(View.VISIBLE);
            overlaySlider.setVisibility(View.VISIBLE);
            arrowView.setVisibility(View.INVISIBLE);
            targetText.setVisibility(View.INVISIBLE);
            orientationText.setVisibility(View.INVISIBLE);
            overlayFab.setImageResource(R.drawable.ic_baseline_center_focus_weak_24);
            arFragment.getPlaneDiscoveryController().hide();
            overlayViewEnabled = true;
        } else {
            overlayView.setVisibility(View.GONE);
            overlaySlider.setVisibility(View.GONE);
            arrowView.setVisibility(View.VISIBLE);
            targetText.setVisibility(View.VISIBLE);
            orientationText.setVisibility(View.VISIBLE);
            arFragment.getPlaneDiscoveryController().show();
            overlayFab.setImageResource(R.drawable.ic_baseline_image_24);
            overlayViewEnabled = false;
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        if (arFragment.getArSceneView().getArFrame() == null) {
            Log.d(TAG, "onUpdate: No frame available");
            return;
        }

        if (arFragment.getArSceneView().getArFrame().getCamera().getTrackingState() != TrackingState.TRACKING) {
            Log.d(TAG, "onUpdate: Tracking not started yet");
            // Tracking not started yet
            return;
        }

        if (this.mAnchorNode == null && testViewRenderable != null) {
            Session session = arFragment.getArSceneView().getSession();

            if (!overlayViewEnabled && (int) orientation == (int) azimuthToTarget) {
                arrowView.setImageResource(android.R.color.transparent);
                int distance = (int) Utils.haversineDistance(latitude, longitude, targetLat, targetLon);
                Vector3 cameraPos = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
                cameraPos.z = 0;
                Vector3 cameraForward = arFragment.getArSceneView().getScene().getCamera().getForward();
                Vector3 position = Vector3.add(cameraPos, cameraForward.scaled(distance));

                Pose pose = Pose.makeTranslation(position.x, position.y, position.z);

                assert session != null;
                Anchor anchor = session.createAnchor(pose);
                mAnchorNode = new AnchorNode(anchor);
                mAnchorNode.setParent(arFragment.getArSceneView().getScene());

                TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                node.setRenderable(testViewRenderable);
                node.setParent(mAnchorNode);

                Quaternion localRotation = node.getLocalRotation();

                Quaternion rotationDeltaX = new Quaternion(Vector3.up(), (bearing - (int) orientation % 360));
                localRotation = Quaternion.multiply(localRotation, rotationDeltaX);

                node.setLocalRotation(localRotation);
                imageDrawn = true;

            } else if (!overlayViewEnabled && !imageDrawn && (int) orientation < (int) azimuthToTarget) {
                arrowView.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_right_24);
            } else if (!overlayViewEnabled && !imageDrawn && (int) orientation > (int) azimuthToTarget) {
                arrowView.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_left_24);
            }
        }
    }


    @Override
    public void onDestroy() {
        try {
            getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiver);
            getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiverOrientation);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onDestroy();
        binding = null;
    }

    private void startLocationServiceAndBind() {
        //Location service
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);
        mBroadcastReceiver = new LocationOrientationReceiver();
        IntentFilter locationFilter = new IntentFilter(LocationService.BROADCAST_LOCATION);
        getLayoutInflater().getContext().registerReceiver(mBroadcastReceiver, locationFilter);

        //Orientation service
        Intent orientationService = new Intent(this, OrientationService.class);
        startService(orientationService);
        mBroadcastReceiverOrientation = new LocationOrientationReceiver();
        IntentFilter orientationFilter = new IntentFilter(OrientationService.BROADCAST_ORIENTATION);
        getLayoutInflater().getContext().registerReceiver(mBroadcastReceiverOrientation, orientationFilter);
    }

    private class LocationOrientationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(OrientationService.BROADCAST_ORIENTATION)) {
                orientation = intent.getDoubleExtra("orientation", 0);
                if (orientation < 0) {
                    orientation = 360 - Math.abs(orientation);
                }
                orientationText.setText(String.valueOf((int) orientation));
                if (longitude != 0 && latitude != 0) {
                    azimuthToTarget = Utils.calculateHeadingAngle(targetLat, targetLon, latitude, longitude);

                    targetText.setText(String.valueOf((int) azimuthToTarget));
                }
            } else if (action.equals(LocationService.BROADCAST_LOCATION)) {
                latitude = intent.getDoubleExtra("Latitude", 0);
                longitude = intent.getDoubleExtra("Longitude", 0);
            }
        }
    }
}