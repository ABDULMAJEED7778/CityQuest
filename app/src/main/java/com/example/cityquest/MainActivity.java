package com.example.cityquest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_explore:
                    selectedFragment = new ExploreAroundActivity();
                    break;
                case R.id.nav_itinerary:
                    selectedFragment = new TripsActivity();
                    break;
                case R.id.nav_community:
                    selectedFragment = new CommunityActivity();
                    break;
                case R.id.nav_saved:
                    selectedFragment = new SavedActivity();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Load the default fragment on startup
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_explore);
        }
    }
}