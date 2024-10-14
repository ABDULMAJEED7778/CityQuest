package com.example.cityquest.model;

import java.util.HashMap;



public class TravelInfoCache {

    private static TravelInfoCache instance;
    private HashMap<String, HashMap<String, TravelInfo>> travelInfoCache;

    private TravelInfoCache() {
        travelInfoCache = new HashMap<>();
    }

    public static synchronized TravelInfoCache getInstance() {
        if (instance == null) {
            instance = new TravelInfoCache();
        }
        return instance;
    }

    // Method to add or update cache
    public void addOrUpdateCache(String travelMode, String placeId, TravelInfo travelInfo) {
        // Create a nested HashMap if it doesn't exist
        travelInfoCache.putIfAbsent(travelMode, new HashMap<>());
        // Add or update the TravelInfo for the specified placeId
        travelInfoCache.get(travelMode).put(placeId, travelInfo);
    }

    // Method to get TravelInfo from the cache
    public TravelInfo getTravelInfo(String travelMode, String placeId) {
        if (travelInfoCache.containsKey(travelMode)) {
            return travelInfoCache.get(travelMode).get(placeId);
        }
        return null;
    }
}

