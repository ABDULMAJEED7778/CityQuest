package com.example.cityquest;

import static java.security.AccessController.getContext;

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
import androidx.recyclerview.widget.PagerSnapHelper;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.view.View;

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
    private RecyclerView searchRecyclerView;

    private PopularCityAdapter popularCityAdapter;
    private PlacesClient placesClient;
    private List<City> cities;

    private PlaceAutocompleteAdapter suggestionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);


        // Change icon color
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(uiOptions);

        String apiKey = getString(R.string.google_maps_api_key);
        Places.initializeWithNewPlacesApiEnabled(this, apiKey);
        placesClient = Places.createClient(this);

        // Initialize RecyclerView for showing autocomplete results
        searchRecyclerView = findViewById(R.id.recyclerView_search_suggestion);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Autocomplete Adapter
        suggestionAdapter = new PlaceAutocompleteAdapter(this, new ArrayList<>());
        searchRecyclerView.setAdapter(suggestionAdapter);

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

        mapBtn = findViewById(R.id.mapBtn_dest);
        backBtn = findViewById(R.id.backBtn_dest);
        skipBtn = findViewById(R.id.skipBtn_dest);
        logOutBtn = findViewById(R.id.logoutBtn_dest);
//        usernameTV = findViewById(R.id.userName_Destination);





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

        RecyclerView recyclerViewCityPopular = findViewById(R.id.recyclerView_city_popular);


        cities = new ArrayList<>();
        popularCityAdapter = new PopularCityAdapter(cities);
        Log.e("gjgj","htdgdht");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCityPopular.setLayoutManager(layoutManager);
        recyclerViewCityPopular.setAdapter(popularCityAdapter);
        // Fetch cities data from Firestore
        fetchCitiesFromFirestore();

//        cities.add(new City("Bangalore", "India", "1", "https://images.pexels.com/photos/3573385/pexels-photo-3573385.jpeg", "4.8", "A vibrant city known for its technology industry and parks."));
//        cities.add(new City("Delhi", "India", "2", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "The capital city of India, known for its rich history and culture."));
//        cities.add(new City("Mumbai", "India", "3", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India."));
//        cities.add(new City("Chennai", "India", "4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.6", "A coastal city famous for its cultural heritage and temples."));


        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("Historical", R.drawable.historical_icon));
        filters.add(new Filter("Cultural", R.drawable.historical_icon));
        filters.add(new Filter("Modern", R.drawable.historical_icon));

        RecyclerView recyclerViewFilters = findViewById(R.id.recyclerViewFilters);
        recyclerViewFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FilterAdapter adapter3 = new FilterAdapter(filters, this);
        recyclerViewFilters.setAdapter(adapter3);



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
                    searchRecyclerView.setVisibility(View.GONE);
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

    private void fetchCitiesFromFirestore() {
        FirebaseUtils.getCitiesCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            City city = document.toObject(City.class);  // Convert document to City object
                            cities.add(city);
                        }

                        // Notify RecyclerView adapter about data changes
                        popularCityAdapter.notifyDataSetChanged();

//                        // Now initialize the ViewPager adapter after data is fetched
//                        cityPagerAdapter = new CityPagerAdapter(requireActivity(), cityList);
//                        viewPager.setAdapter(cityPagerAdapter);

                    } else {
                        Log.d("CityActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setUserInfo(String userId) {
        DocumentReference userRef = FirebaseUtils.getUserDocument(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    if (user != null) {
//                        usernameTV.setText(user.getUserName());
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
                    searchRecyclerView.setVisibility(View.VISIBLE);
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















