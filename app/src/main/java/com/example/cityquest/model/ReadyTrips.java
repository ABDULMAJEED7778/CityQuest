package com.example.cityquest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.cityquest.utils.Converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity(tableName = "ready_trips")
public class ReadyTrips implements Parcelable {

    @NonNull
    @PrimaryKey
    private String tripId; // Firestore document ID
    private String name; // Name of the trip
    private String photoUrl; // Photo URL of the trip
    private float rating; // Rating of the trip
    private String city; // Location city
    private String destinationId; // City ID


    private String country; // Location country

    @TypeConverters(Converters.class) // Apply TypeConverter to activities
    private List<String> activities; // List of activities chosen by the user

    private String weatherStatus; // Weather status (rainy, sunny, etc.)
    private float temperature; // Temperature in Celsius/Fahrenheit
    private String tripType; // Type of trip (adventure, sport, etc.)
    private String companionType; // Companion type (solo, partner, family, friends)
    private String startDate; // Start date of the trip
    private String endDate; // End date of the trip
    private String overview; // Description/Overview of the trip

    private boolean synced = true; // Flag to indicate if the trip is synced with Firestore

    // Empty constructor required for Firestore
    @Ignore

    public ReadyTrips() {}

    // Constructor
    public ReadyTrips(String tripId, String name, String photoUrl, float rating, String city, String country,
                String weatherStatus, float temperature, String tripType, String companionType,
                String startDate, String endDate, String overview, List<String> activities, String destinationId) {
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
        this.activities = activities;
        this.destinationId = destinationId;
    }


    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
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

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tripId);
        dest.writeString(name);
        dest.writeString(photoUrl);
        dest.writeFloat(rating);
        dest.writeString(city);
        dest.writeString(destinationId);
        dest.writeString(country);
        dest.writeStringList(activities);
        dest.writeString(weatherStatus);
        dest.writeFloat(temperature);
        dest.writeString(tripType);
        dest.writeString(companionType);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(overview);
        dest.writeByte((byte) (synced ? 1 : 0)); // Convert boolean to byte
    }

    // Constructor for creating the object from a Parcel
    protected ReadyTrips(Parcel in) {
        tripId = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        rating = in.readFloat();
        city = in.readString();
        destinationId = in.readString();
        country = in.readString();
        activities = in.createStringArrayList();
        weatherStatus = in.readString();
        temperature = in.readFloat();
        tripType = in.readString();
        companionType = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        overview = in.readString();
        synced = in.readByte() != 0; // Convert byte back to boolean
    }

    // Parcelable.Creator implementation
    public static final Creator<ReadyTrips> CREATOR = new Creator<ReadyTrips>() {
        @Override
        public ReadyTrips createFromParcel(Parcel in) {
            return new ReadyTrips(in);
        }

        @Override
        public ReadyTrips[] newArray(int size) {
            return new ReadyTrips[size];
        }
    };

}
