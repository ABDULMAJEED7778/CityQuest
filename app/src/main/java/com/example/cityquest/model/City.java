package com.example.cityquest.model;

public class City {
    private String CityName;
    private int BackgroundDrawable;
    private String CountryName;
    private String CityImageUrl;

    private String CityRating;
    private String CityDescription;




    public City() {
    }

    public City(String cityName, int backgroundDrawable, String countryName, String cityRating) {
        CityName = cityName;
        BackgroundDrawable = backgroundDrawable;
        CountryName = countryName;
        CityRating = cityRating;
    }

    public City(String cityName, String cityImageUrl, String cityDescription) {
        CityName = cityName;
        CityImageUrl = cityImageUrl;
        CityDescription = cityDescription;
    }

    public City(String cityName, String countryName) {
        CityName = cityName;
        CountryName = countryName;
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

    public String toString() {
        return "City{name='" + getCityName() + "', imageResId=" + getBackgroundDrawable() + ", country='" + getCountryName() + "', rating='" + getCityRating() + "'}";
    }

    public String getCityImageUrl() {
        return CityImageUrl;
    }

    public void setCityImageUrl(String cityImageUrl) {
        CityImageUrl = cityImageUrl;
    }

    public String getCityDescription() {
        return CityDescription;
    }

    public void setCityDescription(String cityDescription) {
        CityDescription = cityDescription;
    }
}

