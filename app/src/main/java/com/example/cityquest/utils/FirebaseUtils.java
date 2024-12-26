package com.example.cityquest.utils;

import com.example.cityquest.model.Trip;
import com.example.cityquest.model.Trips;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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

    public static CollectionReference getPostsCollection() {
        return db.collection("posts");
    }

    public static CollectionReference getCitiesCollection() {
        return db.collection("cities");
    }

    // Get reference to a specific user document
    public static DocumentReference getUserDocument(String userId) {
        return getUsersCollection().document(userId);
    }

    public static void getUserData(String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        getUserDocument(userId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static DocumentReference getMyTripsDocument(String userId,String TripId) {
        return getUsersCollection().document(userId).collection("mytrips").document(TripId);

    }
    public static CollectionReference getLikedPostsCollection(String userId) {
        return getUsersCollection().document(userId).collection("myLikedPosts");

    }

    public static void getUserLikedPosts(String userId, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        getLikedPostsCollection(userId)
                .get()
                .addOnCompleteListener(onCompleteListener);
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

    public static void addTrip(Trips trip, OnCompleteListener<DocumentReference> onCompleteListener) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            getTripsCollection(userId).add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Call the listener with the DocumentReference
                    onCompleteListener.onComplete(task);
                } else {
                    // Handle error
                    onCompleteListener.onComplete(null);
                }
            });
        } else {
            // Handle case when user is not logged in
            onCompleteListener.onComplete(null);
        }
    }


}
