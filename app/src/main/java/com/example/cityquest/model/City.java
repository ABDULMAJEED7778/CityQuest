package com.example.cityquest.model;

public class City {
    private String CityName;

    private String CountryName;
    private String cityType;
    private String cityId;


    private String CityImageUrl;

    private String CityRating;
    private String CityDescription;


    public City() {
    }

    public City(String cityName, String countryName, String cityType, String cityId, String cityImageUrl, String cityRating, String cityDescription) {
        CityName = cityName;
        CountryName = countryName;
        this.cityType = cityType;
        this.cityId = cityId;
        CityImageUrl = cityImageUrl;
        CityRating = cityRating;
        CityDescription = cityDescription;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityImageUrl() {
        return CityImageUrl;
    }

    public void setCityImageUrl(String cityImageUrl) {
        CityImageUrl = cityImageUrl;
    }

    public String getCityRating() {
        return CityRating;
    }

    public void setCityRating(String cityRating) {
        CityRating = cityRating;
    }

    public String getCityDescription() {
        return CityDescription;
    }

    public void setCityDescription(String cityDescription) {
        CityDescription = cityDescription;
    }

    @Override
    public String toString() {
        return "City{" +
                "CityName='" + CityName + '\'' +
                ", cityType='" + cityType + '\'' +
                '}';
    }
}

