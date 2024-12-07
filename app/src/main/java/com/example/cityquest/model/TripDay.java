package com.example.cityquest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripDay implements Parcelable  {
    private int dayNumber; // e.g., "Day1", "Day2"
    private List<ItineraryPlace> places; // List of places visited on this day
    private boolean isExpanded = false; // Add this flag to handle the expansion state
    private List<String> notes;



    // Empty constructor required for Firestore
    public TripDay() {
        this.notes = new ArrayList<>();
    }

    public TripDay(int dayNumber, List<ItineraryPlace> places) {
        this.dayNumber = dayNumber;
        this.places = places;
    }

    public TripDay(int dayNumber, List<ItineraryPlace> places, List<String> notes) {
        this.dayNumber = dayNumber;
        this.places = places;
        this.notes = notes;
    }


    // Copy constructor
    public TripDay(TripDay original) {
        this.dayNumber = original.dayNumber;
        this.isExpanded = original.isExpanded;
        this.places = new ArrayList<>();
        for (ItineraryPlace place : original.places) {
            this.places.add(new ItineraryPlace(place)); // Assuming ItineraryPlace has a copy constructor
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDay tripDay = (TripDay) o;
        return dayNumber == tripDay.dayNumber &&
                isExpanded == tripDay.isExpanded &&
                Objects.equals(places, tripDay.places); // Use equals for the list comparison
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayNumber, places, isExpanded);
    }

    // Parcelable implementation
    protected TripDay(Parcel in) {
        dayNumber = in.readInt();
        places = in.createTypedArrayList(ItineraryPlace.CREATOR);
        isExpanded = in.readByte() != 0; // Read boolean as byte
    }
    public static final Parcelable.Creator<TripDay> CREATOR = new Parcelable.Creator<TripDay>() {
        @Override
        public TripDay createFromParcel(Parcel in) {
            return new TripDay(in);
        }

        @Override
        public TripDay[] newArray(int size) {
            return new TripDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(dayNumber);
        parcel.writeTypedList(places);
        parcel.writeByte((byte) (isExpanded ? 1 : 0)); // Write boolean as byte
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


    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
