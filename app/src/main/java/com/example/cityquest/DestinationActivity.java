package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.cityquest.adapter.PopularCityAdapter;
import com.example.cityquest.apiCalls.VolleyCallback;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Filter;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.apiCalls.NetworkUtils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private Button skipBtn, backBtn, logOutBtn,mapBtn;
    private AutoCompleteTextView searchET;
    private TextView usernameTV;
    private RecyclerView recyclerView;

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

        searchET = findViewById(R.id.search_input);
        searchET.setContentDescription("Search for a city or place");
        skipBtn = findViewById(R.id.skipBtn_dest);
        mapBtn = findViewById(R.id.mapBtn_dest);
        backBtn = findViewById(R.id.backBtn_dest);
        logOutBtn = findViewById(R.id.logoutBtn_dest);
        usernameTV = findViewById(R.id.userName_Destination);




        // Set up buttons' onClick listeners
        skipBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, DateRangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
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

        // Set up search functionality
        searchET.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = (String) parent.getItemAtPosition(position);
            if (!TextUtils.isEmpty(selectedCity)) {
                // Start DestinationDetailsActivity with the selected city name
                Intent intent = new Intent(DestinationActivity.this, DestinationDetailsActivity.class);
                intent.putExtra("city_name", selectedCity);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }
        });
        searchET.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    performSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
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

    private void performSearch(String searchText) {
        String apiKey = getString(R.string.api_key);  // Assuming you store your API key in strings.xml

        NetworkUtils.getCitySuggestions(this, searchText, apiKey, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray response) {
                // Directly pass the JSONArray to handleApiResponse
                handleApiResponse(response);
            }

            @Override
            public void onError(String error) {
                Log.e("API_ERROR", "Error fetching suggestions: " + error);
                Toast.makeText(DestinationActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void handleApiResponse(JSONArray response) {
        List<String> suggestions = new ArrayList<>();
        try {
            // Loop through each item in the JSONArray
            for (int i = 0; i < response.length(); i++) {
                JSONObject result = response.getJSONObject(i);

                // Check if the result is of type "locality"
                JSONArray types = result.getJSONArray("types");
                boolean isLocality = false;
                for (int j = 0; j < types.length(); j++) {
                    if (types.getString(j).equals("locality")) {
                        isLocality = true;
                        break;
                    }
                }

                if (isLocality) {
                    // Extract the name from the structured_formatting object
                    JSONObject structuredFormatting = result.getJSONObject("structured_formatting");
                    String mainText = structuredFormatting.getString("main_text");
                    if (!suggestions.contains(mainText)) {
                        suggestions.add(mainText);
                    }
                }
            }

            // Set the adapter with the city names
            CityArrayAdapter adapter = new CityArrayAdapter(this, suggestions);
            searchET.setAdapter(adapter);
            searchET.setThreshold(1); // Start filtering after 1 character

        } catch (JSONException e) {
            Log.e("api_error", e.getMessage());
            Toast.makeText(DestinationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }









}
