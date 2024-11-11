package com.example.podcatsv2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.podcatsv2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private TextView usernameTextView;
    private SharedPreferences sharedPreferences;
    private static final String DARK_MODE_KEY = "dark_mode_enabled";
    private static final String USERNAME_KEY = "username";
    private static final String LANGUAGE_KEY = "language_code";
    private ImageView logOut, imgbuttonBack;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences and theme
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        String languageCode = sharedPreferences.getString(LANGUAGE_KEY, "en"); // Default language

        // Update the app locale before setting the content view
        updateLocale(languageCode);
        updateTheme(isDarkMode);

        setContentView(R.layout.activity_settings);

        // Initialize views
        logOut = findViewById(R.id.btn_logout);
        imgbuttonBack = findViewById(R.id.back_button);
        radioGroup = findViewById(R.id.lang_rg);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        usernameTextView = findViewById(R.id.username);

        // Set initial dark mode switch state
        switchDarkMode.setChecked(isDarkMode);
        setupSwitchTint(switchDarkMode);

        // Set the initial language state in the RadioGroup
        setInitialLanguageSelection(languageCode);

        // Dark mode switch listener
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSwitchThumbColor(switchDarkMode, isChecked);
            saveDarkModePreference(isChecked);
            updateTheme(isChecked);
            recreate(); // Recreate the activity to apply the theme
        });

        // Back button listener
        imgbuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // Load username from SharedPreferences
        loadUsername();

        // Log out button listener
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        // Initialize bottom navigation
        initializeBottomNavigation();

        // Language change listener for RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedLanguageCode = "en"; // Default to English

            if (checkedId == R.id.rb_eus) {
                selectedLanguageCode = "eu"; // Euskera
            } else if (checkedId == R.id.rb_eng) {
                selectedLanguageCode = "en"; // English
            } else if (checkedId == R.id.rb_es) {
                selectedLanguageCode = "es"; // Spanish
            }

            // Check if the selected language is different from the current one
            String currentLanguageCode = sharedPreferences.getString(LANGUAGE_KEY, "en");
            if (!currentLanguageCode.equals(selectedLanguageCode)) {
                saveLanguagePreference(selectedLanguageCode);
                updateLocale(selectedLanguageCode);
                recreate(); // Recreate activity to apply language changes
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsername();
        // Ensure the correct language is selected in the RadioGroup after resume
        String languageCode = sharedPreferences.getString(LANGUAGE_KEY, "en");
        setInitialLanguageSelection(languageCode);
    }

    private void loadUsername() {
        String username = sharedPreferences.getString(USERNAME_KEY, "Default Username");
        usernameTextView.setText(username);
    }

    private void setupSwitchTint(Switch switchComponent) {
        setSwitchThumbColor(switchComponent, switchComponent.isChecked());
    }

    private void setSwitchThumbColor(Switch switchComponent, boolean isChecked) {
        if (isChecked) {
            switchComponent.getThumbDrawable().setTint(Color.parseColor("#FF007A")); // Pink
        } else {
            switchComponent.getThumbDrawable().setTint(Color.parseColor("#424242")); // Gray
        }
    }

    private void saveDarkModePreference(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DARK_MODE_KEY, isDarkMode);
        editor.apply();
    }

    private void updateTheme(boolean isDarkMode) {
        int defaultNightMode = isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(defaultNightMode);
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void setInitialLanguageSelection(String languageCode) {
        if ("eu".equals(languageCode)) {
            radioGroup.check(R.id.rb_eus);
        } else if ("es".equals(languageCode)) {
            radioGroup.check(R.id.rb_es);
        } else {
            radioGroup.check(R.id.rb_eng);
        }
    }

    private void initializeBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_video) {
                startActivity(new Intent(this, UploadVideosActivity.class));
                return true;
            } else if (itemId == R.id.nav_feed) {
                startActivity(new Intent(this, VideoFeedActivity.class));
                return true;
            } else if (itemId == R.id.nav_audio) {
                startActivity(new Intent(this, UploadAudioActivity.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                return true;
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
    }
    public void editProfile(View view) {
        Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void changePassword(View view) {
        // Navegar a la actividad ChangePasswordActivity
        Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, VideoFeedActivity.class);
        startActivity(intent);
        finish();
    }
}
