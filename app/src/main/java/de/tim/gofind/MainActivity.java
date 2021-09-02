package de.tim.gofind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import de.tim.gofind.databinding.ActivityMainBinding;
import de.tim.gofind.search.ResultsActivity;
import de.tim.gofind.utils.LocationService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private double latitude;
    private double longitude;
    private BroadcastReceiver mBroadcastReceiver;
    private EditText latitudeEdit;
    private EditText longitudeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(0,0);

        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);

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
        Button currentLocation = binding.locationButton;
        currentLocation.setOnClickListener(view -> {
            if (latitude != 0 && longitude != 0) {
                latitudeEdit.setText(String.valueOf(latitude));
                longitudeEdit.setText(String.valueOf(longitude));
            }
        });

        FloatingActionButton searchFab = binding.searchFab;
        searchFab.setOnClickListener(view -> handleSearchClick());
    }

    private void handleSearchClick() {
        Intent resultsIntent = new Intent(this, ResultsActivity.class);
        resultsIntent.putExtra("latitude", latitude);
        resultsIntent.putExtra("longitude", longitude);
        startActivity(resultsIntent);
        overridePendingTransition(0,0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLayoutInflater().getContext().unregisterReceiver(mBroadcastReceiver);
        binding = null;
    }
}