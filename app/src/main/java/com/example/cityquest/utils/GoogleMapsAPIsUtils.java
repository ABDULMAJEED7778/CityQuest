package com.example.cityquest.utils;

import static androidx.core.content.ContextCompat.getString;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityquest.BuildConfig;
import com.example.cityquest.R;
import com.example.cityquest.model.PlaceDetails;
import com.example.cityquest.model.TravelInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;
import com.google.android.libraries.places.api.net.SearchNearbyResponse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapsAPIsUtils {

    private String apiKey;
    private static final String TAG = "GooglePlacesUtils";
    private PlacesClient placesClient;



    private static final String API_KEY =  BuildConfig.MAPS_API_KEY;

    private Context context;
    private RequestQueue requestQueue;

    public GoogleMapsAPIsUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public GoogleMapsAPIsUtils(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;
        if (!Places.isInitialized()) {
            Places.initialize(context, apiKey);
        }
        this.placesClient = Places.createClient(context);
    }





    public interface NearbySearchCallback {
        void onSuccess(List<PlaceDetails> placeDetailsList);
        void onError(String errorMessage);
    }

    // Define an interface for distance callback
    public interface DistanceCallback {
        void onSuccess(List<TravelInfo> travelInfoList);
        void onError(String errorMessage);
    }

    // Method to get distances for all places in one API call
    public void getDistancesForDay(List<String> placeIds, String travelMode, DistanceCallback callback) {
        // Build the Distance Matrix API URL
        String url = buildDistanceMatrixUrl(placeIds, travelMode);

        // Make the API request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Parse the response to extract travel information for each segment
                    List<TravelInfo> travelInfoList = parseDistanceResponse(response, placeIds.size());
                    if (travelInfoList != null) {
                        callback.onSuccess(travelInfoList);
                    } else {
                        callback.onError("Failed to get travel info.");
                    }
                },
                error -> callback.onError(error.getMessage())
        );

        // Add the request to a request queue (e.g., Volley queue)
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    // Helper method to build the Distance Matrix API URL
    private String buildDistanceMatrixUrl(List<String> placeIds, String travelMode) {
        StringBuilder origins = new StringBuilder();
        StringBuilder destinations = new StringBuilder();

        // Build origins and destinations for all pairs //TODO improve this logic
        for (int i = 0; i < placeIds.size(); i++) {
            origins.append("place_id:").append(placeIds.get(i));
            if (i < placeIds.size() - 1) {
                origins.append("|"); // Use "|" to separate origins
            }
        }

        // Build destinations from the second place to the last
        for (int i = 1; i < placeIds.size(); i++) {
            destinations.append("place_id:").append(placeIds.get(i));
            if (i < placeIds.size() - 1) {
                destinations.append("|"); // Use "|" to separate destinations
            }
        }

        Log.e("request", "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins
                + "&destinations=" + destinations
                + "&mode=" + travelMode
                + "&key=" + apiKey);


        return "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins
                + "&destinations=" + destinations
                + "&mode=" + travelMode
                + "&key=" + apiKey;
    }

    // Helper method to parse the API response and extract travel info
    private List<TravelInfo> parseDistanceResponse(JSONObject response, int placeCount) {
        List<TravelInfo> travelInfoList = new ArrayList<>();
        try {
            JSONArray rows = response.getJSONArray("rows");

            for (int i = 0; i < placeCount - 1; i++) { // For n places, we have n-1 rows
                JSONArray elements = rows.getJSONObject(i).getJSONArray("elements");

                    JSONObject element = elements.getJSONObject(i);
                    String duration = element.getJSONObject("duration").getString("text");
                    String distance = element.getJSONObject("distance").getString("text");

                    travelInfoList.add(new TravelInfo(duration, distance));

            }
            Log.e("travelInfoList", travelInfoList.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return travelInfoList;
    }

    //this is for nearby places

    // Method to perform nearby search using PlacesClient
    public void searchNearbyPlaces(double lat, double lng, int radius, List<String> includedTypes,int maxResultCount, NearbySearchCallback callback) {
        LatLng center = new LatLng(lat, lng);
        CircularBounds bounds = CircularBounds.newInstance(center, radius);

        final List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.RATING,
                Place.Field.ADDRESS,
                Place.Field.BUSINESS_STATUS
        );

        // Create the request
        SearchNearbyRequest searchNearbyRequest = SearchNearbyRequest.builder(bounds, placeFields)
                .setIncludedTypes(includedTypes)
                .setMaxResultCount(maxResultCount)
                .build();

        // Perform the search
        placesClient.searchNearby(searchNearbyRequest).addOnSuccessListener(response -> {
            List<PlaceDetails> placeDetailsList = parseSearchNearbyResponse(response);
            callback.onSuccess(placeDetailsList);
            Log.e(TAG,"nearby search request is successful" );
        }).addOnFailureListener(error -> {
            Log.e(TAG, "Error: " + error.getMessage());
            callback.onError(error.getMessage());
        });
    }

    // Helper method to parse the PlacesClient search response
    private List<PlaceDetails> parseSearchNearbyResponse(SearchNearbyResponse response) {
        List<PlaceDetails> placeDetailsList = new ArrayList<>();
        for (Place place : response.getPlaces()) {
            String id = place.getId();
            String name = place.getName();
            String address = place.getAddress();

            // Retrieve the rating, if available
            double rating = place.getRating() != null ? place.getRating() : 0.0;



            // Check if the place is open now
            boolean isOpenNow = place.getBusinessStatus() == Place.BusinessStatus.OPERATIONAL;


            // Create PlaceDetails object with the extracted data
            PlaceDetails placeDetails = new PlaceDetails(id, name, rating, address, isOpenNow);
            placeDetailsList.add(placeDetails);

            if (place.getPhotoMetadatas() != null && !place.getPhotoMetadatas().isEmpty()) {
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();

                // Fetch photo asynchronously and set it in PlaceDetails
                placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener(fetchPhotoResponse -> {
                            placeDetails.setPhotoBitmap(fetchPhotoResponse.getBitmap());
                            // Notify data change if you’re using a RecyclerView to refresh the view with the photo
                        })
                        .addOnFailureListener(exception -> {
                            Log.e(TAG, "Photo fetch failed: " + exception.getMessage());
                        });
            }
        }
        return placeDetailsList;
    }






}
