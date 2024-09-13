package com.example.cityquest.apiCalls;



import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// NetworkUtils.java
public class NetworkUtils {

    // Method for city suggestions
    public static void getCitySuggestions(Context context, String input, String apiKey, VolleyCallback callback) {
        String urlString = "https://api.olamaps.io/places/v1/autocomplete?input=" + input + "&api_key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    try {
                        JSONArray suggestions = response.getJSONArray("predictions"); // Adjust if needed
                        callback.onSuccess(suggestions);
                    } catch (JSONException e) {
                        callback.onError(e.toString());
                    }
                },
                error -> callback.onError(error.toString()));

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    // Method for getting city details from Wikipedia
    public static void getCityDetails(Context context, String cityName, VolleyCallback callback) {
        // Construct the URL for the Wikipedia API to get details about the city
        String urlString = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="
                + cityName + "&exintro&explaintext";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    try {
                        JSONObject extract = response.getJSONObject("query").getJSONObject("pages");
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(extract); // Convert to JSONArray if needed
                        callback.onSuccess(jsonArray);
                    } catch (JSONException e) {
                        callback.onError(e.toString());
                    }
                },
                error -> callback.onError(error.toString()));

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    // Method to fetch photos from Unsplash
    public static void fetchPhotos(Context context, String query, String apiKey, VolleyCallback callback) {
        // Construct the Pexels API URL
        String urlString = "https://api.pexels.com/v1/search?query=" + query + "&per_page=10"; // Adjust per_page as needed

        // Create a JsonObjectRequest for Pexels API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    try {
                        JSONArray photos = response.getJSONArray("photos");
                        callback.onSuccess(photos);
                    } catch (JSONException e) {
                        callback.onError(e.toString());
                    }
                },
                error -> callback.onError(error.toString())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", apiKey); // Set the Authorization header with the API key
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void getVectorTileMetadata(Context context, String apiKey, VolleyCallback callback) {
        String urlString = "https://api.olamaps.io/tiles/vector/v1/data/planet.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(response); // Wrap the JSONObject in a JSONArray
                        callback.onSuccess(jsonArray);
                    } catch (Exception e) {
                        callback.onError(e.toString());
                    }
                },
                error -> callback.onError(error.toString())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    // Method to fetch map style
    public static void getMapStyle(Context context, String apiKey, VolleyCallback callback) {
        String urlString = "https://api.olamaps.io/tiles/vector/v1/styles/default-light-standard/style.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(response); // Wrap the JSONObject in a JSONArray
                        callback.onSuccess(jsonArray);
                    } catch (Exception e) {
                        callback.onError(e.toString());
                    }
                },
                error -> callback.onError(error.toString())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}