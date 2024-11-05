package com.example.podcatsapp.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class Publication implements Serializable {

    private String title;
    private String description;
    private String image;
    private String audio;
    private String video;
    private boolean like;
    private Category category;

    public Publication(String title, String description, String image, String audio, String video, boolean like, Category category) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.audio = audio;
        this.video = video;
        this.like = like;
        this.category = category;
    }

    // Métodos Getter y Setter (sin cambios)
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Métodos para cargar los recursos desde las URLs

    // Cargar la imagen usando Glide
    public static void loadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
    }

    // Reproducir audio desde la URL usando MediaPlayer
    public static void playAudioFromUrl(Context context, String audioUrl) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reproducir video desde la URL usando VideoView
    public static void playVideoFromUrl(Context context, String videoUrl, VideoView videoView) {
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}
