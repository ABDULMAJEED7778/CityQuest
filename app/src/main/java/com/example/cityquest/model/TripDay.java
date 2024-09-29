package com.example.cityquest.model;

import java.util.List;

public class TripDay {
    private String dayName; // e.g., "Day1", "Day2"
    private List<ItineraryPlace> places; // List of places visited on this day

    // Empty constructor required for Firestore
    public TripDay() {}

    // Constructor
    public TripDay(String dayName, List<ItineraryPlace> places) {
        this.dayName = dayName;
        this.places = places;
    }

    // Getters and setters
    // (Generate getters and setters for all fields)

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<ItineraryPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<ItineraryPlace> places) {
        this.places = places;
    }
}
