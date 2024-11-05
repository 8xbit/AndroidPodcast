package com.example.podcatsapp.view;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.podcatsapp.R;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;
    private static final String DARK_MODE_KEY = "dark_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Set the theme before setting content view
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        updateTheme(isDarkMode);

        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switch_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        // Set initial switch state based on saved preference
        switchDarkMode.setChecked(isDarkMode);

        // Initialize the thumb tint colors
        setupSwitchTint(switchNotifications);
        setupSwitchTint(switchDarkMode);

        // Set listeners to change thumb tint on switch toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSwitchThumbColor(switchNotifications, isChecked);
            // Add additional notification logic here if needed
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSwitchThumbColor(switchDarkMode, isChecked);
            saveDarkModePreference(isChecked);
            updateTheme(isChecked);
            recreate(); // Recreate the activity to apply the theme
        });
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

    public void editProfile(View view) {
        // code to edit profile
    }

    public void changePassword(View view) {
        // code to change password
    }
}