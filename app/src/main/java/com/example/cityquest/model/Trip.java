package com.example.cityquest.model;

public class Trip {

    private String rating;
    private String name;
    private String dateRange;
    private String companion;
    private int backgroundDrawable;


    public Trip() {
    }

    public Trip(String rating, String name, String dateRange, String companion,int backgroundDrawable) {
        this.rating = rating;
        this.name = name;
        this.dateRange = dateRange;
        this.companion = companion;
        this.backgroundDrawable = backgroundDrawable;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    public int getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }
}
