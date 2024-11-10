package com.example.podcatsapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.podcatsapp.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUsername;
    private ImageView profileImage;
    private TextView usernameDisplay;
    private SharedPreferences sharedPreferences;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String USERNAME_KEY = "username";
    private static final String PROFILE_IMAGE_URI_KEY = "profile_image_uri";
    private static final String DARK_MODE_KEY = "dark_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        updateTheme(isDarkMode);

        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editUsername = findViewById(R.id.edit_username);
        profileImage = findViewById(R.id.profile_image);
        usernameDisplay = findViewById(R.id.username); // Inicializar el TextView para mostrar el nombre

        loadUserProfile();

        profileImage.setOnClickListener(v -> openImagePicker());
        findViewById(R.id.save_button).setOnClickListener(v -> saveChanges());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameDisplay();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);
            profileImage.setTag(selectedImageUri); // Store URI as a tag for later retrieval
        }
    }

    private void loadUserProfile() {
        String username = sharedPreferences.getString(USERNAME_KEY, "Default Username");
        String profileImageUri = sharedPreferences.getString(PROFILE_IMAGE_URI_KEY, null);

        editUsername.setText(username);
        usernameDisplay.setText(username); // Muestra el nombre de usuario en el TextView

        if (profileImageUri != null) {
            profileImage.setImageURI(Uri.parse(profileImageUri));
        }
    }

    private void updateUsernameDisplay() {
        // Actualiza el TextView con el nombre de usuario actual en preferencias
        String username = sharedPreferences.getString(USERNAME_KEY, "Default Username");
        usernameDisplay.setText(username);
    }

    private void saveChanges() {
        String username = editUsername.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);

        Uri imageUri = (Uri) profileImage.getTag();
        if (imageUri != null) {
            editor.putString(PROFILE_IMAGE_URI_KEY, imageUri.toString());
        }

        editor.apply();
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateTheme(boolean isDarkMode) {
        int defaultNightMode = isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(defaultNightMode);
    }
}
