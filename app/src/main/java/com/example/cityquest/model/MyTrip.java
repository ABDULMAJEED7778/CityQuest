package com.example.cityquest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class MyTrip implements Parcelable {

    private String tripId;
    private String destinationCity;
    private String dateRange;
    private String company;
    private List<String> activities;
    private String tripType;
    private Map<String, List<String>> navigationList;

    // Empty constructor (required for Firestore)
    public MyTrip() {}

    // Constructor with parameters
    public MyTrip(String tripId, String destinationCity, String dateRange, String company, List<String> activities, String tripType, Map<String, List<String>> navigationList) {
        this.tripId = tripId;
        this.destinationCity = destinationCity;
        this.dateRange = dateRange;
        this.company = company;
        this.activities = activities;
        this.tripType = tripType;
        this.navigationList = navigationList;
    }

    protected MyTrip(Parcel in) {
        tripId = in.readString();
        destinationCity = in.readString();
        dateRange = in.readString();
        company = in.readString();
        activities = in.createStringArrayList();
        tripType = in.readString();
    }

    public static final Creator<MyTrip> CREATOR = new Creator<MyTrip>() {
        @Override
        public MyTrip createFromParcel(Parcel in) {
            return new MyTrip(in);
        }

        @Override
        public MyTrip[] newArray(int size) {
            return new MyTrip[size];
        }
    };

    // Getters and Setters
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Map<String, List<String>> getNavigationList() {
        return navigationList;
    }

    public void setNavigationList(Map<String, List<String>> navigationList) {
        this.navigationList = navigationList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tripId);
        dest.writeString(destinationCity);
        dest.writeString(dateRange);
        dest.writeString(company);
        dest.writeStringList(activities);
        dest.writeString(tripType);
    }
}
