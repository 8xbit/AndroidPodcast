package com.example.podcatsapp.view;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.podcatsapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;
    private List<MediaItem> mediaItems; // List of videos

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Find PlayerView in the layout
        playerView = findViewById(R.id.playerView);

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Initialize the list of videos
        mediaItems = new ArrayList<>();
        mediaItems.add(MediaItem.fromUri(getVideoUri(R.raw.videoprueba)));
        mediaItems.add(MediaItem.fromUri(getVideoUri(R.raw.videoprueba2)));
        mediaItems.add(MediaItem.fromUri(getVideoUri(R.raw.videoprueba3)));

        // Set the media items for playback
        player.setMediaItems(mediaItems);
        player.prepare();

        // Automatically start playback
        player.play();
    }

    // Method to get the URI of the video
    private Uri getVideoUri(int resourceId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + resourceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
