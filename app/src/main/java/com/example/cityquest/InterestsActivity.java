package com.example.cityquest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.flexbox.FlexboxLayout;

public class InterestsActivity extends AppCompatActivity {

    Button nextBtn, backBtn;
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
        nextBtn = findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(InterestsActivity.this,SummaryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        backBtn = findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(InterestsActivity.this,CompanionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });



        FlexboxLayout flexboxLayout = findViewById(R.id.interest_flexbox_interests);
        String[] interests = {"Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage",
                "Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage"};

        for (String interest : interests) {
            Button button = new Button(this);
            button.setText(interest);
            button.setBackgroundResource(R.drawable.interest_background_int_default); // Set background drawable
            button.setTextColor(getResources().getColor(R.color.primary_color)); // Set default text color
            Typeface defaultFont = Typeface.create("sans-serif", Typeface.NORMAL);
            button.setTypeface(defaultFont);
            button.setPadding(16, 8, 16, 8); // Set padding
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Change text color and background when clicked
                    if (button.getCurrentTextColor() == getResources().getColor(R.color.primary_color)) {
                        button.setTextColor(getResources().getColor(R.color.secondary_color));
                        button.setBackgroundResource(R.drawable.interest_background_int_clicked);
                    } else {
                        button.setTextColor(getResources().getColor(R.color.primary_color));
                        button.setBackgroundResource(R.drawable.interest_background_int_default);
                    }
                }
            });
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16); // Set margins
            button.setLayoutParams(layoutParams); // Set layout parameters
            flexboxLayout.addView(button); // Add Button to FlexboxLayout
        }

    }
}