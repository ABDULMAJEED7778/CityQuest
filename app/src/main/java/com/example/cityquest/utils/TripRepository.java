package com.example.cityquest.utils;

import android.util.Log;

import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.Database.ReadyTripsDao;
import com.example.cityquest.model.ReadyTrips;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TripRepository {//TODO Stop Listener When Not Needed
    private final ReadyTripsDao readyTripsDao;
    private final FirebaseFirestore db;

    public TripRepository(ReadyTripsDao dao, FirebaseFirestore firestore) {
        this.readyTripsDao = dao;
        this.db = firestore;
    }

    public void startListeningForTripUpdates(String userId) {
        db.collection("users").document(userId).collection("trips")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Log.e("FirestoreListener", "Error listening for updates", error);
                        return;
                    }

                    if (snapshots != null) {
                        List<ReadyTrips> updatedTrips = new ArrayList<>();
                        List<String> firestoreIds = new ArrayList<>();

                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            ReadyTrips trip = doc.toObject(ReadyTrips.class);
                            if (trip != null) {
                                updatedTrips.add(trip);
                                firestoreIds.add(trip.getTripId());
                            }
                        }

                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            readyTripsDao.insertOrUpdateTrips(updatedTrips);
                            readyTripsDao.deleteTripsNotInFirestore(firestoreIds);
                        });
                    }
                });
    }
}
