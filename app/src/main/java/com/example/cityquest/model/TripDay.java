package com.example.cityquest.model;

import java.util.List;

public class TripDay {
    private int dayNumber; // e.g., "Day1", "Day2"
    private List<ItineraryPlace> places; // List of places visited on this day
    private boolean isExpanded = false; // Add this flag to handle the expansion state


    // Empty constructor required for Firestore
    public TripDay() {
    }

    public TripDay(int dayNumber, List<ItineraryPlace> places) {
        this.dayNumber = dayNumber;
        this.places = places;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public List<ItineraryPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<ItineraryPlace> places) {
        this.places = places;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
