package com.example.podcatsapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.podcatsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

       // switchNotifications = findViewById(R.id.switch_notifications);
       // switchDarkMode = findViewById(R.id.switch_dark_mode);

     /*
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // codigo notificacines
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // codigo modo oscuro
        });*/
    }

    public void editProfile(View view) {
        // codigo editar perfil
    }

    public void changePassword(View view) {
        // codigo cambiar contraseña
    }
}
