package com.example.cityquest.utils;

import android.util.Log;

import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.Trip;
import com.example.cityquest.model.TripDay;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirestoreTripUploader {

    private FirebaseFirestore db;

    public FirestoreTripUploader() {
        db = FirebaseFirestore.getInstance();
    }

    // Method to generate and store 20 fixed trips
    public void generateAndStoreTrips() {
        List<ReadyTrips> trips = createTripsList(); // Create a list of 20 trips

        for (ReadyTrips trip : trips) {
            storeTripInFirestore(trip);
        }
    }

    // Method to create a list of 20 trips with detailed itineraries
    private List<ReadyTrips> createTripsList() {
        List<ReadyTrips> trips = new ArrayList<>();

        trips.add(new ReadyTrips("trip1", "Mumbai Experience", "https://images.pexels.com/photos/1553636/pexels-photo-1553636.jpeg", 4.7f,
                "Mumbai", "India", "Sunny", 30.0f, "Urban Adventure", "Solo",
                "2024-04-01", "2024-04-05", "Explore the bustling city life of Mumbai."));

        trips.add(new ReadyTrips("trip2", "Delhi Heritage", "https://images.pexels.com/photos/2662110/pexels-photo-2662110.jpeg", 4.5f,
                "Delhi", "India", "Cloudy", 28.0f, "Historical & Cultural", "Family",
                "2024-04-10", "2024-04-14", "Discover the rich history and culture of Delhi."));

        trips.add(new ReadyTrips("trip3", "Goa Beach Fun", "https://images.pexels.com/photos/1029850/pexels-photo-1029850.jpeg", 4.8f,
                "Goa", "India", "Sunny", 32.0f, "Beach & Relaxation", "Friends",
                "2024-05-01", "2024-05-05", "Enjoy the beaches and nightlife of Goa."));

        trips.add(new ReadyTrips("trip4", "Kerala Backwaters", "https://images.pexels.com/photos/2026593/pexels-photo-2026593.jpeg", 4.6f,
                "Alleppey", "India", "Rainy", 27.0f, "Nature & Wellness", "Couple",
                "2024-06-01", "2024-06-05", "Experience the serene backwaters of Kerala."));

        trips.add(new ReadyTrips("trip5", "Agra and the Taj Mahal", "https://images.pexels.com/photos/1640741/pexels-photo-1640741.jpeg", 4.9f,
                "Agra", "India", "Sunny", 35.0f, "Historical", "Solo",
                "2024-04-01", "2024-04-02", "Witness the beauty of the Taj Mahal in Agra."));

        trips.add(new ReadyTrips("trip6", "Udaipur Royalty", "https://images.pexels.com/photos/3552158/pexels-photo-3552158.jpeg", 4.5f,
                "Udaipur", "India", "Sunny", 33.0f, "Culture & Heritage", "Family",
                "2024-04-03", "2024-04-06", "Explore the royal palaces of Udaipur."));

        trips.add(new ReadyTrips("trip7", "Rajasthan Safari", "https://images.pexels.com/photos/1130226/pexels-photo-1130226.jpeg", 4.7f,
                "Jaisalmer", "India", "Sunny", 38.0f, "Adventure & Safari", "Friends",
                "2024-04-10", "2024-04-13", "Enjoy a desert safari in Rajasthan."));

        trips.add(new ReadyTrips("trip8", "Chennai City Life", "https://images.pexels.com/photos/4346513/pexels-photo-4346513.jpeg", 4.4f,
                "Chennai", "India", "Sunny", 34.0f, "City Exploration", "Couple",
                "2024-04-08", "2024-04-10", "Explore the cultural richness of Chennai."));

        trips.add(new ReadyTrips("trip9", "Jaipur Pink City", "https://images.pexels.com/photos/1932433/pexels-photo-1932433.jpeg", 4.6f,
                "Jaipur", "India", "Sunny", 36.0f, "Heritage & Culture", "Family",
                "2024-04-15", "2024-04-17", "Visit the palaces and forts of Jaipur."));

        trips.add(new ReadyTrips("trip10", "Bangalore Gardens", "https://images.pexels.com/photos/1036861/pexels-photo-1036861.jpeg", 4.5f,
                "Bangalore", "India", "Cloudy", 25.0f, "City & Nature", "Solo",
                "2024-04-18", "2024-04-20", "Explore the beautiful gardens and tech parks."));

        return trips;
    }



    // Method to create and store the itinerary for each trip
    private void storeTripInFirestore(ReadyTrips trip) {
        db.collection("readyTrips").document(trip.getTripId()).set(trip)
                .addOnSuccessListener(aVoid -> {
                    Log.e("tripadd","Trip added successfully: " + trip.getTripId());
                    // Store the itinerary for each trip
                    storeItinerary(trip.getTripId());
                })
                .addOnFailureListener(e -> {
                    Log.e("tripadd","Error adding trip: " + trip.getTripId() + " - " + e.getMessage());
                });
    }

    // Method to create and store the itinerary sub-collections in Firestore
    private void storeItinerary(String tripId) {
        List<TripDay> itinerary = new ArrayList<>();

        // Example: Adding a basic itinerary for demonstration purposes
        itinerary.add(new TripDay(1, Arrays.asList(
                new ItineraryPlace("Gateway of India", "https://example.com/gateway_of_india.jpg", "An iconic arch monument in Mumbai."),
                new ItineraryPlace("Marine Drive", "https://example.com/marine_drive.jpg", "A famous promenade along the coast.")
        )));
        itinerary.add(new TripDay(2, Arrays.asList(
                new ItineraryPlace("Elephanta Caves", "https://example.com/elephanta_caves.jpg", "Ancient rock-cut temples on an island."),
                new ItineraryPlace("Colaba Causeway", "https://example.com/colaba_causeway.jpg", "A vibrant street market.")
        )));

        // Store each day in the itinerary collection
        for (TripDay day : itinerary) {
            db.collection("readyTrips").document(tripId)
                    .collection("itinerary").document(day.getDayNumber()+"").set(day)
                    .addOnSuccessListener(aVoid -> Log.e("tripadd","Day itinerary added for " + tripId + " - " + day.getDayNumber()))
                    .addOnFailureListener(e -> Log.e("tripadd","Error adding itinerary: " + e.getMessage()));
        }
    }


}
