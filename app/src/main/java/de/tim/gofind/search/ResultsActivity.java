package de.tim.gofind.search;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.openapitools.client.api.MetadataApi;
import org.openapitools.client.api.ObjectApi;
import org.openapitools.client.api.SegmentApi;
import org.openapitools.client.api.SegmentsApi;
import org.openapitools.client.model.IdList;
import org.openapitools.client.model.MediaObjectDescriptor;
import org.openapitools.client.model.MediaObjectMetadataDescriptor;
import org.openapitools.client.model.MediaObjectMetadataQueryResult;
import org.openapitools.client.model.MediaObjectQueryResult;
import org.openapitools.client.model.MediaSegmentDescriptor;
import org.openapitools.client.model.MediaSegmentQueryResult;
import org.openapitools.client.model.OptionallyFilteredIdList;
import org.openapitools.client.model.QueryComponent;
import org.openapitools.client.model.QueryTerm;
import org.openapitools.client.model.SimilarityQuery;
import org.openapitools.client.model.SimilarityQueryResult;
import org.openapitools.client.model.SimilarityQueryResultBatch;
import org.openapitools.client.model.StringDoublePair;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.tim.gofind.R;
import de.tim.gofind.ar.ARActivity;
import de.tim.gofind.databinding.ActivityResultsBinding;
import de.tim.gofind.utils.CustomInfoWindowAdapter;
import de.tim.gofind.utils.LocationService;
import de.tim.gofind.utils.Utils;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback, Response.ErrorListener {

    private final int VIEW_DISTANCE = 1000;
    private final HashMap<String, HistoricalImage> imageMap = new HashMap<>();
    private double latitude;
    private double longitude;
    private ListView listView;
    private GoogleMap mMap = null;
    private MapView mapView;
    private LinearLayout bottomSheet;
    private ActivityResultsBinding binding;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private BroadcastReceiver mBroadcastReceiver;
    private EditText latitudeEdit;
    private EditText longitudeEdit;
    private MenuItem listMenuItem;
    private MaterialToolbar toolbar;
    private androidx.appcompat.widget.Toolbar filterBar;
    private ImageButton currentLocation;
    private ConstraintLayout searchButton;
    private RangeSlider rangeSlider;
    private TextView minTextField;
    private TextView maxTextField;
    private Circle locationCircle;
    private CheckBox spacialCheckBox;
    private CheckBox imageCheckBox;
    private CheckBox temporalCheckBox;
    private LinearLayout spacialLayout;
    private LinearLayout temporalLayout;
    private LinearLayout imageLayout;
    private Marker locationPicker;
    private GroundOverlay mapOverlay;
    private FloatingActionButton layersFab;
    private FloatingActionButton locationFab;
    private FloatingActionButton cancelFab;
    private ImageView locationButton;
    private Slider mapOverlaySlider;
    private ImageButton imageButton;
    private ImageButton cameraButton;
    private ImageView searchBitmapView;
    private Bitmap searchBitmap;
    private Uri image_uri;
    private boolean temporalQuery = false;
    private boolean spacialQuery = false;
    private boolean exampleQuery = false;
    private MenuItem backgroundLocationPermission;
    private static final int RESULT_LOAD_IMAGE = 123;
    public static final int IMAGE_CAPTURE_CODE = 654;
    private ActivityResultLauncher<String> permissionActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultContracts.RequestPermission requestPermissionsContract = new ActivityResultContracts.RequestPermission();
        permissionActivityResultLauncher = registerForActivityResult(requestPermissionsContract, isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (!isGranted) {
                Log.d("PERMISSIONS", "Permission ACCESS_BACKGROUND_LOCATION was not granted, launching again...");
                permissionActivityResultLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            }
            backgroundLocationPermission.setChecked(isGranted);
        });

        setupNotificationChannel();
        initializeViews(savedInstanceState);
        setSupportActionBar(toolbar);
        setFlags();
        overridePendingTransition(0, 0);
        setUpListeners();
        startLocationServiceAndBind();
    }

    private void setupNotificationChannel() {
        CharSequence name = getString(R.string.app_name);
        String description = getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("CHANNEL_GO_FIND", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void setFlags() {
        if (getResources().getBoolean(R.bool.night_mode_on)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initializeViews(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        bottomSheet = findViewById(R.id.bottom_navigation_container);
        filterBar = findViewById(R.id.filter_toolbar);
        latitudeEdit = findViewById(R.id.latitude);
        longitudeEdit = findViewById(R.id.longitude);
        currentLocation = findViewById(R.id.location_button);
        searchButton = findViewById(R.id.search_fab);
        rangeSlider = findViewById(R.id.seekBar);
        minTextField = findViewById(R.id.min_text);
        maxTextField = findViewById(R.id.max_text);
        listView = binding.resultList;
        mapView = binding.map;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setVisibility(View.INVISIBLE);
        spacialCheckBox = findViewById(R.id.spacial_check_box);
        temporalCheckBox = findViewById(R.id.temporal_check_box);
        imageCheckBox = findViewById(R.id.image_check_box);
        spacialLayout = findViewById(R.id.location_layout);
        temporalLayout = findViewById(R.id.temporal_layout);
        imageLayout = findViewById(R.id.image_layout);
        layersFab = findViewById(R.id.layers_fab);
        locationFab = findViewById(R.id.location_fab);
        cancelFab = findViewById(R.id.cancel_fab);
        mapOverlaySlider = findViewById(R.id.map_overlay_slider);
        imageButton = findViewById(R.id.image_button);
        cameraButton = findViewById(R.id.camera_button);
        searchBitmapView = findViewById(R.id.search_image_thumbnail);

        MaterialShapeDrawable materialShapeDrawable = (MaterialShapeDrawable) toolbar.getBackground();
        materialShapeDrawable.setShapeAppearanceModel(materialShapeDrawable.getShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 104)
                .build());
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK){
            searchBitmap = uriToBitmap(image_uri);
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            image_uri = data.getData();
            searchBitmap = uriToBitmap(image_uri);
        }

        searchBitmapView.setRotation(90);
        searchBitmapView.setImageBitmap(searchBitmap);
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private void setUpListeners() {

        imageButton.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        });

        cameraButton.setOnClickListener(view -> openCamera());

        mapOverlaySlider.addOnChangeListener((slider, value, fromUser) -> mapOverlay.setTransparency(1-value));

        cancelFab.setOnClickListener(view -> {
            if (mapOverlay != null) {
                mapOverlay.remove();
                cancelFab.setVisibility(View.INVISIBLE);
                mapOverlaySlider.setVisibility(View.GONE);
            }
        });

        locationFab.setOnClickListener(view -> {
            if(mMap != null) {
                if(locationButton != null)
                    locationButton.callOnClick();
            }
        });

        layersFab.setOnClickListener(view -> {
            AlertDialog materialAlertDialog =
                    new MaterialAlertDialogBuilder(ResultsActivity.this)
                            .setTitle("Historical Maps")
                            .setItems(HistoricalMaps.getMapNameList(), (dialogInterface, i) -> {
                                if (mMap != null) {
                                    if (mapOverlay != null) mapOverlay.remove();
                                    HistoricalMap currentMap = HistoricalMaps.getMapList().get(i);
                                    LatLng newarkLatLng = new LatLng(47.55751690300252, 7.589010670781135);

                                    GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();

                                    Picasso.get().load(currentMap.getPath()).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                            groundOverlayOptions.bearing(220.0f);
                                            groundOverlayOptions.position(newarkLatLng, bitmap.getWidth() * 1.5f, bitmap.getHeight() * 1.5f);
                                            groundOverlayOptions.image(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            mapOverlay = mMap.addGroundOverlay(groundOverlayOptions);
                                            assert mapOverlay != null;
                                            mapOverlay.setTransparency(1-mapOverlaySlider.getValue());
                                            cancelFab.setVisibility(View.VISIBLE);
                                            mapOverlaySlider.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    });
                                }
                            }).show();
        });

        spacialCheckBox.setOnCheckedChangeListener((compoundButton, enabled) -> {
            spacialLayout.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
            if (locationPicker != null && !enabled) locationPicker.remove();
            spacialQuery = enabled;
        });

        temporalCheckBox.setOnCheckedChangeListener((compoundButton, enabled) -> {
            temporalLayout.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
            temporalQuery = enabled;
        });

        imageCheckBox.setOnCheckedChangeListener((compoundButton, enabled) -> {
            imageLayout.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
            exampleQuery = enabled;
        });

        filterBar.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheet.setVisibility(View.INVISIBLE);
        });

        currentLocation.setOnClickListener(view -> {
            if (!LocationService.isServiceRunning) {
                startLocationServiceAndBind();
            }
            if (latitude != 0 && longitude != 0) {
                latitudeEdit.setText(String.valueOf(latitude));
                longitudeEdit.setText(String.valueOf(longitude));
            }
        });

        searchButton.setOnClickListener(view -> handleSearchClick());

        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            minTextField.setText(String.valueOf(values.get(0).intValue()));
            maxTextField.setText(String.valueOf(values.get(1).intValue()));

        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setFlags();
        mapView.getMapAsync(this);
    }

    private void startLocationServiceAndBind() {
        Intent locationService = new Intent(this, LocationService.class);
        locationService.setAction("START");
        startForegroundService(locationService);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("LOCATION".equals(action)) {
                    latitude = intent.getDoubleExtra("Latitude", 0);
                    longitude = intent.getDoubleExtra("Longitude", 0);
                }
            }

        };

        IntentFilter filter = new IntentFilter("LOCATION");
        getLayoutInflater().getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    private void drawCircle(LatLng point) {
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(VIEW_DISTANCE);
        // Border color of the circle
        circleOptions.strokeColor(Color.BLUE);
        // Fill color of the circle
        circleOptions.fillColor(Color.TRANSPARENT);
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        locationCircle = mMap.addCircle(circleOptions);
    }

    private void handleSearchClick() {
        imageMap.clear();
        if (locationCircle != null) locationCircle.remove();
        LocationService.getInstance().getNotificationIds().clear();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setVisibility(View.INVISIBLE);

        List<QueryTerm> queryTerms = new ArrayList<>();

        if (spacialQuery && latitudeEdit.getText().length() > 0 && longitudeEdit.getText().length() > 0) {
            QueryTerm queryTerm = new QueryTerm();
            queryTerm.setCategories(Collections.singletonList("spatialdistance"));
            queryTerm.setType(QueryTerm.TypeEnum.LOCATION);
            queryTerm.setData(String.format("[%s,%s]", latitudeEdit.getText(), longitudeEdit.getText()));
            queryTerms.add(queryTerm);
        }

        if (temporalQuery) {
            String minText = minTextField.getText().toString();
            String maxText = maxTextField.getText().toString();
            QueryTerm queryTerm = new QueryTerm();
            queryTerm.setCategories(Collections.singletonList("temporaldistance"));
            queryTerm.setType(QueryTerm.TypeEnum.TIME);
            queryTerm.setData(String.format("%s-01-01T12:00:00Z", Integer.parseInt(minText) + ((Integer.parseInt(maxText) - Integer.parseInt(minText)) / 2)));
            queryTerms.add(queryTerm);
        }

        if (exampleQuery && searchBitmap != null) {
            QueryTerm queryTerm = new QueryTerm();
            queryTerm.setCategories(Collections.singletonList("distance"));
            queryTerm.setType(QueryTerm.TypeEnum.IMAGE);
            queryTerm.setData(Utils.toBase64(searchBitmap));
            queryTerms.add(queryTerm);
        }

        QueryComponent queryComponent = new QueryComponent();
        queryComponent.setTerms(queryTerms);

        SimilarityQuery similarityQuery = new SimilarityQuery();
        similarityQuery.setContainers(Collections.singletonList(queryComponent));

        Response.Listener<SimilarityQueryResultBatch> resultBatchListener = this::handleSimilarityQueryResult;

        SegmentsApi segmentsApi = new SegmentsApi();
        segmentsApi.findSegmentSimilar(similarityQuery, resultBatchListener, this);

    }

    private void handleSimilarityQueryResult(SimilarityQueryResultBatch response) {
        mMap.clear();
        listMenuItem.setVisible(true);
        List<String> keyList = new ArrayList<>();
        for (SimilarityQueryResult result : response.getResults()) {
            for (StringDoublePair pair : result.getContent()) {
                keyList.add(pair.getKey());
            }
        }

        if (response.getResults().size() > 1) {
            keyList = keyList.stream().collect(Collectors.groupingBy(Function.identity()))
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        IdList idList = new IdList();
        idList.setIds(keyList);
        Response.Listener<MediaSegmentQueryResult> segmentQueryResultListener = this::handleMediaSegmentQueryResult;
        SegmentApi segmentApi = new SegmentApi();
        segmentApi.findSegmentByIdBatched(idList, segmentQueryResultListener, this);
    }

    private void handleMediaSegmentQueryResult(MediaSegmentQueryResult mediaSegmentQueryResult) {
        List<String> objectKeyList = new ArrayList<>();
        for (MediaSegmentDescriptor segmentDescriptor : mediaSegmentQueryResult.getContent()) {
            objectKeyList.add(segmentDescriptor.getObjectId());

            if (!imageMap.containsKey(segmentDescriptor.getObjectId())) {
                HistoricalImage newImage = new HistoricalImage();
                newImage.setObjectID(segmentDescriptor.getObjectId());
                newImage.setSegmentID(segmentDescriptor.getSegmentId());
                imageMap.put(segmentDescriptor.getObjectId(), newImage);
            }
        }

        IdList objectIdList = new IdList();
        objectIdList.setIds(objectKeyList);

        Response.Listener<MediaObjectQueryResult> objectQueryResultListener = this::handleMediaObjectQueryResult;

        ObjectApi objectApi = new ObjectApi();
        objectApi.findObjectsByIdBatched(objectIdList, objectQueryResultListener, this);
    }

    private void handleMediaObjectQueryResult(MediaObjectQueryResult mediaObjectQueryResult) {
        List<String> objectList = new ArrayList<>();
        for (MediaObjectDescriptor objectDescriptor : mediaObjectQueryResult.getContent()) {
            objectList.add(objectDescriptor.getObjectId());

            String basePath = "http://10.34.58.145/thumbnails/%s/%s.png";

            if (imageMap.containsKey(objectDescriptor.getObjectId())) {
                HistoricalImage currentImage = imageMap.get(objectDescriptor.getObjectId());
                assert currentImage != null;
                currentImage.setTitle(objectDescriptor.getName());
                currentImage.setPath(String.format(basePath, currentImage.getObjectID(), currentImage.getSegmentID()));
            }
        }

        OptionallyFilteredIdList objectIdList2 = new OptionallyFilteredIdList();
        objectIdList2.setIds(objectList);

        MetadataApi metadataApi = new MetadataApi();

        Response.Listener<MediaObjectMetadataQueryResult> objectMetadataQueryResultListener = this::handleMediaObjectMetadataQueryResult;

        metadataApi.findMetadataForObjectIdBatched(objectIdList2, objectMetadataQueryResultListener, this);
    }

    private void handleMediaObjectMetadataQueryResult(MediaObjectMetadataQueryResult mediaObjectMetadataQueryResult) {
        for (MediaObjectMetadataDescriptor metadataDescriptor : mediaObjectMetadataQueryResult.getContent()) {
            if (imageMap.containsKey(metadataDescriptor.getObjectId())) {
                HistoricalImage currentImage = imageMap.get(metadataDescriptor.getObjectId());
                switch (metadataDescriptor.getDomain()) {
                    case "LOCATION":
                        if (metadataDescriptor.getKey().equals("latitude")) {
                            assert currentImage != null;
                            currentImage.setLatitude(Double.parseDouble(metadataDescriptor.getValue()));
                        } else if (metadataDescriptor.getKey().equals("longitude")) {
                            assert currentImage != null;
                            currentImage.setLongitude(Double.parseDouble(metadataDescriptor.getValue()));
                        }
                        break;
                    case "JSON":
                        switch (metadataDescriptor.getKey()) {
                            case "Description":
                                //currentImage.setTitle(metadataDescriptor.getValue());
                                break;
                            case "Dating":
                                assert currentImage != null;
                                currentImage.setDate(metadataDescriptor.getValue());
                                break;
                            case "Source":
                                assert currentImage != null;
                                currentImage.setSource(metadataDescriptor.getValue());
                                break;
                            case "bearing":
                                assert currentImage != null;
                                currentImage.setBearing(Integer.parseInt(metadataDescriptor.getValue()));
                                break;

                        }
                        break;
                }
            }
        }
        ArrayList<HistoricalImage> toOrder = new ArrayList<>(imageMap.values());
        for (HistoricalImage historicalImage : toOrder) {
            historicalImage.setDistance((int) Utils.haversineDistance(latitude, longitude, historicalImage.getLatitude(), historicalImage.getLongitude()));
        }
        DataStorage.getInstance().setImageList(toOrder.stream().sorted(Comparator.comparing(HistoricalImage::getDistance)).collect(Collectors.toList()));
        SearchListAdapter adapter = new SearchListAdapter(getLayoutInflater(), DataStorage.getInstance().getImageList());
        listView.setAdapter(adapter);

        displayMarkersOnMap();
    }

    private void displayMarkersOnMap() {
        if (mMap != null) {
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater()));
            mMap.setOnInfoWindowClickListener(marker -> {

                if ((int) Utils.haversineDistance(latitude, longitude,
                        marker.getPosition().latitude, marker.getPosition().longitude)
                        < getResources().getInteger(R.integer.ar_distance)) {
                    Intent intent = new Intent(this, ARActivity.class);
                    intent.putExtra("path", marker.getTitle());
                    intent.putExtra("lat", marker.getPosition().latitude);
                    intent.putExtra("lon", marker.getPosition().longitude);
                    intent.putExtra("bearing", (int) marker.getTag());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Too far away! Get closer!", Toast.LENGTH_LONG).show();
                }

            });

            if (DataStorage.getInstance().getImageList() != null) {
                for (HistoricalImage image : DataStorage.getInstance().getImageList()) {
                    LatLng latLng = new LatLng(image.getLatitude(), image.getLongitude());
                    if (!(spacialQuery && ((int) Utils.haversineDistance(
                            Double.parseDouble(latitudeEdit.getText().toString()),
                            Double.parseDouble(longitudeEdit.getText().toString()),
                            image.getLatitude(), image.getLongitude())) > VIEW_DISTANCE)) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(image.getTitle()).snippet(image.getPath()));
                        assert marker != null;
                        marker.setTag(image.getBearing());
                        image.setMarker(marker);
                    }
                }
                if (spacialQuery) {
                    drawCircle(new LatLng(Double.parseDouble(latitudeEdit.getText().toString()),
                            Double.parseDouble(longitudeEdit.getText().toString())));
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (getResources().getBoolean(R.bool.night_mode_on)) {
            System.out.println("Test");
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_dark));
        } else {
            System.out.println("Day");
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.maps_style));
        }


        mMap.setPadding(0, 400, 16, 0);

        if (ActivityCompat.checkSelfPermission(getLayoutInflater().getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getLayoutInflater().getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        LatLng basel = new LatLng(47.55963623772201, 7.588694683884673);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(basel, 15));

        mMap.setOnMapClickListener(latLng -> {
            if (locationPicker != null) {
                locationPicker.remove();
            }
            locationPicker = mMap.addMarker(new MarkerOptions().position(latLng));
            bottomSheet.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            latitudeEdit.setText(String.valueOf(latLng.latitude));
            longitudeEdit.setText(String.valueOf(latLng.longitude));
            spacialCheckBox.setChecked(true);
        });

        locationButton = mapView.findViewById(0x2);
        if(locationButton != null)
            locationButton.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getColor(R.color.textColorPrimary)));
        listMenuItem = menu.getItem(1);
        menu.getItem(1).setIconTintList(ColorStateList.valueOf(getColor(R.color.textColorPrimary)));
        menu.getItem(2).setIconTintList(ColorStateList.valueOf(getColor(R.color.textColorPrimary)));
        Objects.requireNonNull(toolbar.getOverflowIcon()).setTintList(ColorStateList.valueOf(getColor(R.color.textColorPrimary)));
        backgroundLocationPermission = menu.getItem(2);
        backgroundLocationPermission.setChecked(hasBackgroundLocationPermission());
        return true;
    }

    private void askBackgroundLocationPermission() {
        if (!hasBackgroundLocationPermission()) {
            Log.d("PERMISSIONS", "Launching contract permission launcher for ACCESS_BACKGROUND_LOCATION");
            permissionActivityResultLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            Log.d("PERMISSIONS", "Permission is already granted");
        }
    }

    private boolean hasBackgroundLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSIONS", "Permission is not granted: " + Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            return false;
        }
        Log.d("PERMISSIONS", "Permission already granted: " + Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.setVisibility(View.INVISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheet.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                return true;
            case R.id.list_display:
                if (mapView.getVisibility() == View.VISIBLE) {
                    mapView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_baseline_map_24);
                } else {
                    mapView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    item.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);
                }
                return true;
            case R.id.background_location_permission:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    askBackgroundLocationPermission();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}