package com.example.cityquest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.FilterAdapter;
import com.example.cityquest.adapter.TripAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Filter;
import com.example.cityquest.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trips);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));




        List<Trip> trips = new ArrayList<>();
// Add cities to the list
        trips.add(new Trip("4.8","Cultural Delights of Bangalore","Dec 12 - Dec 14 ,2023","Family",R.drawable.bangalore));
        trips.add(new Trip("4.8","Cultural Delights of Bangalore","Dec 12 - Dec 14 ,2023","Family",R.drawable.bangalore));
        trips.add(new Trip("4.8","Cultural Delights of Bangalore","Dec 12 - Dec 14 ,2023","Family",R.drawable.bangalore));
        trips.add(new Trip("4.8","Cultural Delights of Bangalore","Dec 12 - Dec 14 ,2023","Family",R.drawable.bangalore));







        TripAdapter adapter = new TripAdapter(trips);
        recyclerView.setAdapter(adapter);
    }
}