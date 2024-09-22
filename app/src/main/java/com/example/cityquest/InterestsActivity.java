package com.example.cityquest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityquest.model.Trips;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    Button nextBtn, backBtn;
    ChipGroup chipGroup;
    Trips trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interests);
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
            List<String> selectedActivities = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedActivities.add(chip.getText().toString());
                }
            }
            trip.setActivities(selectedActivities); // Store selected activities
            Log.e("intersts",trip.getActivities().toString());


            Intent intent = new Intent(InterestsActivity.this, SummaryActivity.class);
            intent.putExtra("trip", trip); // Pass the trip object
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        backBtn = findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(InterestsActivity.this,CompanionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        chipGroup = findViewById(R.id.chipGroupInterests);
        String[] interests = {"Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage"};

        for (String interest : interests) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.primary_color_light); // Customize as needed
            chip.setTextColor((R.color.primary_color)); // Customize as needed
            chipGroup.addView(chip);
        }


    }
}