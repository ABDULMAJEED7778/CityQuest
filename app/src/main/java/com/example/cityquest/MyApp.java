package com.example.cityquest;

import android.app.Application;
import com.google.android.libraries.places.api.Places;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String apiKey = getString(R.string.google_maps_api_key);
        // Initialize the Places API only once for the entire app
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),apiKey );
        }
    }
}

