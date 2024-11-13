package com.example.cityquest.model;

import android.graphics.Bitmap;

public  class PlaceDetails {
    private String name;
    private String placeId;

    private Bitmap photoBitmap; // Bitmap for photo

    private String formattedAddress;
    private double rating;

    private boolean openNow;


    public PlaceDetails(String id, String name, double rating, String formattedAddress, boolean businessStatus) {
        this.placeId = id;
        this.name = name;
        this.rating = rating;
        this.formattedAddress = formattedAddress;

        this.openNow = businessStatus;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }



    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    @Override
    public String toString() {
        return "PlaceDetails{" +
                "name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", rating=" + rating +
                ", openNow='" + openNow + '\'' +
                '}';
    }
}
