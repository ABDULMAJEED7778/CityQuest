package com.example.cityquest;

import android.app.Application;
import android.util.Log;

import com.google.android.libraries.places.api.Places;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String apiKey = BuildConfig.MAPS_API_KEY;
        // Initialize the Places API only once for the entire app
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this, apiKey);
            Log.e("MyApp", "Places API initialized");
        }
    }
}

