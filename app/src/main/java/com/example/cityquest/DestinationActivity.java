package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.CityAdapter;
import com.example.cityquest.adapter.FilterAdapter;
import com.example.cityquest.adapter.PopularCityAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Filter;

import java.util.ArrayList;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    Button skipBtn,backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left+24, systemBars.top+30, systemBars.right, systemBars.bottom+30);
            return insets;
        });


        skipBtn = findViewById(R.id.skipBtn_dest);
        skipBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this,DateRangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        backBtn = findViewById(R.id.backBtn_dest);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this,SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });






        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_city_category);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_city_popular);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        List<City> cities = new ArrayList<>();
// Add cities to the list
        cities.add(new City("Bangalore", R.drawable.bangalore, "India","4.8"));
        cities.add(new City("Bangalore", R.drawable.bangalore, "India","4.8"));
        cities.add(new City("Bangalore", R.drawable.bangalore, "India","4.8"));
        cities.add(new City("Bangalore", R.drawable.bangalore, "India","4.8"));

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("Historical", R.drawable.historical_icon));
        filters.add(new Filter("Historical", R.drawable.historical_icon));
        filters.add(new Filter("Historical", R.drawable.historical_icon));


        RecyclerView recyclerViewFilters = findViewById(R.id.recyclerViewFilters);
        recyclerViewFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FilterAdapter adapter3 = new FilterAdapter(filters, this);
        recyclerViewFilters.setAdapter(adapter3);

        // Create and set adapter
        CityAdapter adapter = new CityAdapter(cities);
        PopularCityAdapter adapter2 = new PopularCityAdapter(cities);
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);
    }
}