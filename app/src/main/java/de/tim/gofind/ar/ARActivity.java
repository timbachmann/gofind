package de.tim.gofind.ar;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.MathHelper;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
    private boolean imageDrawn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        targetLat = intent.getDoubleExtra("lat",0);
        targetLon = intent.getDoubleExtra("lon",0);
        bearing = intent.getIntExtra("bearing",0);
        String fullPath = String.format("http://10.34.58.145/objects/%s", path);

        ImageView imageView = new ImageView(getApplicationContext());
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        orientationText = findViewById(R.id.orientation_text);
        targetText = findViewById(R.id.target_text);
        arrowView = findViewById(R.id.arrow_view);
        startLocationServiceAndBind();

        arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        Picasso.get().load(fullPath).into(imageView);

        ViewRenderable.builder()
                .setView(this, imageView)
                .build()
                .thenAccept(renderable -> testViewRenderable = renderable);


        /*arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (testViewRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    anchorNode.setRenderable(testViewRenderable);
                });*/
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
            Log.d(TAG, "onUpdate: mAnchorNode is null");
            Session session = arFragment.getArSceneView().getSession();

            /*float[] position = {0, 0, -1};
            float[] rotation = {0, 0, 0, 1};
            Anchor anchor = session.createAnchor(new Pose(position, rotation));*/

            if ((int)orientation >= (int) azimuthToTarget - 1 && (int) orientation <= (int) azimuthToTarget + 1) {
                arrowView.setImageResource(android.R.color.transparent);
                int distance = (int) Utils.haversineDistance(latitude, longitude, targetLat, targetLon);
                Vector3 cameraPos = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
                cameraPos.z = 0;
                Vector3 cameraForward = arFragment.getArSceneView().getScene().getCamera().getForward();
                Vector3 position = Vector3.add(cameraPos, cameraForward.scaled(distance));

                // Create an ARCore Anchor at the position.
                Pose pose = Pose.makeTranslation(position.x, position.y, position.z);
                Anchor anchor = arFragment.getArSceneView().getSession().createAnchor(pose);

                mAnchorNode = new AnchorNode(anchor);
                mAnchorNode.setParent(arFragment.getArSceneView().getScene());
                //mAnchorNode.setRenderable(testViewRenderable);

                TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                node.setRenderable(testViewRenderable);
                //node.setWorldScale(node.getWorldScale().scaled(10f));
                node.setParent(mAnchorNode);

                Quaternion localRotation = node.getLocalRotation();

                Quaternion rotationDeltaX = new Quaternion(Vector3.up(), (bearing + (int) orientation % 360));
                localRotation = Quaternion.multiply(localRotation, rotationDeltaX);

                node.setLocalRotation(localRotation);
                imageDrawn = true;

            } else if (!imageDrawn && (int) orientation < (int) azimuthToTarget - 1) {
                arrowView.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_right_24);
            } else if (!imageDrawn && (int) orientation > (int) azimuthToTarget + 1) {
                arrowView.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_left_24);
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiver);
            getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiverOrientation);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        binding = null;
    }

    private void startLocationServiceAndBind() {
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("LOCATION".equals(action)) {
                    latitude = intent.getDoubleExtra("Latitude", 0);
                    longitude = intent.getDoubleExtra("Longitude", 0);
                    System.out.println("Location changed");
                }
            }

        };

        IntentFilter filter = new IntentFilter("LOCATION");
        getLayoutInflater().getContext().registerReceiver(mBroadcastReceiver, filter);

        Intent orientationService = new Intent(this, OrientationService.class);
        startService(orientationService);

        mBroadcastReceiverOrientation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("ORIENTATION".equals(action)) {
                    orientation = intent.getDoubleExtra("orientation", 0);
                    if (orientation < 0) {
                        orientation = 360 - Math.abs(orientation);
                    }
                    orientationText.setText(String.valueOf((int) orientation));
                    if (longitude != 0 && latitude != 0) {
                        azimuthToTarget = Utils.azimuthToTarget(targetLat, targetLon, latitude, longitude);

                        targetText.setText(String.valueOf((int) azimuthToTarget));
                    }
                }
            }

        };

        IntentFilter filter2 = new IntentFilter("ORIENTATION");
        getLayoutInflater().getContext().registerReceiver(mBroadcastReceiverOrientation, filter2);
    }
}