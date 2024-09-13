package com.example.cityquest.model;

public class Photo {
    private String url;
    private String photographer;


    public Photo(String url, String photographer) {
        this.url = url;
        this.photographer = photographer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }
}

