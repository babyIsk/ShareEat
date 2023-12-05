package com.example.shareeat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Référence à l'ImageView
        ImageView logoImageView = findViewById(R.id.logoImageView);

        // Animation de l'ImageView
        logoImageView.animate().translationY(-100f).setDuration(1000).start();

        // Handler pour déclencher une action après un délai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Transition vers l'activité principale
                startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
                finish();
                // ou masquage du layout temporaire : findViewById(R.id.yourLayoutId).setVisibility(View.GONE);
            }
        }, 2000); // Délai de 2000 millisecondes (2 secondes)
    }

}