package com.example.podcatsapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.podcatsapp.R;
import com.example.podcatsapp.controller.PublicationsAdapter;
import com.example.podcatsapp.model.Category;
import com.example.podcatsapp.model.Publication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvPublications = findViewById(R.id.rec_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inicializa las publicaciones con audio y video
        ArrayList<Publication> pubList = new ArrayList<>();
        pubList.add(new Publication("Publication 1", "Description 1", "url_image","url_audio","url_video", true, Category.PODCAST));
        pubList.add(new Publication("Publication 2", "Description 2", "url_image","url_audio","url_video", true, Category.VIDEO));
        // Agrega más publicaciones según sea necesario...

        // Crea el adaptador y lo asigna al RecyclerView
        PublicationsAdapter adapter = new PublicationsAdapter(this, pubList);
        rvPublications.setAdapter(adapter);
        rvPublications.setLayoutManager(new LinearLayoutManager(this));

        // Configura el Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_home) {
                return true; // Ya estamos en Home
            } else if (id == R.id.item_videos || id == R.id.item_upload) {
                // Si se selecciona "Videos" o "Upload"
                startActivity(new Intent(MainActivity.this, PlayerActivity.class));
                return true;
            } else if (id == R.id.item_setting) {
                // Si se selecciona "Settings"
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false; // Si no coincide ningún item
        });

        bottomNavigationView.setSelectedItemId(R.id.item_home);
    }
}
