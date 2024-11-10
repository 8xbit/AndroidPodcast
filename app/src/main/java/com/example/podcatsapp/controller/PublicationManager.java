package com.example.podcatsapp.controller;

import android.net.Uri;
import android.util.Log;

import com.example.podcatsapp.model.Category;
import com.example.podcatsapp.model.Publication;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PublicationManager {
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    public PublicationManager() {
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    // Upload a new publication (video)
    public Task<Publication> uploadPublication(Uri videoUri, String title,
                                               String description, Category category) {
        // Ensure user is logged in
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return Tasks.forException(new Exception("User not logged in"));
        }

        // Create publication ID
        String publicationId = database.getReference("publications").push().getKey();
        StorageReference videoRef = storage.getReference()
                .child("videos")
                .child(user.getUid())
                .child(publicationId + ".mp4");

        // Create publication object
        Publication publication = new Publication();
        publication.setVideoId(publicationId);
        publication.setTitle(title);
        publication.setDescription(description);
        publication.setCategory(category);
        publication.setUserId(user.getUid());
        publication.setUploadDate(System.currentTimeMillis());
        publication.setLike(false);

        // Start upload process
        return videoRef.putFile(videoUri)
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) /
                            snapshot.getTotalByteCount();
                    // You can create a method to update UI with progress
                    Log.d("Upload", "Progress: " + progress + "%");
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Get download URL
                    return videoRef.getDownloadUrl();
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Set video URL and save to database
                    publication.setVideoUrl(task.getResult().toString());
                    return database.getReference("publications")
                            .child(user.getUid())
                            .child(publicationId)
                            .setValue(publication);
                })
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return publication;
                });
    }

    // Get publications by category
    public Task<List<Publication>> getPublicationsByCategory(Category category) {
        return database.getReference("publications")
                .orderByChild("category")
                .equalTo(category.toString())
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    List<Publication> publications = new ArrayList<>();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        Publication pub = snapshot.getValue(Publication.class);
                        if (pub != null) {
                            publications.add(pub);
                        }
                    }
                    return publications;
                });
    }

    // Toggle like
    public Task<Void> toggleLike(String publicationId) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return Tasks.forException(new Exception("User not logged in"));
        }

        DatabaseReference likeRef = database.getReference("likes")
                .child(publicationId)
                .child(user.getUid());

        return likeRef.get()
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    boolean currentLike = task.getResult().getValue(Boolean.class) != null;
                    if (currentLike) {
                        return likeRef.removeValue();
                    } else {
                        return likeRef.setValue(true);
                    }
                });
    }

    // Delete publication
    public Task<Void> deletePublication(Publication publication) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null || !user.getUid().equals(publication.getUserId())) {
            return Tasks.forException(new Exception("Unauthorized"));
        }

        StorageReference videoRef = storage.getReferenceFromUrl(publication.getVideoUrl());

        return videoRef.delete()
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return database.getReference("publications")
                            .child(publication.getUserId())
                            .child(publication.getVideoId())
                            .removeValue();
                });
    }
}