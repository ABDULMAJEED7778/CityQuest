package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.CityAdapter;
import com.example.cityquest.adapter.CityArrayAdapter;
import com.example.cityquest.adapter.FilterAdapter;
import com.example.cityquest.adapter.*;
import com.example.cityquest.adapter.PopularCityAdapter;
import com.example.cityquest.apiCalls.VolleyCallback;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Filter;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.apiCalls.NetworkUtils;
import com.example.cityquest.utils.FirestoreTripUploader;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private Button skipBtn, backBtn, logOutBtn,mapBtn;
    private AutoCompleteTextView searchET;

    private TextView usernameTV;
    private RecyclerView recyclerView;

    private PlacesClient placesClient;

    private PlaceAutocompleteAdapter suggestionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + 24, systemBars.top + 30, systemBars.right, systemBars.bottom + 30);
            return insets;
        });
//
//        FirestoreTripUploader tripUploader = new FirestoreTripUploader();
//        tripUploader.generateAndStoreTrips();

        String apiKey = getString(R.string.google_maps_api_key);
        Places.initializeWithNewPlacesApiEnabled(this, apiKey);
        placesClient = Places.createClient(this);

        // Initialize RecyclerView for showing autocomplete results
        recyclerView = findViewById(R.id.recyclerView_search_suggestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Autocomplete Adapter
        suggestionAdapter = new PlaceAutocompleteAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(suggestionAdapter);

        suggestionAdapter.setOnItemClickListener(position -> {
            AutocompletePrediction selectedPrediction = suggestionAdapter.getItemAt(position);
            if (selectedPrediction != null) {
                String placeId = selectedPrediction.getPlaceId();
                String cityName = selectedPrediction.getPrimaryText(null).toString();
                Log.e("placeId",placeId);
                DestinationDetailsBottomSheet bottomSheet = DestinationDetailsBottomSheet.newInstance(placeId,cityName);
                bottomSheet.show(getSupportFragmentManager(), "DestinationDetailsBottomSheet");

//                fetchPlaceDetails(placeId);
            }
        });


        searchET = findViewById(R.id.search_input);
        searchET.setContentDescription("Search for a city or place");

        mapBtn = findViewById(R.id.mapBtn_dest);
        backBtn = findViewById(R.id.backBtn_dest);
        skipBtn = findViewById(R.id.skipBtn_dest);
        logOutBtn = findViewById(R.id.logoutBtn_dest);
        usernameTV = findViewById(R.id.userName_Destination);





        mapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, MapActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        skipBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, TripsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });



        logOutBtn.setOnClickListener(v -> {
            FirebaseUtils.signOut();
            Intent intent = new Intent(DestinationActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            setUserInfo(userId);
        }

        // Initialize RecyclerViews
        RecyclerView recyclerViewCityCategory = findViewById(R.id.recyclerView_city_category);
        RecyclerView recyclerViewCityPopular = findViewById(R.id.recyclerView_city_popular);

        recyclerViewCityCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCityPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<City> cities = new ArrayList<>();
        cities.add(new City("Bangalore", R.drawable.bangalore, "India", "4.8"));
        cities.add(new City("Delhi", R.drawable.bangalore, "India", "4.5"));
        cities.add(new City("Mumbai", R.drawable.bangalore, "India", "4.7"));
        cities.add(new City("Chennai", R.drawable.bangalore, "India", "4.6"));

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("Historical", R.drawable.historical_icon));
        filters.add(new Filter("Cultural", R.drawable.historical_icon));
        filters.add(new Filter("Modern", R.drawable.historical_icon));

        RecyclerView recyclerViewFilters = findViewById(R.id.recyclerViewFilters);
        recyclerViewFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FilterAdapter adapter3 = new FilterAdapter(filters, this);
        recyclerViewFilters.setAdapter(adapter3);

        CityAdapter adapter = new CityAdapter(cities);
        PopularCityAdapter adapter2 = new PopularCityAdapter(cities);
        recyclerViewCityCategory.setAdapter(adapter);
        recyclerViewCityPopular.setAdapter(adapter2);



        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action required here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    // Fetch predictions after the user has typed at least 3 characters
                    fetchPlacePredictions(s.toString());
                } else {
                    // Clear the predictions if input is too short

                    suggestionAdapter.updateData(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action required here
            }
        });


//        searchET.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedCity = (String) parent.getItemAtPosition(position);
//            Log.e("placeid",selectedCity);
//
//            DestinationDetailsBottomSheet bottomSheet = DestinationDetailsBottomSheet.newInstance(selectedCity);
//            bottomSheet.show(getSupportFragmentManager(), "DestinationDetailsBottomSheet");
//
////            Intent intent = new Intent(DestinationActivity.this, DestinationDetailsActivity.class);
////            intent.putExtra("city_name", selectedCity);
////            startActivity(intent);
//        });
    }

    private void setUserInfo(String userId) {
        DocumentReference userRef = FirebaseUtils.getUserDocument(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    if (user != null) {
                        usernameTV.setText(user.getUserName());
                        // Optionally, load profile picture using Glide or Picasso
//                        if (user.getProfilePictureUrl() != null) {
//                            Glide.with(DestinationActivity.this)
//                                .load(user.getProfilePictureUrl())
//                                .into(profileImageView);
//                        }
                    }
                } else {
                    // Handle the case where the document doesn't exist
                }
            } else {
                // Handle the failure
            }
        });
    }


    private void fetchPlacePredictions(String query) {
        // Build the autocomplete request
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(Arrays.asList("locality"))
                .setQuery(query)
                .build();

        // Make the request to fetch predictions
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    // Update adapter with the new list of predictions
                    suggestionAdapter.updateData(predictions);
                })
                .addOnFailureListener(exception -> {
                    Log.e("PlaceError", "Error fetching place predictions: " + exception.getMessage());
                });
    }

//    private void fetchPlaceDetails(String placeId) {
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
//        FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
//
//        placesClient.fetchPlace(fetchPlaceRequest)
//                .addOnSuccessListener(response -> {
//                    Place place = response.getPlace();
////                    Intent intent = new Intent(DestinationActivity.this, DestinationDetailsActivity.class);
////                    intent.putExtra("place_id", place.getId());
////                    startActivity(intent);
//                })
//                .addOnFailureListener(e -> Log.e("Place Error", "Place not found: " + e.getMessage()));
//    }

}















