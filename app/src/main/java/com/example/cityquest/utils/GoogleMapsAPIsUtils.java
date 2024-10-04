package com.example.cityquest.utils;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

public class GoogleMapsAPIsUtils {

    private GeoApiContext context;

    // Initialize GeoApiContext with your API key
    public GoogleMapsAPIsUtils(String apiKey) {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    // Method to calculate the distance between origin and destination
    public DistanceMatrix getDistance(String origin, String destination) throws Exception {
        DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                .origins(origin)
                .destinations(destination)
                .mode(TravelMode.DRIVING) // You can also set walking, bicycling, etc.
                .await();
        return result; // Returns DistanceMatrix object containing distances
    }

    // Shutdown GeoApiContext after usage
    public void shutdown() {
        context.shutdown();
    }
}

