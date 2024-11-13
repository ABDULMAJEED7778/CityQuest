package com.example.cityquest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItineraryPlace implements Parcelable {
    private String placeName; // Name of the place
    private String placeId; // Unique identifier for the place
    private String photoUrl; // Photo URL of the place
    private String overview; // Description/Overview of the place

    // Empty constructor required for Firestore
    public ItineraryPlace() {}

    public ItineraryPlace(String placeName, String placeId, String photoUrl, String overview) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.photoUrl = photoUrl;
        this.overview = overview;
    }

    // Parcelable implementation
    protected ItineraryPlace(Parcel in) {
        placeName = in.readString();
        placeId = in.readString();
        photoUrl = in.readString();
        overview = in.readString();
    }

    public static final Creator<ItineraryPlace> CREATOR = new Creator<ItineraryPlace>() {
        @Override
        public ItineraryPlace createFromParcel(Parcel in) {
            return new ItineraryPlace(in);
        }

        @Override
        public ItineraryPlace[] newArray(int size) {
            return new ItineraryPlace[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(placeId);
        parcel.writeString(photoUrl);
        parcel.writeString(overview);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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
