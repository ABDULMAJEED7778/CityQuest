package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityquest.model.Trips;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

public class SummaryActivity extends AppCompatActivity {

    Button nextBtn,backBtn;
    TextView cityNameTV,CompanionTV,dateRangeTV;
    Trips trip;
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

        trip = getIntent().getParcelableExtra("trip");
        if (trip == null) {
            trip = new Trips();
        }
        cityNameTV = findViewById(R.id.cityName_summary);
        CompanionTV = findViewById(R.id.companion_summary);
        dateRangeTV = findViewById(R.id.date_range_summary);



        setTripSummaryData();

        nextBtn = findViewById(R.id.nextBtn_sum);


        nextBtn.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();

                // Get the trips collection for the current user
                CollectionReference tripsCollection = FirebaseUtils.getTripsCollection(userId);

                // Add the trip
                tripsCollection.add(trip).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Trip saved successfully, move to the next activity
                        Intent intent = new Intent(SummaryActivity.this, TripsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        // Handle the error
                        Toast.makeText(this, task.getException() != null ? task.getException().getMessage() : "Error saving trip.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            }
        });





        backBtn = findViewById(R.id.backBtn_sum);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this,InterestsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });



        FlexboxLayout flexboxLayout = findViewById(R.id.interest_flexbox_sum);

        for (String interest : trip.getActivities()) {
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

    private void setTripSummaryData() {

        String dateRange = trip.getStartDate() + " - " + trip.getEndDate();
        cityNameTV.setText(trip.getDestination());
        CompanionTV.setText(trip.getCompanion());
        dateRangeTV.setText(dateRange);
    }
}