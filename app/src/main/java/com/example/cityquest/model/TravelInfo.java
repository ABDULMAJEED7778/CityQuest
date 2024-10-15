package com.example.cityquest.model;

public class TravelInfo {

    private String distance;
    private String duration;

    public TravelInfo(String distance, String duration) {

        this.distance = distance;
        this.duration = duration;
    }



    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return " Distance: " + distance + ", Duration: " + duration;
    }
}
