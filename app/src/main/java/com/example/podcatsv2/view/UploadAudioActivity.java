package com.example.podcatsv2.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.podcatsv2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UploadAudioActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button btnRecord;
    private ListView listRecordings;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String recordingFilePath;
    private boolean isRecording = false;
    private ArrayList<String> recordings;
    private ArrayAdapter<String> recordingsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);



        // Initialize views
        btnRecord = findViewById(R.id.btnRecord);
        listRecordings = findViewById(R.id.listRecordings);

        // Initialize recordings list
        recordings = new ArrayList<>();
        recordingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordings);
        listRecordings.setAdapter(recordingsAdapter);

        // Check and request permissions
        if (!checkPermissions()) {
            requestPermissions();
        }

        // Set up record button click listener
        btnRecord.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
        });

        // Set up list item click listener for playback
        listRecordings.setOnItemClickListener((parent, view, position, id) -> {
            playRecording(recordings.get(position));
        });

        // Load existing recordings
        loadRecordings();
        initializeBottomNavigation();

    }

    private boolean checkPermissions() {
        int recordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return recordPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    private void startRecording() {
        // Create file for recording
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "Recording_" + timestamp + ".mp3";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(storageDir, fileName);
        recordingFilePath = file.getAbsolutePath();

        // Initialize MediaRecorder
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(recordingFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            btnRecord.setText("Stop Recording");
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Recording Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            btnRecord.setText("Start Recording");
            loadRecordings(); // Refresh the list
            Toast.makeText(this, "Recording Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecordings() {
        recordings.clear();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (storageDir != null) {
            File[] files = storageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".mp3")) {
                        recordings.add(file.getAbsolutePath());
                    }
                }
            }
        }
        recordingsAdapter.notifyDataSetChanged();
    }

    private void playRecording(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

            Toast.makeText(this, "Playing Recording", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Playback Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Method to set up the Bottom Navigation
    private void initializeBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_video) {
                // Already on Feed, do nothing
                Intent audioIntent = new Intent(UploadAudioActivity.this, UploadVideosActivity.class);
                startActivity(audioIntent);
                return true;
            } else if (itemId == R.id.nav_feed) {
                // Navigate to UploadVideosActivity
                Intent uploadIntent = new Intent(UploadAudioActivity.this, VideoFeedActivity.class);
                startActivity(uploadIntent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                // Navigate to SettingsActivity
                Intent settingsIntent = new Intent(UploadAudioActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            } else if (itemId == R.id.nav_audio) {
                // Navigate to SettingsActivity

                return true;
            }

            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_audio);

    }








    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UploadAudioActivity.this, VideoFeedActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }


}// activity








