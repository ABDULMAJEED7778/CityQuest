package com.example.cityquest.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityquest.model.TravelInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapsAPIsUtils {

    private String apiKey;
    private Context context;

    public GoogleMapsAPIsUtils(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;
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
}
