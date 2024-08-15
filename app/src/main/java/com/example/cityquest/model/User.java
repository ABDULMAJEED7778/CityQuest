package com.example.cityquest.model;

public class User {

    private String userId;
    private String userName;
    private String email;
//    private String phoneNumber;
    private String profilePictureUrl;
//    private List<Trip> trips;
//    private Map<String, String> preferences; User preferences such as travel destinations, types of trips, etc.
   // private List<Location> savedLocations;
   // private String contactInformation;
   // private String address;
  //  private String dateOfBirth;
    //private String userType;



    public User() {
    }
    public User(String userId, String userName, String email, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;}

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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
