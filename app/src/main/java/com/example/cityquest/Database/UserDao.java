package com.example.cityquest.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cityquest.model.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if user already exists
    void insertUser(User user);

    @Query("SELECT * FROM user_table LIMIT 1") // Fetch the single user
    User getUser();

    @Update
    void updateUser(User user);

    @Query("DELETE FROM user_table") // Clear user data on logout
    void deleteUser();
}
