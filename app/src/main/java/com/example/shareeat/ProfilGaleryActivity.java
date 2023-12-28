package com.example.shareeat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilGaleryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_galery);
        getSupportActionBar().hide();
    }
}
