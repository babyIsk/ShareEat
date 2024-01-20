package com.example.shareeat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button redirection;
    Button redirect2;
    Button redirect3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        redirection = (Button) findViewById(R.id.btnRedirection);
        redirect2 = (Button) findViewById(R.id.btnRedirection2);
        redirect3 = (Button) findViewById(R.id.btnRedirection3);

        redirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirection vers formulaire post un plat
                Intent intent = new Intent(MainActivity.this, AddPlatActivity.class);
                startActivity(intent);
                finish();
            }
        });



        redirect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirection vers la messagerie
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        redirect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirection vers le profil
                Intent intent = new Intent(MainActivity.this, ProfilGaleryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}