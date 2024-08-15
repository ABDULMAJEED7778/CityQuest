package com.example.cityquest.utils;

import com.example.cityquest.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirebaseUtils {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get current user
    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Sign out
    public static void signOut() {
        auth.signOut();
    }

    // Get reference to users collection
    public static CollectionReference getUsersCollection() {
        return db.collection("users");
    }

    // Get reference to a specific user document
    public static DocumentReference getUserDocument(String userId) {
        return getUsersCollection().document(userId);
    }

    // Get reference to trips collection for a specific user
    public static CollectionReference getTripsCollection(String userId) {
        return getUserDocument(userId).collection("trips");
    }

    // Add a new trip for the current user
//    public static void addTrip(String userId, Trip trip, OnCompleteListener<Void> onCompleteListener) {
//        getTripsCollection(userId).add(trip).addOnCompleteListener(onCompleteListener);
//    }

    // Query trips by date range
    public static Query getTripsByDateRange(String userId, long startDate, long endDate) {
        return getTripsCollection(userId)
                .whereGreaterThanOrEqualTo("startDate", startDate)
                .whereLessThanOrEqualTo("endDate", endDate);
    }

    // Other Firebase-related utility methods can be added here...
}
