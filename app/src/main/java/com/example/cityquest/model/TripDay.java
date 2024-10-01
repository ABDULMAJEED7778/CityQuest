package com.example.cityquest.model;

import java.util.List;

public class TripDay {
    private int dayNumber; // e.g., "Day1", "Day2"
    private List<ItineraryPlace> places; // List of places visited on this day

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
}
