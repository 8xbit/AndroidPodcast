package com.example.podcatsapp.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String userId;
    private String displayName;
    private String email;
    private String photoUrl;
    private Map<String, Publication> videos;
    // Required for Firebase
    public User() {}

    public User(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.videos = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public Map<String, Publication> getVideos() {
        return videos;
    }

    public void setVideos(Map<String, Publication> videos) {
        this.videos = videos;
    }
}
