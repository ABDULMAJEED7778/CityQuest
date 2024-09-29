package com.example.cityquest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ReadyPlanDetailsActivity extends AppCompatActivity {

    private LinearLayout itinerarySection;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_plan_page);

        TabLayout dayTabLayout = findViewById(R.id.dayTabLayout);
        itinerarySection = findViewById(R.id.itinerary_section);
        scrollView = findViewById(R.id.scrollview);

        // Example data for itineraries
        String[] itineraries = new String[]{
                "Day 1: Visit Central Park and enjoy nature.",
                "Day 2: Explore the museums and local culture.",
                "Day 3: Take a city tour and visit historical sites.",
                "Day 4: Relax at the beach and enjoy a sunset cruise.",
                "Day 5: Shopping and departure."
        };

        // Dynamically create tabs based on the number of days in the trip
        // Dynamically set the number of days based on trip data
        int numberOfDays = 5;
        for (int i = 1; i <= numberOfDays; i++) {
            TabLayout.Tab tab = dayTabLayout.newTab();
            tab.setText("Day " + i);
            dayTabLayout.addTab(tab);
        }

        // Set a listener for when a tab is selected
        dayTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Get the position of the selected tab
                int position = tab.getPosition();

                // Update the itinerary section with the content for the selected day
                updateItineraryContent(itineraries[position]);

                // Scroll to the top of the ScrollView to display the new content
                scrollView.scrollTo(0, 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optionally handle tab unselection
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optionally handle tab reselection
            }
        });

        // Initialize with the itinerary for Day 1
        if (numberOfDays > 0) {
            updateItineraryContent(itineraries[0]);
        }
    }

    private void updateItineraryContent(String content) {
        // Clear previous itinerary content
        itinerarySection.removeAllViews();

        // Create a new TextView to display the itinerary for the selected day
        TextView itineraryTextView = new TextView(this);
        itineraryTextView.setText(content);
        itineraryTextView.setTextSize(16);
        itineraryTextView.setTextColor(getResources().getColor(R.color.primary_color));
        itineraryTextView.setPadding(16, 16, 16, 16);

        // Add the TextView to the itinerary section
        itinerarySection.addView(itineraryTextView);
    }
}
