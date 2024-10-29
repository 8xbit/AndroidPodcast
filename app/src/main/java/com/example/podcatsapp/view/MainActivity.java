package com.example.podcatsapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
        // Lookup the recyclerview in activity layout
        RecyclerView rvPublications = (RecyclerView) findViewById(R.id.rec_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize contacts
        ArrayList<Publication>  pubList= new ArrayList<>();
        //( title,  description, Image image, boolean like, Category category)
        Publication p1 = new Publication("Publication 1","Descriptions1",R.drawable.cat1,true, Category.PODCAST);
        Publication p2 = new Publication("Publication 2","Descriptions2",R.drawable.cat2,true, Category.VIDEO);
        Publication p3 = new Publication("Publication 3","Descriptions3",R.drawable.cat3,true, Category.VIDEO);
        Publication p4 = new Publication("Publication 4","Descriptions4",R.drawable.cat3,true, Category.PODCAST);
        Publication p5= new Publication("Publication 4","Descriptions5",R.drawable.cat3,true, Category.PODCAST);
        Publication p6 = new Publication("Publication 4","Descriptions6",R.drawable.cat3,true, Category.PODCAST);
        Publication p7 = new Publication("Publication 4","Descriptions7",R.drawable.cat3,true, Category.PODCAST);
        Publication p8 = new Publication("Publication 4","Descriptions8",R.drawable.cat3,true, Category.PODCAST);
        Publication p9 = new Publication("Publication 4","Descriptions9",R.drawable.cat3,true, Category.PODCAST);
        Publication p10 = new Publication("Publication 4","Descriptions10",R.drawable.cat3,true, Category.PODCAST);
        pubList.add(p1);
        pubList.add(p2);
        pubList.add(p3);
        pubList.add(p4);
        pubList.add(p5);
        pubList.add(p6);
        pubList.add(p7);
        pubList.add(p10);
        pubList.add(p8);
        pubList.add(p9);
        pubList.add(p10);

        // Create adapter passing in the sample user data
        PublicationsAdapter adapter = new PublicationsAdapter(pubList);
        // Attach the adapter to the recyclerview to populate items
        rvPublications.setAdapter(adapter);
        // Set layout manager to position the items
        rvPublications.setLayoutManager(new LinearLayoutManager(this));




        // Bottom menu
        // why is not working with swich case ?
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_home) {
                return true; // Already on Home
            } else if (id == R.id.item_videos) {
             Intent   intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.item_upload) {
                Intent   intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
                return true;
            }else if (id== R.id.item_setting) {
                Intent  intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false; // If no item matches, return false
        });


        bottomNavigationView.setSelectedItemId(R.id.item_home);




    }//
}// class