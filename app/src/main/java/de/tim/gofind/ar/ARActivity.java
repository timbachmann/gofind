package de.tim.gofind.ar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.LinkAddress;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.tim.gofind.R;
import de.tim.gofind.databinding.ActivityArBinding;
import de.tim.gofind.search.HistoricalImage;
import de.tim.gofind.utils.LocationService;
import de.tim.gofind.utils.OrientationService;
import de.tim.gofind.utils.Utils;

/**
 *
 */
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
    private String serializedHistoricalImage;
    private HistoricalImage historicalImage;
    private double azimuthToTarget;
    private ImageView arrowView;
    private ImageView overlayView;
    private FloatingActionButton overlayFab;
    private FloatingActionButton detailsFab;
    private Slider overlaySlider;
    private boolean imageDrawn = false;
    private boolean overlayViewEnabled = false;
    private String fullPath;
    private LinearLayout bottomSheet;
    private ConstraintLayout overlayLayout;
    private ArrayList<HistoricalImage> imageObjectList;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private final String BASE_PATH = "/objects/%s";

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ImageView arImageView = new ImageView(getApplicationContext());
        serializedHistoricalImage = intent.getStringExtra("image");
        historicalImage = new HistoricalImage(serializedHistoricalImage);
        targetLat = historicalImage.getLatitude();
        targetLon = historicalImage.getLongitude();

        SharedPreferences sharedPref = getSharedPreferences("GoFind", Context.MODE_PRIVATE);
        String path = sharedPref.getString(getString(R.string.shared_preferences_cineast_path), getResources().getString(R.string.shared_preferences_cineast_path));
        fullPath = String.format(path + BASE_PATH, historicalImage.getObjectID());

        imageObjectList = new ArrayList<>();
        overlayLayout = findViewById(R.id.overlay_layout);

        SharedPreferences sharedPrefs = getSharedPreferences("GoFind", Context.MODE_PRIVATE);
        Set<String> serializedImages = sharedPrefs.getStringSet(getString(R.string.shared_preferences_key), null);
        if (serializedImages != null) {
            int i = 9;
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setVisibility(View.GONE);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            overlayLayout.addView(linearLayout, 8, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            List<HistoricalImage> imageList = serializedImages.stream().map(HistoricalImage::new).collect(Collectors.toList());
            for (HistoricalImage image : imageList) {
                if (!image.getObjectID().equals(historicalImage.getObjectID())
                        && Utils.haversineDistance(image.getLatitude(),
                        image.getLongitude(), historicalImage.getLatitude(),
                        historicalImage.getLongitude()) < 30) {
                    imageObjectList.add(historicalImage);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setVisibility(View.GONE);
                    imageView.setAlpha(0.0f);
                    Picasso.get().load(String.format(path + BASE_PATH, image.getObjectID())).placeholder(R.drawable.ic_twotone_image_24).into(imageView);
                    overlayLayout.addView(imageView, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    Slider slider = new Slider(this);
                    slider.setVisibility(View.VISIBLE);
                    linearLayout.addView(slider);
                    slider.setValueFrom(0.0f);
                    slider.setValue(0.0f);
                    slider.setValueTo(1.0f);
                    slider.addOnChangeListener((lambdaSlider, value, fromUser) -> imageView.setAlpha(value));
                    i++;
                }
            }
        }

        bottomSheet = findViewById(R.id.bottom_details_container);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setVisibility(View.INVISIBLE);

        ImageView detailsImage = findViewById(R.id.details_icon);
        TextView detailsTitle = findViewById(R.id.details_title);
        TextView detailsDistance = findViewById(R.id.details_distance);
        TextView detailsDate = findViewById(R.id.details_date);
        TextView detailsSource = findViewById(R.id.details_source);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        orientationText = findViewById(R.id.orientation_text);
        targetText = findViewById(R.id.target_text);
        arrowView = findViewById(R.id.arrow_view);
        overlayView = findViewById(R.id.overlay_image);
        overlaySlider = findViewById(R.id.overlay_slider);
        overlayFab = findViewById(R.id.overlay_fab);
        detailsFab = findViewById(R.id.details_fab);
        Toolbar detailsBar = findViewById(R.id.details_toolbar);
        startLocationServiceAndBind();

        arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        Picasso.get().load(fullPath).placeholder(R.drawable.ic_twotone_image_24).into(overlayView);
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_twotone_image_24).into(arImageView);
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_twotone_image_24).into(detailsImage);

        detailsTitle.setText(historicalImage.getTitle());
        detailsDistance.setText(String.format("%sm", historicalImage.getDistance()));
        detailsDate.setText(historicalImage.getDate());
        detailsSource.setText(historicalImage.getSource());

        detailsBar.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheet.setVisibility(View.INVISIBLE);
        });

        ViewRenderable.builder().setView(this, arImageView).build().thenAccept(renderable -> testViewRenderable = renderable);


        overlayFab.setOnClickListener(view -> onOverlayFabClick());
        overlaySlider.addOnChangeListener((slider, value, fromUser) -> overlayView.setAlpha(value));

        detailsFab.setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.setVisibility(View.INVISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                bottomSheet.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

    }

    /**
     *
     */
    private void onOverlayFabClick() {
        if (!overlayViewEnabled) {
            overlayView.setVisibility(View.VISIBLE);
            overlaySlider.setVisibility(View.VISIBLE);
            arrowView.setVisibility(View.INVISIBLE);
            targetText.setVisibility(View.INVISIBLE);
            orientationText.setVisibility(View.INVISIBLE);
            overlayFab.setImageResource(R.drawable.ic_baseline_center_focus_weak_24);
            arFragment.getPlaneDiscoveryController().hide();
            for (int i = 8; i < 8 + imageObjectList.size() + 1; i++) {
                overlayLayout.getChildAt(i).setVisibility(View.VISIBLE);
            }
            overlayViewEnabled = true;
        } else {
            overlayView.setVisibility(View.GONE);
            overlaySlider.setVisibility(View.GONE);
            arrowView.setVisibility(View.VISIBLE);
            targetText.setVisibility(View.VISIBLE);
            orientationText.setVisibility(View.VISIBLE);
            arFragment.getPlaneDiscoveryController().show();
            overlayFab.setImageResource(R.drawable.ic_baseline_image_24);
            for (int i = 8; i < 8 + imageObjectList.size() + 1; i++) {
                overlayLayout.getChildAt(i).setVisibility(View.GONE);
            }
            overlayViewEnabled = false;
        }
    }

    /**
     * @param frameTime
     */
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
    private class LocationOrientationReceiver extends BroadcastReceiver {

        /**
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(OrientationService.BROADCAST_ORIENTATION)) {
                orientation = intent.getDoubleExtra("orientation", 0);
                if (orientation < 0) {
                    orientation = 360 - Math.abs(orientation);
                }
                orientationText.setText("Current: " + (int) orientation + "°");
                if (longitude != 0 && latitude != 0) {
                    azimuthToTarget = Utils.calculateHeadingAngle(targetLat, targetLon, latitude, longitude);
                    targetText.setText("Target: " + (int) azimuthToTarget + "°");
                }
            } else if (action.equals(LocationService.BROADCAST_LOCATION)) {
                latitude = intent.getDoubleExtra("Latitude", 0);
                longitude = intent.getDoubleExtra("Longitude", 0);
            }
        }
    }
}