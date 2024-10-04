package com.example.cityquest;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.ChangeCityLocationAdapter;
import com.example.cityquest.adapter.TrendingCityAdapter;
import com.example.cityquest.model.LocationViewModel;
import com.example.cityquest.utils.LocationPermissionUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChangeLocationCityActivity extends AppCompatActivity implements ChangeCityLocationAdapter.OnCityClickListener{

    Toolbar toolbar;
    private RecyclerView CitiesRecyclerView;
    private LinearLayout SetCurrentCityLoation;
    private TextView currentCityTV;
    private boolean locationPermissionGranted = false;

    private LocationViewModel locationViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private List<String> cityList = new ArrayList<>();

    private ChangeCityLocationAdapter cityAdapter; //TODO fetch cities of user location and show in recycler view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_location_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String apiKey = getString(R.string.google_maps_api_key);
        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey); // Replace with your API key
        }

        CitiesRecyclerView = findViewById(R.id.recycler_view_city_changeLocation);
        cityAdapter = new ChangeCityLocationAdapter(this, cityList,this);
        CitiesRecyclerView.setAdapter(cityAdapter);
        CitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        toolbar = findViewById(R.id.toolbar_changCity);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SetCurrentCityLoation = findViewById(R.id.set_City_current_location);
        currentCityTV = findViewById(R.id.current_City_tv);
        SetCurrentCityLoation.setOnClickListener(v -> {
            LocationPermissionUtil.requestLocationPermission(this, new LocationPermissionUtil.LocationPermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    locationPermissionGranted = true;  // Update permission granted status
                    getLastLocation();  // Once permission is granted, fetch the location
                }

                @Override
                public void onPermissionDenied() {

                    Toast.makeText(ChangeLocationCityActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            });
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow_icon); // Replace with your arrow drawable
        }


    }

    // Implement the click handling method
    @Override
    public void onCityClick(String cityName) {
        // Handle the click event
        Toast.makeText(this, "Selected city: " + cityName, Toast.LENGTH_SHORT).show();

        // You can also navigate to another activity or perform any other action
        Intent intent = new Intent(ChangeLocationCityActivity.this, MainActivity.class);
        intent.putExtra("selected_city", cityName); // Pass the selected city name
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity when the back arrow is clicked
        return true;
    }

    private void getLastLocation() {
        if (locationPermissionGranted) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                locationViewModel.setLocation(location);
                                getCitiesOfUserCountry(location.getLatitude(), location.getLongitude());
                                String cityName = getCityName(location.getLatitude(), location.getLongitude());
                                if(cityName!=null){
                                    currentCityTV.setText(cityName);
                                    SetCurrentCityLoation.setOnClickListener(v -> {
                                        Log.e("citiesnames",cityList.toString());
                                        Intent intent = new Intent(ChangeLocationCityActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        finish();

                                    });

                                }
                                Toast.makeText(ChangeLocationCityActivity.this, "Location fetched", Toast.LENGTH_SHORT).show();

                            } else {
                                getPopularCities();

                                Toast.makeText(ChangeLocationCityActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            getPopularCities();
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
                Toast.makeText(ChangeLocationCityActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(ChangeLocationCityActivity.this, Locale.getDefault());
        String cityName = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality(); // Get the city name

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }
    private void getCitiesOfUserCountry(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String countryName = addresses.get(0).getCountryCode();

                Log.d("Location", "User is in country: " + countryName);

                // Now, retrieve cities of the user’s country using Google Places API
                retrieveCitiesByCountry(countryName);
            } else {
                // If unable to get country name, fallback to popular cities
                getPopularCities();
            }
        } catch (IOException e) {
            e.printStackTrace();
            getPopularCities();
        }
    }

    private void retrieveCitiesByCountry(String country) {
        // Create bounds for the search, for example, around a central point in the country.
//        LatLngBounds bounds = new LatLngBounds(
//                new LatLng(/* country's southwest coordinates */),
//                new LatLng(/* country's northeast coordinates */)
//        );

        // Specify a list of types to include in the search (like locality)
        List<String> placeTypes = Arrays.asList("locality");

        // Create and execute the Nearby Search request
        placesClient = Places.createClient(this);
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery("city")
                .setCountries(country)
                .setTypesFilter(placeTypes)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            cityList.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                String cityName = prediction.getPrimaryText(null).toString();
                cityList.add(cityName);  // Add the city to the list
            }
            Log.e("citiesnames",cityList.toString());
            cityAdapter.notifyDataSetChanged();

            // Now you have the cities in cityList
        }).addOnFailureListener(e -> {
            Log.e("Error", "Error fetching cities", e);
            getPopularCities(); // If fetching cities fails, fallback to popular cities
        });
    }

    private void getPopularCities() {
        // Create a list of predefined popular cities
        List<String> popularCities = Arrays.asList("New York", "Los Angeles", "London", "Paris", "Tokyo");
        cityList.addAll(popularCities);

        // Now cityList contains popular cities
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up and release resources if necessary
        if (placesClient != null) {
            placesClient = null; // Setting to null for garbage collection
        }
    }




}