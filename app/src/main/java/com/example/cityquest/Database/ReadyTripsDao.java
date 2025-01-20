package com.example.cityquest.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cityquest.model.ReadyTrips;

import java.util.List;

@Dao
public interface ReadyTripsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // This will replace the trip if it exists
    void insertTrip(ReadyTrips readyTrip);

    @Query("SELECT * FROM ready_trips")
    List<ReadyTrips> getAllTrips();

    @Update
    void updateTrip(ReadyTrips trip);

    @Query("SELECT * FROM ready_trips WHERE tripId = :tripId")
    ReadyTrips getTripById(String tripId);

    @Delete
    void deleteTrip(ReadyTrips readyTrip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateTrips(List<ReadyTrips> trips);

    @Query("SELECT * FROM ready_trips WHERE synced = 0")
    List<ReadyTrips> getUnsyncedTrips(); // Retrieve unsynced trips for syncing


    @Query("DELETE FROM ready_trips WHERE tripId NOT IN (:firestoreIds)")
    void deleteTripsNotInFirestore(List<String> firestoreIds);

}
