package com.example.cityquest.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {
    private final MutableLiveData<Location> location = new MutableLiveData<>();

    public void setLocation(Location location) {
        this.location.setValue(location);
    }

    public LiveData<Location> getLocation() {
        return location;
    }
}

