package com.example.cityquest.model;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Comment {

    private String commentId;
    private String username; // The username of the person who commented
    private Timestamp timePosted; // The timestamp of when the comment was posted
    private String content; // The text content of the comment
    private int likeCount; // The number of likes on the comment
    private int replyCount; // The number of replies to the comment
    private List<Comment> replies; // A list of replies (nested comments)
    private String profileImageUrl; // URL of the profile image of the commenter
    private boolean isReply; // NEW: To differentiate comments and replies
    private boolean isExpanded = false; // Add this flag to handle the expansion state


    // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    public Comment() {}

    // Parameterized constructor

    //for Replay
    public Comment( String username,  String content,  String profileImageUrl, boolean isReply) {
        this.username = username;
        this.content = content;
        this.profileImageUrl = profileImageUrl;
        this.isReply = isReply;
    }


    public Comment(String commentId, String username, Timestamp timePosted, String content, int likeCount, int replyCount, String profileImageUrl, boolean isReply, boolean isExpanded) {
        this.commentId = commentId;
        this.username = username;
        this.timePosted = timePosted;
        this.content = content;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.replies = new ArrayList<>();
        this.profileImageUrl = profileImageUrl;
        this.isReply = isReply;
        this.isExpanded = isExpanded;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean getIsReply() {
        return isReply;
    }

    public void setIsReply(boolean isReply) {
        this.isReply = isReply;
    }
    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
        this.timePosted = timePosted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {

        this.replies = replies;

    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", replies=" + replies +
                '}';
    }
}
