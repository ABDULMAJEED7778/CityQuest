package com.example.cityquest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class Trips implements Parcelable {
    private String tripId;
    private String destination; // City name
    private String destinationId; // City ID
    private String startDate; // Start date of the trip
    private String endDate;   // End date of the trip
    private String companion; // Companion type (family, friends, etc.)
    private List<String> activities; // List of activities chosen by the user
    private boolean isCustomPlan; // Whether the user selected a custom or ready-made plan
    private Map<String, List<String>> dailyItinerary; // Custom activities/locations for each day

    // Empty constructor (required for Firestore)
    public Trips() {}

    // Constructor
    public Trips(String tripId, String destination, String destinationId, String startDate, String endDate, String companion, List<String> activities, boolean isCustomPlan, Map<String, List<String>> dailyItinerary) {
        this.tripId = tripId;
        this.destination = destination;
        this.destinationId = destinationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companion = companion;
        this.activities = activities;
        this.isCustomPlan = isCustomPlan;
        this.dailyItinerary = dailyItinerary;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public boolean isCustomPlan() {
        return isCustomPlan;
    }

    public void setCustomPlan(boolean customPlan) {
        isCustomPlan = customPlan;
    }

    public Map<String, List<String>> getDailyItinerary() {
        return dailyItinerary;
    }

    public void setDailyItinerary(Map<String, List<String>> dailyItinerary) {
        this.dailyItinerary = dailyItinerary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tripId);
        dest.writeString(destination);
        dest.writeString(destinationId);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(companion);
        dest.writeStringList(activities);
        dest.writeByte((byte) (isCustomPlan ? 1 : 0));
        dest.writeMap(dailyItinerary);
    }

    protected Trips(Parcel in) {
        tripId = in.readString();
        destination = in.readString();
        destinationId = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        companion = in.readString();
        activities = in.createStringArrayList();
        isCustomPlan = in.readByte() != 0;
        dailyItinerary = in.readHashMap(Map.class.getClassLoader());
    }

    public static final Creator<Trips> CREATOR = new Creator<Trips>() {
        @Override
        public Trips createFromParcel(Parcel in) {
            return new Trips(in);
        }

        @Override
        public Trips[] newArray(int size) {
            return new Trips[size];
        }
    };
}
