package com.example.cityquest.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "user_table")

public class User {

    @PrimaryKey
    @NonNull
    private String userId;
    private String userName;
    private String email;
    private int followersCount;
    private int followingCount;
    private int postCount;
//    private String phoneNumber;
    private String profilePictureUrl;
//    private List<Trip> trips;
//    private Map<String, String> preferences; User preferences such as travel destinations, types of trips, etc.
   // private List<Location> savedLocations;
   // private String contactInformation;
   // private String address;
  //  private String dateOfBirth;
    //private String userType;
    @Ignore // Room will ignore this field
    private List<String> followers; // IDs of followers
    @Ignore // Room will ignore this field
    private List<String> following; // IDs of following



    public User() {
    }

    @Ignore
    public User(String userId, String userName, String email, int followersCount, int followingCount, int postCount, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.postCount = postCount;
        this.profilePictureUrl = profilePictureUrl;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
