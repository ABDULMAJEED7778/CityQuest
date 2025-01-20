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
//    public void generateAndStoreTrips() {
//        List<ReadyTrips> trips = createTripsList(); // Create a list of 20 trips
//
//        for (ReadyTrips trip : trips) {
//            storeTripInFirestore(trip);
//        }
//    }

    // Method to create a list of 20 trips with detailed itineraries
//    private List<ReadyTrips> createTripsList() {
//        List<ReadyTrips> trips = new ArrayList<>();
//
//        trips.add(new ReadyTrips("trip1", "Mumbai Experience", "https://images.pexels.com/photos/1553636/pexels-photo-1553636.jpeg", 4.7f,
//                "Mumbai", "India", "Sunny", 30.0f, "Urban Adventure", "Solo",
//                "2024-04-01", "2024-04-05", "Dive into the heart of Mumbai, where the energy of city life collides with vibrant street culture. Explore the Gateway of India, savor the delicious street food, and experience the bustling markets of this megacity."));
//
//        trips.add(new ReadyTrips("trip2", "Delhi Heritage", "https://images.pexels.com/photos/2662110/pexels-photo-2662110.jpeg", 4.5f,
//                "Delhi", "India", "Cloudy", 28.0f, "Historical & Cultural", "Family",
//                "2024-04-10", "2024-04-14", "Immerse yourself in the timeless heritage of Delhi. Visit historical landmarks like the Red Fort and Qutub Minar, stroll through ancient bazaars, and discover the deep cultural layers of India's capital city."));
//
//        trips.add(new ReadyTrips("trip3", "Goa Beach Fun", "https://images.pexels.com/photos/1029850/pexels-photo-1029850.jpeg", 4.8f,
//                "Goa", "India", "Sunny", 32.0f, "Beach & Relaxation", "Friends",
//                "2024-05-01", "2024-05-05", "Revel in the sun-soaked beaches of Goa. Relax by the ocean, dance at beach parties, and taste the local Goan cuisine, all while soaking up the laid-back beach vibes of India's favorite coastal destination."));
//
//        trips.add(new ReadyTrips("trip4", "Kerala Backwaters", "https://images.pexels.com/photos/2026593/pexels-photo-2026593.jpeg", 4.6f,
//                "Alleppey", "India", "Rainy", 27.0f, "Nature & Wellness", "Couple",
//                "2024-06-01", "2024-06-05", "Journey into the tranquil backwaters of Kerala, where serene waters and lush landscapes await. Enjoy a houseboat ride, experience traditional Ayurvedic treatments, and connect with nature in this peaceful paradise."));
//
//        trips.add(new ReadyTrips("trip5", "Agra and the Taj Mahal", "https://images.pexels.com/photos/1640741/pexels-photo-1640741.jpeg", 4.9f,
//                "Agra", "India", "Sunny", 35.0f, "Historical", "Solo",
//                "2024-04-01", "2024-04-02", "Marvel at the iconic Taj Mahal, one of the Seven Wonders of the World. Walk through its beautiful gardens, learn about its rich history, and take in the breathtaking beauty of this timeless monument of love."));
//
//        trips.add(new ReadyTrips("trip6", "Udaipur Royalty", "https://images.pexels.com/photos/3552158/pexels-photo-3552158.jpeg", 4.5f,
//                "Udaipur", "India", "Sunny", 33.0f, "Culture & Heritage", "Family",
//                "2024-04-03", "2024-04-06", "Step into the royal past of Udaipur, where majestic palaces and shimmering lakes offer a glimpse into India's regal history. Visit City Palace, take a boat ride on Lake Pichola, and witness the beauty of this 'City of Lakes.'"));
//
//        trips.add(new ReadyTrips("trip7", "Rajasthan Safari", "https://images.pexels.com/photos/1130226/pexels-photo-1130226.jpeg", 4.7f,
//                "Jaisalmer", "India", "Sunny", 38.0f, "Adventure & Safari", "Friends",
//                "2024-04-10", "2024-04-13", "Embark on an adventurous desert safari in the golden sands of Rajasthan. Ride camels through the dunes, experience vibrant folk performances, and immerse yourself in the mystique of the Thar Desert."));
//
//        trips.add(new ReadyTrips("trip8", "Chennai City Life", "https://images.pexels.com/photos/4346513/pexels-photo-4346513.jpeg", 4.4f,
//                "Chennai", "India", "Sunny", 34.0f, "City Exploration", "Couple",
//                "2024-04-08", "2024-04-10", "Experience the cultural and modern charm of Chennai, a city where tradition meets innovation. Discover its temples, beaches, and bustling markets, and savor its rich culinary heritage."));
//
//        trips.add(new ReadyTrips("trip9", "Jaipur Pink City", "https://images.pexels.com/photos/1932433/pexels-photo-1932433.jpeg", 4.6f,
//                "Jaipur", "India", "Sunny", 36.0f, "Heritage & Culture", "Family",
//                "2024-04-15", "2024-04-17", "Wander through the palaces and forts of Jaipur, the Pink City. Explore the Amber Fort, City Palace, and Hawa Mahal, and immerse yourself in the vibrant culture and history of Rajasthan's capital."));
//
//        trips.add(new ReadyTrips("trip10", "Bangalore Gardens", "https://images.pexels.com/photos/1036861/pexels-photo-1036861.jpeg", 4.5f,
//                "Bangalore", "India", "Cloudy", 25.0f, "City & Nature", "Solo",
//                "2024-04-18", "2024-04-20", "Embark on a journey through the Cultural Delights of Bangalore, where vibrant traditions meet modern innovation. Explore bustling markets, savor aromatic spices, and immerse yourself in the rich heritage of India's Garden City."));
//
//        return trips;
//    }




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

        // Example: Adding an itinerary with five places each day
        itinerary.add(new TripDay(1, Arrays.asList(
                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),
                new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."),
                new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island.")
//                new ItineraryPlace("Shri Gavi", "ChIJh6gk5PUVrjsRT9dhFc5-nrI", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A vibrant street market."),
//                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai.")

        )));
        itinerary.add(new TripDay(2, Arrays.asList(

                new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island."),
                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),
                new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast.")
//                new ItineraryPlace("Shri Gavi", "ChIJh6gk5PUVrjsRT9dhFc5-nrI", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A vibrant street market."),
//                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai.")

        )));
        itinerary.add(new TripDay(3, Arrays.asList(
                new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."),
                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),

                new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island.")
//                new ItineraryPlace("Shri Gavi", "ChIJh6gk5PUVrjsRT9dhFc5-nrI", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A vibrant street market."),
//                new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai.")

        )));



        // Store each day in the itinerary collection
        for (TripDay day : itinerary) {
            db.collection("readyTrips").document(tripId)
                    .collection("itinerary").document(day.getDayNumber() + "").set(day)
                    .addOnSuccessListener(aVoid -> Log.e("tripadd", "Day itinerary added for " + tripId + " - " + day.getDayNumber()))
                    .addOnFailureListener(e -> Log.e("tripadd", "Error adding itinerary: " + e.getMessage()));
        }
    }


}
