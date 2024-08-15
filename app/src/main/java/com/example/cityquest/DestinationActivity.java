package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.cityquest.model.City;
import com.example.cityquest.model.Filter;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.olaMap.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ola.mapsdk.MapFactory;

public class DestinationActivity extends AppCompatActivity {

    private Button skipBtn, backBtn, logOutBtn;
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
        backBtn = findViewById(R.id.backBtn_dest);
        logOutBtn = findViewById(R.id.logoutBtn_dest);
        usernameTV = findViewById(R.id.userName_Destination);




        // Set up buttons' onClick listeners
        skipBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, DateRangeActivity.class);
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
            String selectedItem = (String) parent.getItemAtPosition(position);
            // Handle the selected item
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
        new Thread(() -> {
            try {
                String apiKey = getString(R.string.api_key);  // Assuming you store your API key in strings.xml
                String response = NetworkUtils.getCitySuggestions(searchText, apiKey);  // Update this to match Ola Maps API
                Log.d("API_RESPONSE", "Raw Response: " + response);
                runOnUiThread(() -> handleApiResponse(response));
            } catch (IOException e) {
                Log.e("API_ERROR", "Error fetching suggestions: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(DestinationActivity.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private void handleApiResponse(String response) {
        List<String> suggestions = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray predictions = jsonObject.getJSONArray("predictions");
            for (int i = 0; i < predictions.length(); i++) {
                JSONObject prediction = predictions.getJSONObject(i);

                // Check if the prediction is of type "locality" (city)
                JSONArray types = prediction.getJSONArray("types");
                for (int j = 0; j < types.length(); j++) {
                    if (types.getString(j).equals("locality")) {
                        // Extract the city name, which is usually the first term
                        JSONArray terms = prediction.getJSONArray("terms");
                        if (terms.length() > 0) {
                            String cityName = terms.getJSONObject(0).getString("value");
                            suggestions.add(cityName);
                        }
                        break; // Exit the loop after finding a locality type
                    }
                }
            }

            // Set the adapter with city names only
            CityArrayAdapter adapter = new CityArrayAdapter(this, suggestions);
            searchET.setAdapter(adapter);
            searchET.setThreshold(1); // Start filtering after 1 character

        } catch (JSONException e) {
            Toast.makeText(DestinationActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    // In your DestinationActivity.java



    // Ensure this method is called after your API response is processed



}
