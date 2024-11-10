package com.example.podcatsapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.podcatsapp.R;
import com.example.podcatsapp.model.Category;
import com.example.podcatsapp.model.Publication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadLocalActivity extends AppCompatActivity {
    private static final int VIDEO_CAPTURE_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SIGN_IN = 101;

    private Button recordBtn;
    private Button publishBtn;
    private VideoView videoView;
    private EditText editTextTitle, editTextDescription;
    private ProgressBar progressBar;
    private CountDownTimer timer;

    private FirebaseFirestore db;
    private Drive driveService;

    private Uri videoUri; // To store the captured video URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_local);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        recordBtn = findViewById(R.id.btn_record);
        publishBtn = findViewById(R.id.buttonPublish);
        videoView = findViewById(R.id.videoView);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Drive service
        initializeDriveService();

        recordBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(UploadLocalActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadLocalActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                launchVideoCapture();
            }
        });

        publishBtn.setOnClickListener(view -> {
            if (videoUri != null) {
                uploadVideoToGoogleDrive(videoUri);
            } else {
                Toast.makeText(this, "No video to upload. Record a video first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchVideoCapture() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        startProgressBarTimer();

        startActivityForResult(intent, VIDEO_CAPTURE_REQUEST_CODE);
    }

    private void startProgressBarTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((30000 - millisUntilFinished) / 1000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        };
        timer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURE_REQUEST_CODE && data != null && data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
            if (timer != null) {
                timer.cancel();
            }
            progressBar.setVisibility(View.GONE);
            saveVideoMetadata(videoUri);
        }
    }

    private void saveVideoMetadata(Uri videoUri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, videoUri);

            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Bitmap thumbnail = retriever.getFrameAtTime(0);

            retriever.release();

            // Store metadata and link to Google Drive after upload
            saveMetaDataToFirebase(title, description, durationStr, thumbnail);

        } catch (Exception e) {
            Log.e("UploadLocalActivity", "Error saving video metadata", e);
        }
    }

    private void saveMetaDataToFirebase(String title, String description, String duration, Bitmap thumbnail) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbnailData = baos.toByteArray();

        // Metadata to save
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", title);
        metadata.put("description", description);
        metadata.put("duration", duration);
        metadata.put("thumbnail", thumbnailData);

        db.collection("videos").add(metadata)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Metadata saved in Firebase", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Log.e("UploadLocalActivity", "Error saving metadata", e));
    }

    private void initializeDriveService() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(account.getAccount());
            driveService = new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    new GsonFactory(),
                    credential)
                    .setApplicationName("PodcatsApp")
                    .build();
        } else {
            startActivityForResult(GoogleSignIn.getClient(this,
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                            .build()).getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }

    private void uploadVideoToGoogleDrive(Uri videoUri) {
        try {
            java.io.File filePath = new java.io.File(videoUri.getPath());
            FileContent mediaContent = new FileContent("video/mp4", filePath);

            File fileMetadata = new File();
            fileMetadata.setName("UploadedVideo.mp4");

            driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink")
                    .executeAsync(new FutureCallback<File>() {
                        @Override
                        public void onSuccess(File file) {
                            String videoDriveLink = file.getWebContentLink();
                            updateMetaDataWithDriveLink(videoDriveLink);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("UploadLocalActivity", "Error uploading to Google Drive", t);
                        }
                    });
        } catch (Exception e) {
            Log.e("UploadLocalActivity", "Google Drive upload failed", e);
        }
    }

    private void updateMetaDataWithDriveLink(String videoDriveLink) {
        // Update Firebase document with Google Drive link
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("videoUrl", videoDriveLink);

        db.collection("videos").document(/* ID of the document */)
                .update(updateData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Google Drive link updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Log.e("UploadLocalActivity", "Error updating Google Drive link", e));
    }
}
