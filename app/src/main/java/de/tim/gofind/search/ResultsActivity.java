package de.tim.gofind.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.tim.gofind.R;
import de.tim.gofind.ar.ARActivity;
import de.tim.gofind.databinding.ActivityResultsBinding;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback, Response.ErrorListener, GoogleMap.OnMarkerClickListener {

    private ListView listView;
    private double latitude;
    private double longitude;
    private GoogleMap mMap;
    private MapView mapView;
    private FloatingActionButton searchFab;
    private final HashMap<String, HistoricalImage> imageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        de.tim.gofind.databinding.ActivityResultsBinding binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        overridePendingTransition(0,0);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        QueryTerm queryTerm = new QueryTerm();
        queryTerm.setCategories(Collections.singletonList("spatialdistance"));
        queryTerm.setType(QueryTerm.TypeEnum.LOCATION);
        queryTerm.setData(String.format("[%s,%s]", latitude, longitude));

        QueryComponent queryComponent = new QueryComponent();
        queryComponent.setTerms(Collections.singletonList(queryTerm));

        SimilarityQuery similarityQuery = new SimilarityQuery();
        similarityQuery.setContainers(Collections.singletonList(queryComponent));

        listView = binding.resultList;

        mapView = binding.map;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        searchFab = binding.mapFab;
        searchFab.setOnClickListener(view -> handleSearchClick());

        Response.Listener<SimilarityQueryResultBatch> resultBatchListener = this::handleSimilarityQueryResult;

        SegmentsApi segmentsApi = new SegmentsApi();
        segmentsApi.findSegmentSimilar(similarityQuery, resultBatchListener, this);
    }

    private void handleSimilarityQueryResult(SimilarityQueryResultBatch response) {
        List<String> keyList = new ArrayList<>();
        for (SimilarityQueryResult result : response.getResults()) {
            for (StringDoublePair pair : result.getContent()) {
                keyList.add(pair.getKey());
            }
        }
        IdList idList = new IdList();
        idList.setIds(keyList);
        Response.Listener<MediaSegmentQueryResult> segmentQueryResultListener = this::handleMediaSegmentQueryResult;
        SegmentApi segmentApi = new SegmentApi();
        segmentApi.findSegmentByIdBatched(idList, segmentQueryResultListener,this);
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
            } else {
                HistoricalImage currentImage = imageMap.get(segmentDescriptor.getObjectId());
                assert currentImage != null;
                currentImage.setObjectID(segmentDescriptor.getObjectId());
                currentImage.setSegmentID(segmentDescriptor.getSegmentId());
            }
        }

        IdList objectIdList = new IdList();
        objectIdList.setIds(objectKeyList);

        Response.Listener<MediaObjectQueryResult> objectQueryResultListener = this::handleMediaObjectQueryResult;

        ObjectApi objectApi = new ObjectApi();
        objectApi.findObjectsByIdBatched(objectIdList, objectQueryResultListener,this);
    }

    private void handleMediaObjectQueryResult(MediaObjectQueryResult mediaObjectQueryResult) {
        List<String> objectList = new ArrayList<>();
        for (MediaObjectDescriptor objectDescriptor : mediaObjectQueryResult.getContent()) {
            objectList.add(objectDescriptor.getObjectId());

            String basePath = "http://10.34.58.145/thumbnails/%s/%s.png";

            if (!imageMap.containsKey(objectDescriptor.getObjectId())) {
                HistoricalImage newImage = new HistoricalImage();
                newImage.setTitle(objectDescriptor.getName());
                newImage.setPath(String.format(basePath, newImage.getObjectID(), newImage.getSegmentID()));
                imageMap.put(objectDescriptor.getObjectId(), newImage);
            } else {
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

        metadataApi.findMetadataForObjectIdBatched(objectIdList2, objectMetadataQueryResultListener,this);
    }

    private void handleMediaObjectMetadataQueryResult(MediaObjectMetadataQueryResult mediaObjectMetadataQueryResult) {
        for (MediaObjectMetadataDescriptor metadataDescriptor : mediaObjectMetadataQueryResult.getContent()) {
            if (!imageMap.containsKey(metadataDescriptor.getObjectId())) {
                HistoricalImage newImage = new HistoricalImage();
                switch (metadataDescriptor.getDomain()) {
                    case "LOCATION":
                        if (metadataDescriptor.getKey().equals("latitude")) {
                            newImage.setLatitude(Double.parseDouble(metadataDescriptor.getValue()));
                        } else if (metadataDescriptor.getKey().equals("longitude")) {
                            newImage.setLongitude(Double.parseDouble(metadataDescriptor.getValue()));
                        }
                        break;
                    case "JSON":
                        switch (metadataDescriptor.getKey()) {
                            case "Description":
                                newImage.setTitle(metadataDescriptor.getValue());
                                break;
                            case "Dating":
                                newImage.setDate(metadataDescriptor.getValue());
                                break;
                            case "Source":
                                newImage.setSource(metadataDescriptor.getValue());
                                break;
                        }
                        break;
                }
                imageMap.put(metadataDescriptor.getObjectId(), newImage);
            } else {
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
                        }
                        break;
                }
            }
        }
        List<HistoricalImage> imageList = new ArrayList<>(imageMap.values());
        SearchListAdapter adapter = new SearchListAdapter(getLayoutInflater(), imageList, latitude, longitude);
        listView.setAdapter(adapter);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.setOnInfoWindowClickListener(marker -> {

            Intent intent = new Intent(this, ARActivity.class);
            intent.putExtra("path", marker.getTitle());
            startActivity(intent);

        });

        for (HistoricalImage image : imageList) {

            LatLng latLng = new LatLng(image.getLatitude(), image.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(image.getTitle()).snippet(image.getPath()));
        }
    }

    private void handleSearchClick() {
        if (mapView.getVisibility() == View.VISIBLE) {
            mapView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            searchFab.setImageResource(R.drawable.ic_baseline_map_24);
        } else {
            mapView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            searchFab.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getLayoutInflater().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getLayoutInflater().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng basel = new LatLng(47.55963623772201, 7.588694683884673);

        mMap.setMyLocationEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(basel, 15));

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            Picasso.get().load(marker.getSnippet()).placeholder(R.drawable.ic_baseline_image_24).into(((ImageView) view.findViewById(R.id.badge)));

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }
        }
    }
}