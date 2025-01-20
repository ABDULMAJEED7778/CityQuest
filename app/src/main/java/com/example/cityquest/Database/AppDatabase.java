package com.example.cityquest.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ReadyTrips.class, User.class}, version = 5,exportSchema = false)
@TypeConverters(Converters.class) // Register TypeConverter

public abstract class AppDatabase extends RoomDatabase {

    public abstract ReadyTripsDao readyTripsDao();
    public abstract UserDao userDao(); // Add UserDao


    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4; // For running multiple tasks concurrently
   public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public  static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "cityquest_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

