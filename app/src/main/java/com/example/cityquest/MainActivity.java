package com.example.cityquest;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cityquest.model.LocationViewModel;
import com.example.cityquest.utils.LocationPermissionUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    LocationViewModel locationViewModel;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets gestureInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures());

            int defaultNavHeight = getResources().getDimensionPixelSize(R.dimen.bottom_nav_height);
            ViewGroup.LayoutParams layoutParams = bottomNavigationView.getLayoutParams();


            if (systemBarsInsets.bottom > 0 && gestureInsets.bottom > 0) {
                // 3-button navigation mode or gesture mode with bottom bar
                layoutParams.height = defaultNavHeight + systemBarsInsets.bottom;
            } else {
                // Gesture navigation without bottom bar
                layoutParams.height = defaultNavHeight;
            }

            bottomNavigationView.setLayoutParams(layoutParams);

            return insets;
        });

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        String selectedCity =  getIntent().getStringExtra("selected_city") != null ? getIntent().getStringExtra("selected_city") : "city not selected";


        // Request location permission
        LocationPermissionUtil.requestLocationPermission(this, new LocationPermissionUtil.LocationPermissionCallback() {
            @Override
            public void onPermissionGranted() {
                locationPermissionGranted = true;  // Update permission granted status
                getLastLocation();  // Once permission is granted, fetch the location
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(MainActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_explore:
                    selectedFragment = ExploreAroundFragment.newInstance(selectedCity);
                    break;
                case R.id.nav_itinerary:
                    selectedFragment = new TripsFragment();
                    break;
                case R.id.nav_community:
                    selectedFragment = new CommunityActivity();
                    break;
                case R.id.nav_saved:
                    selectedFragment = new SavedActivity();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Load the default fragment on startup
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_explore);
        }
    }







    private void getLastLocation() {
        if (locationPermissionGranted) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {


                                locationViewModel.setLocation(location);

                            } else {
                                Toast.makeText(MainActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Delegate permission handling to LocationPermissionUtil
        LocationPermissionUtil.handleRequestPermissionsResult(requestCode, grantResults, new LocationPermissionUtil.LocationPermissionCallback() {
            @Override
            public void onPermissionGranted() {
                locationPermissionGranted = true;  // Update permission granted status
                getLastLocation(); // If permission granted, get location
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(MainActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

}