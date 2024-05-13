package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.flexbox.FlexboxLayout;

public class SummaryActivity extends AppCompatActivity {

    Button nextBtn,backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nextBtn = findViewById(R.id.nextBtn_sum);
        nextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this,TripsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        backBtn = findViewById(R.id.backBtn_sum);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this,InterestsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });



        FlexboxLayout flexboxLayout = findViewById(R.id.interest_flexbox_sum);
        String[] interests = {"Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage",
                "Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage"};

        for (String interest : interests) {
            TextView textView = new TextView(this);
            textView.setText(interest);
            textView.setBackgroundResource(R.drawable.interest_background_sum); // Set background drawable
            textView.setPadding(16, 8, 16, 8); // Set padding
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16,16,16,16); // Set margins
            textView.setLayoutParams(layoutParams); // Set layout parameters
            flexboxLayout.addView(textView); // Add TextView to FlexboxLayout
        }


    }
}