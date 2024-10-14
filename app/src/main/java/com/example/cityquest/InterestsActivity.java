package com.example.cityquest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityquest.model.Trips;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    Button nextBtn, backBtn;
    FlexboxLayout chipGroup;  // Use FlexboxLayout to manage dynamic buttons
    Trips trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interests);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondary_color_dark));

        trip = getIntent().getParcelableExtra("trip");
        if (trip == null) {
            trip = new Trips();
        }
        nextBtn = findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
            List<String> selectedActivities = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                AppCompatButton button = (AppCompatButton) chipGroup.getChildAt(i);
                if (button.isSelected()) {
                    selectedActivities.add(button.getText().toString());
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


        chipGroup = findViewById(R.id.chipGroupInterests); // FlexboxLayout in activity_interests.xml
        String[] interests = {"Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage"};

        for (String interest : interests) {
            AppCompatButton button = createChipButton(interest);
            // Set margin
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8)); // Set the desired margins (left, top, right, bottom)
            button.setLayoutParams(params);

//            Chip chip = new Chip(this);
//            chip.setText(interest);
//            chip.setCheckable(true);
//            chip.setChipBackgroundColorResource(R.color.primary_color_light); // Customize as needed
//            // Apply custom text appearance
//            chip.setTextAppearance(R.style.CustomChipTextAppearance);
            chipGroup.addView(button);
        }
    }

    // Method to create a styled AppCompatButton that acts like a chip
    private AppCompatButton createChipButton(String text) {
        AppCompatButton button = new AppCompatButton(this);
        button.setText(text);
        button.setBackgroundResource(R.drawable.chip_background);  // Add a custom background
        button.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
        button.setPadding(dpToPx(12), dpToPx(0), dpToPx(12), dpToPx(0));  // Add padding to look like a chip
        button.setTextAppearance(this,R.style.CustomChipTextAppearance);

        // Set the initial state (not selected)
        button.setSelected(false);
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // No drawable initially


        // Handle toggle selection
        button.setOnClickListener(v -> {
            button.setSelected(!button.isSelected());
            updateChipStyle(button);
        });

        return button;
    }

    // Method to update the style of the button based on its selected state
    private void updateChipStyle(AppCompatButton button) {
        if (button.isSelected()) {
            button.setBackgroundResource(R.drawable.chip_selected);  // Custom drawable for selected state
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_with_circle, 0, 0, 0); // Set tick mark drawable on the left
            button.setCompoundDrawablePadding(dpToPx(8));

        } else {
            button.setBackgroundResource(R.drawable.chip_background);  // Custom drawable for default state
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // Remove tick mark drawable

        }
    }

    // Convert dp to pixels
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}