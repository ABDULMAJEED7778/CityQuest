package com.example.cityquest.model;

public class City {
    private String CityName;
    private int BackgroundDrawable;
    private String CountryName;
    private String CityRating;


    public City() {
    }

    public City(String cityName, int backgroundDrawable, String countryName, String cityRating) {
        CityName = cityName;
        BackgroundDrawable = backgroundDrawable;
        CountryName = countryName;
        CityRating = cityRating;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getBackgroundDrawable() {
        return BackgroundDrawable;
    }

    public void setBackgroundDrawable(int backgroundDrawable) {
        BackgroundDrawable = backgroundDrawable;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCityRating() {
        return CityRating;
    }

    public void setCityRating(String cityRating) {
        CityRating = cityRating;
    }
}

