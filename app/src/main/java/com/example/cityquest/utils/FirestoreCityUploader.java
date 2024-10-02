package com.example.cityquest.utils;

import android.util.Log;

import com.example.cityquest.model.City;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirestoreCityUploader {

    // Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Upload the list of cities to Firestore
    public void uploadCities() {
        // List of cities
        List<City> cities = new ArrayList<>();

        cities.add(new City("Bangalore", "India", "1", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.8", "A vibrant city known for its technology industry and parks."));
        cities.add(new City("Delhi", "India", "2", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "The capital city of India, known for its rich history and culture."));
        cities.add(new City("Mumbai", "India", "3", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India."));
        cities.add(new City("Chennai", "India", "4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.6", "A coastal city famous for its cultural heritage and temples."));
        cities.add(new City("Hyderabad", "India", "5", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.4", "Known for its iconic Charminar and rich history."));
        cities.add(new City("Kolkata", "India", "6", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.3", "A city with rich cultural heritage and colonial architecture."));
        cities.add(new City("Pune", "India", "7", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.5", "Known for its historical significance and academic institutions."));
        cities.add(new City("Jaipur", "India", "8", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.7", "The 'Pink City' known for its royal palaces and forts."));
        cities.add(new City("Ahmedabad", "India", "9", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.2", "Known for its textile industry and historic attractions."));
        cities.add(new City("Surat", "India", "10", "https://images.pexels.com/photos/4888907/pexels-photo-4888907.jpeg", "4.1", "Famous for its diamond industry and riverside view."));

        // Upload each city to Firestore
        for (City city : cities) {
            db.collection("cities").document(city.getCityId()).set(city)
                    .addOnSuccessListener(aVoid -> {
                        // Log success or notify user
                        Log.e("City " ,city.getCityName() + " added successfully!");

                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e("City ","Error adding city: " + e.getMessage());
                    });
        }
    }
}
