package com.example.cityquest.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;


@Entity(tableName = "ready_trips")
public class ReadyTrips {

    @NonNull
    @PrimaryKey
    private String tripId; // Firestore document ID
    private String name; // Name of the trip
    private String photoUrl; // Photo URL of the trip
    private float rating; // Rating of the trip
    private String city; // Location city
    private String country; // Location country
    private String weatherStatus; // Weather status (rainy, sunny, etc.)
    private float temperature; // Temperature in Celsius/Fahrenheit
    private String tripType; // Type of trip (adventure, sport, etc.)
    private String companionType; // Companion type (solo, partner, family, friends)
    private String startDate; // Start date of the trip
    private String endDate; // End date of the trip
    private String overview; // Description/Overview of the trip

    // Empty constructor required for Firestore
    @Ignore

    public ReadyTrips() {}

    // Constructor
    public ReadyTrips(String tripId, String name, String photoUrl, float rating, String city, String country,
                String weatherStatus, float temperature, String tripType, String companionType,
                String startDate, String endDate, String overview) {
        this.tripId = tripId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.city = city;
        this.country = country;
        this.weatherStatus = weatherStatus;
        this.temperature = temperature;
        this.tripType = tripType;
        this.companionType = companionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.overview = overview;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getCompanionType() {
        return companionType;
    }

    public void setCompanionType(String companionType) {
        this.companionType = companionType;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


}
