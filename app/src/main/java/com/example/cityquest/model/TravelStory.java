package com.example.cityquest.model;



import com.google.firebase.Timestamp;

import java.util.List;

public class TravelStory {

    private String userName; // Name of the person who posted
    private String userId; // ID of the person who posted
    private String postId; // ID of the post
    private Timestamp datePosted; // Date of the post
    private String title; // Title of the story
    private String description; // Description of the story
    private List<String> photoUrls; // List of 5 photo URLs
    private String videoUrl; // URL of the video
    private String placeId; // Place related to the story
    private int views; // Number of views
    private int likes; // Number of like
    private int comments; // Number of comments
    private String userProfilePicture; // URL of the user's profile picture

    private String date;
    // Constructor

    public TravelStory() {
    }

    public TravelStory(String userName,String date, String postId, String title, List<String> photoUrls, String videoUrl, String placeId, int views, int likes, int comments, String userProfilePicture) {
        this.userName = userName;
        this.postId = postId;
        this.title = title;
        this.photoUrls = photoUrls;
        this.videoUrl = videoUrl;
        this.placeId = placeId;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
        this.date = date;
        this.userProfilePicture = userProfilePicture;
    }

    public TravelStory(String userName, String userId, String postId, Timestamp datePosted, String title, String description, List<String> photoUrls, String videoUrl, String placeId, int views, int likes, int comments, String userProfilePicture) {
        this.userName = userName;
        this.userId = userId;
        this.postId = postId;
        this.datePosted = datePosted;
        this.title = title;
        this.description = description;
        this.photoUrls = photoUrls;
        this.videoUrl = videoUrl;
        this.placeId = placeId;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
        this.userProfilePicture = userProfilePicture;
    this.userProfilePicture = userProfilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Timestamp datePosted) {
        this.datePosted = datePosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }
}
