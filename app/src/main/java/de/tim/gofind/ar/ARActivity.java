package de.tim.gofind.ar;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.squareup.picasso.Picasso;

import de.tim.gofind.R;
import de.tim.gofind.databinding.ActivityArBinding;

public class ARActivity extends AppCompatActivity /*implements Scene.OnUpdateListener*/ {

    private ArFragment arFragment;
    private BroadcastReceiver mBroadcastReceiver;
    private ActivityArBinding binding;
    private Renderable testViewRenderable;
    private AnchorNode mAnchorNode;
    private LatLng currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String fullPath = String.format("http://10.34.58.145/objects/%s", path);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("LOCATION".equals(action)) {
                    currentPosition = new LatLng(
                            intent.getDoubleExtra("Latitude", 0),
                            intent.getDoubleExtra("Longitude", 0));
                }
            }

        };

        ImageView imageView = new ImageView(getBaseContext());
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        Picasso.get().load(fullPath).into(imageView);

        ViewRenderable.builder()
                .setView(this, imageView)
                .build()
                .thenAccept(renderable -> testViewRenderable = renderable);


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (testViewRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    anchorNode.setRenderable(testViewRenderable);
                });
    }

    /*@Override
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

            *//*float[] position = {0, 0, -1};
            float[] rotation = {0, 0, 0, 1};
            Anchor anchor = session.createAnchor(new Pose(position, rotation));*//*

            Vector3 cameraPos = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
            cameraPos.z = 0;
            Vector3 cameraForward = arFragment.getArSceneView().getScene().getCamera().();
            Vector3 position = Vector3.add(cameraPos, cameraForward.scaled(4.0f));

            // Create an ARCore Anchor at the position.
            Pose pose = Pose.makeTranslation(position.x, position.y, position.z);
            Anchor anchor = arFragment.getArSceneView().getSession().createAnchor(pose);

            mAnchorNode = new AnchorNode(anchor);
            mAnchorNode.setParent(arFragment.getArSceneView().getScene());
            mAnchorNode.setRenderable(testViewRenderable);

            *//*Node node = new Node();
            node.setRenderable(testViewRenderable);
            node.setParent(mAnchorNode);
            node.setOnTapListener((hitTestResult, motionEvent) -> showBottomSheet());*//*
        }
    }

    private void showBottomSheet() {
    }
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiver);
        binding = null;
    }
}