package com.example.cityquest.model;

public class ItineraryPlace {
    private String placeName; // Name of the place
    private String photoUrl; // Photo URL of the place
    private String overview; // Description/Overview of the place

    // Empty constructor required for Firestore
    public ItineraryPlace() {}

    // Constructor
    public ItineraryPlace(String placeName, String photoUrl, String overview) {
        this.placeName = placeName;
        this.photoUrl = photoUrl;
        this.overview = overview;
    }

    // Getters and setters
    // (Generate getters and setters for all fields)

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
