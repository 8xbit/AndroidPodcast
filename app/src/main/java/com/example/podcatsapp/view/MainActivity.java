package com.example.podcatsapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.podcatsapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Encuentra el botón y configura el evento de clic
        Button openPlayerButton = findViewById(R.id.openPlayerButton);
        openPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad Player
                Intent intent = new Intent(MainActivity.this, Player.class);
                startActivity(intent);
            }
        });
    }
}
