package com.example.cityquest;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.toolbox.ImageLoader;
import com.example.cityquest.adapter.PhotoAdapter;
import com.example.cityquest.apiCalls.NetworkUtils;
import com.example.cityquest.apiCalls.VolleyCallback;
import com.example.cityquest.apiCalls.VolleySingleton;
import com.example.cityquest.model.Photo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DestinationDetailsActivity extends AppCompatActivity {

    private TextView cityDetailsTextView;
    private ViewPager2 viewPager;
    private PhotoAdapter photoAdapter;
    private TabLayout tabLayout;
    Button backBtn,selectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_details);

        cityDetailsTextView = findViewById(R.id.city_details);
        backBtn = findViewById(R.id.backBtn_detail);
        selectBtn = findViewById(R.id.select_btn_detail);
        viewPager = findViewById(R.id.recyclerView_CityDetails);
        tabLayout = findViewById(R.id.tabLayout);


        String cityName = getIntent().getStringExtra("city_name");

        fetchCityDetails(cityName);

        // Fetch photos from Unsplash API
        // Fetch photos from Pexels API
        NetworkUtils.fetchPhotos(this, cityName, getString(R.string.pexels_api_key), new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.e("api", String.valueOf(result));
                List<Photo> photos = parsePhotos(result); // Convert JSONArray to List<Photo>
                photoAdapter = new PhotoAdapter(photos);
                viewPager.setAdapter(photoAdapter);
                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    // Here you can customize tab, e.g., add text or icons
                }).attach();
            }

            @Override
            public void onError(String error) {
                // Handle error
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
        selectBtn.setOnClickListener(v -> {



            Intent intent = new Intent(DestinationDetailsActivity.this, DateRangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });




    }

    private void fetchCityDetails(String cityName) {
        NetworkUtils.getCityDetails(this, cityName, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    // Assuming the result contains a single JSONObject with city details
                    JSONObject cityDetails = result.getJSONObject(0); // Adjust if needed
                    String details = cityDetails.toString(); // Extract relevant details
                    cityDetailsTextView.setText(details);
                } catch (Exception e) {
                    cityDetailsTextView.setText("Error parsing details: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                cityDetailsTextView.setText("Error fetching details: " + error);
            }
        });
    }
    private List<Photo> parsePhotos(JSONArray jsonArray) {
        List<Photo> photos = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject photoObject = jsonArray.getJSONObject(i);
                JSONObject srcObject = photoObject.getJSONObject("src");
                String imageUrl = srcObject.getString("large"); // Use "large" or any other preferred size
                String photographerName = photoObject.getString("photographer");
                photos.add(new Photo(imageUrl, photographerName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos;
    }


}