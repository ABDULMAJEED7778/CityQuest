package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.CityAdapter;

import com.example.cityquest.adapter.CompanionAdapter;
import com.example.cityquest.adapter.FilterAdapter;
import com.example.cityquest.adapter.PopularCityAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Companion;
import com.example.cityquest.model.Filter;
import com.example.cityquest.model.Trips;

import java.util.ArrayList;
import java.util.List;

public class CompanionsActivity extends AppCompatActivity {

    Button nextBtn,backBtn;
    Trips trip;
    CompanionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_companions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });










        trip = getIntent().getParcelableExtra("trip");
        if (trip == null) {
            trip = new Trips();
        }

        nextBtn = findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
            String selectedCompanion = adapter.getSelectedCompanion();
            if (selectedCompanion != null) {
                trip.setCompanion(selectedCompanion); // Store selected companion in trip
                Intent intent = new Intent(CompanionsActivity.this, InterestsActivity.class);
                intent.putExtra("trip", trip); // Pass trip object to next activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                // Optionally show a message that no companion is selected
                Toast.makeText(this, "Please select a companion.", Toast.LENGTH_SHORT).show();
            }

        });
        backBtn = findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CompanionsActivity.this,DateRangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });





        GridView gridView = findViewById(R.id.gridView);

        List<Companion> companions = new ArrayList<>();
        companions.add(new Companion(R.drawable.solo_photo, "Going Solo"));
        companions.add(new Companion(R.drawable.solo_photo, "Partner"));
        companions.add(new Companion(R.drawable.solo_photo, "Family"));
        companions.add(new Companion(R.drawable.solo_photo, "Friends"));

        adapter = new CompanionAdapter(this, companions);
        gridView.setAdapter(adapter);










    }
}