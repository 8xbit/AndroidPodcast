package com.example.podcatsapp.model;

import android.media.Image;

public class Publication {

    private String title;
    private String description;
    private String image;
    private String audio;
    private String video;
    private boolean like;
    private Category category;

    public Publication(String title, String description, String image, String audio,String video, boolean like, Category category) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.audio = audio;
        this.video = video;
        this.like = like;
        this.category = category;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
