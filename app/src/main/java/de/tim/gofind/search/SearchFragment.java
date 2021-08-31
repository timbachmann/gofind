package de.tim.gofind.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.MetadataApi;
import org.openapitools.client.api.ObjectApi;
import org.openapitools.client.api.SegmentApi;
import org.openapitools.client.api.SegmentsApi;
import org.openapitools.client.model.IdList;
import org.openapitools.client.model.MediaObjectDescriptor;
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
import org.openapitools.client.request.PostRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.tim.gofind.R;
import de.tim.gofind.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment implements Response.Listener<SimilarityQueryResultBatch>, Response.ErrorListener {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private double latitude;
    private double longitude;
    private BroadcastReceiver mBroadcastReceiver;
    private EditText latitudeEdit;
    private EditText longitudeEdit;
    private TextView resultsView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        latitudeEdit = binding.latitude;
        longitudeEdit = binding.longitude;
        resultsView = binding.response;
        Button currentLocation = binding.locationButton;
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude != 0 && longitude != 0) {
                    latitudeEdit.setText(String.valueOf(latitude));
                    longitudeEdit.setText(String.valueOf(longitude));
                }
            }
        });

        FloatingActionButton searchFab = binding.searchFab;
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchClick();
            }
        });

        return root;
    }

    private void handleSearchClick() {
        QueryTerm queryTerm = new QueryTerm();
        queryTerm.setCategories(Collections.singletonList("spatialdistance"));
        queryTerm.setType(QueryTerm.TypeEnum.LOCATION);
        queryTerm.setData(String.format("[%s,%s]", latitudeEdit.getText().toString(), longitudeEdit.getText().toString()));

        QueryComponent queryComponent = new QueryComponent();
        queryComponent.setTerms(Collections.singletonList(queryTerm));

        SimilarityQuery similarityQuery = new SimilarityQuery();
        similarityQuery.setContainers(Collections.singletonList(queryComponent));


        SegmentsApi segmentsApi = new SegmentsApi();
        segmentsApi.findSegmentSimilar(similarityQuery, this, this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiver);
        binding = null;
    }


    @Override
    public void onResponse(SimilarityQueryResultBatch response) {
        List<String> keyList = new ArrayList<>();
        for (SimilarityQueryResult result : response.getResults()) {
            for (StringDoublePair pair : result.getContent()) {
                keyList.add(pair.getKey());
            }
        }

        IdList idList = new IdList();
        idList.setIds(keyList);

        SegmentApi segmentApi = new SegmentApi();
        segmentApi.findSegmentByIdBatched(idList, response1 -> {

            List<String> objectKeyList = new ArrayList<>();
            for (MediaSegmentDescriptor segmentDescriptor : response1.getContent()) {
                objectKeyList.add(segmentDescriptor.getObjectId());
            }

            IdList objectIdList = new IdList();
            objectIdList.setIds(objectKeyList);

            ObjectApi objectApi = new ObjectApi();
            objectApi.findObjectsByIdBatched(objectIdList, response2 -> {
                List<String> objectList = new ArrayList<>();
                for (MediaObjectDescriptor objectDescriptor : response2.getContent()) {
                    objectList.add(objectDescriptor.getObjectId());
                }

                OptionallyFilteredIdList objectIdList2 = new OptionallyFilteredIdList();
                objectIdList2.setIds(objectList);

                MetadataApi metadataApi = new MetadataApi();

                metadataApi.findMetadataForObjectIdBatched(objectIdList2, response3 -> {
                    resultsView.setText(response3.getContent().toString());
                }, error -> {

                });
            }, error -> {

            });
        }, error -> {

        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }
}