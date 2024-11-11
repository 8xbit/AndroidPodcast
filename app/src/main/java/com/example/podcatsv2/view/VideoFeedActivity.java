// File: VideoFeedActivity.java
package com.example.podcatsv2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.podcatsv2.R;
import com.example.podcatsv2.models.Video;
import com.example.podcatsv2.adapters.VideoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoFeedActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener {
    private RecyclerView recyclerViewVideos;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;
    private FirebaseAuth mAuth;
    private DatabaseReference videosRef, likesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.podcatsv2.R.layout.activity_video_feed);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        videosRef = FirebaseDatabase.getInstance().getReference("Videos");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");

        // Initialize RecyclerView
        recyclerViewVideos = findViewById(com.example.podcatsv2.R.id.recyclerViewVideos);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoList, this); // Pass 'this' as the listener
        recyclerViewVideos.setAdapter(videoAdapter);

        // Fetch videos from Firebase
        fetchVideos();

        // Initialize Bottom Navigation
        initializeBottomNavigation();
    }

    private void initializeBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(com.example.podcatsv2.R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(com.example.podcatsv2.R.id.nav_feed); // Set current selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == com.example.podcatsv2.R.id.nav_feed) {
                // Already on Feed, do nothing
                return true;
            } else if (itemId == com.example.podcatsv2.R.id.nav_video) {
                // Navigate to UploadVideosActivity
                Intent uploadIntent = new Intent(VideoFeedActivity.this, UploadVideosActivity.class);
                startActivity(uploadIntent);
                finish();
                return true;
            } else if (itemId == com.example.podcatsv2.R.id.nav_settings) {
                // Navigate to SettingsActivity
                Intent settingsIntent = new Intent(VideoFeedActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                finish();

                return true;
            } else if (itemId == com.example.podcatsv2.R.id.nav_audio) {
                // Navigate to SettingsActivity
                Intent audioIntent = new Intent(VideoFeedActivity.this, UploadAudioActivity.class);
                startActivity(audioIntent);
                finish();

                return true;
            }

            return false;
        });
    }

    private void fetchVideos() {
        videosRef.orderByChild("publicationTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                videoList.clear();
                for (DataSnapshot videoSnapshot : snapshot.getChildren()) {
                    Video video = videoSnapshot.getValue(Video.class);
                    if (video != null) {
                        video.setVideoId(videoSnapshot.getKey());
                        //CreateList of all videos in db
                        videoList.add(video);
                    }
                }
                // Reverse list of videos to see last first
                Collections.reverse(videoList);

                //testing if will refresh or no
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(VideoFeedActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Video video) {
        String videoUrl = video.getUrl();
        if (videoUrl == null || videoUrl.trim().isEmpty()) {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, videoUrl); // Use the constant key
        startActivity(intent);
    }
}
