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

        cities.add(new City("Bangalore", "India", "Modern", "1", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.8", "A vibrant city known for its technology industry and parks."));
        cities.add(new City("Delhi", "India", "Historical", "2", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "The capital city of India, known for its rich history and culture."));
        cities.add(new City("Mumbai", "India", "Modern", "3", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India."));
        cities.add(new City("Chennai", "India", "Coastal", "4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.6", "A coastal city famous for its cultural heritage and temples."));
        cities.add(new City("Hyderabad", "India", "Historical", "5", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.4", "Known for its iconic Charminar and rich history."));
        cities.add(new City("Kolkata", "India", "Cultural", "6", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.3", "A city with rich cultural heritage and colonial architecture."));
        cities.add(new City("Pune", "India", "Historical", "7", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.5", "Known for its historical significance and academic institutions."));
        cities.add(new City("Jaipur", "India", "Historical", "8", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.7", "The 'Pink City' known for its royal palaces and forts."));
        cities.add(new City("Ahmedabad", "India", "Cultural", "9", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.2", "Known for its textile industry and historic attractions."));
        cities.add(new City("Surat", "India", "Modern", "10", "https://images.pexels.com/photos/4888907/pexels-photo-4888907.jpeg", "4.1", "Famous for its diamond industry and riverside view."));
        cities.add(new City("Agra", "India", "Historical", "11", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.9", "Home to the iconic Taj Mahal, a historical wonder."));
        cities.add(new City("Lucknow", "India", "Cultural", "12", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.4", "Known for its Nawabi culture and Mughal history."));
        cities.add(new City("Goa", "India", "Coastal", "13", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.8", "A popular coastal destination known for its beaches and nightlife."));
        cities.add(new City("Amritsar", "India", "Historical", "14", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.7", "Known for the Golden Temple and its spiritual significance."));
        cities.add(new City("Varanasi", "India", "Cultural", "15", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.6", "One of the world's oldest cities, famous for its spiritual heritage."));
        cities.add(new City("Udaipur", "India", "Historical", "16", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.9", "Known as the 'City of Lakes' and renowned for its royal palaces."));
        cities.add(new City("Thiruvananthapuram", "India", "Coastal", "17", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.3", "The capital of Kerala, known for its coastal charm and temples."));
        cities.add(new City("Madurai", "India", "Historical", "18", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.5", "An ancient city known for the Meenakshi Temple and cultural heritage."));
        cities.add(new City("Mysore", "India", "Cultural", "19", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.7", "Known for its royal heritage and palaces."));
        cities.add(new City("Coimbatore", "India", "Modern", "20", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.4", "A rapidly growing industrial city with a modern touch."));
        cities.add(new City("Shimla", "India", "Coastal", "21", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.8", "A beautiful hill station known for its colonial architecture."));
        cities.add(new City("Rishikesh", "India", "Cultural", "22", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.9", "Known as the 'Yoga Capital of the World' and for its spiritual vibe."));
        cities.add(new City("Bhopal", "India", "Historical", "23", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "Known for its historical significance and ancient monuments."));
        cities.add(new City("Guwahati", "India", "Cultural", "24", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.6", "Known for its natural beauty and cultural festivals."));
        cities.add(new City("Nagpur", "India", "Modern", "25", "https://images.pexels.com/photos/4888907/pexels-photo-4888907.jpeg", "4.4", "A growing urban center known for its oranges and modern developments."));
        cities.add(new City("Jodhpur", "India", "Historical", "26", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.7", "The 'Blue City,' known for its Mehrangarh Fort and vibrant culture."));
        cities.add(new City("Patna", "India", "Historical", "27", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.2", "One of the oldest continuously inhabited cities, with a rich history."));
        cities.add(new City("Indore", "India", "Modern", "28", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.3", "Known for its cleanliness and as a growing hub for business and education."));
        cities.add(new City("Srinagar", "India", "Coastal", "29", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.9", "Famous for its beautiful lakes and houseboats, a perfect blend of nature and culture."));
        cities.add(new City("Pondicherry", "India", "Coastal", "30", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.7", "A charming coastal town known for its French colonial architecture."));

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
