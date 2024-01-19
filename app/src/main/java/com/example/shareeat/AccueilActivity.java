package com.example.shareeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shareeat.AddPlatActivity;
import com.example.shareeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccueilActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.nav_home){
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddPlatActivity.class));
                    return true;
                }

                return false;
            }
        });
    }

}
