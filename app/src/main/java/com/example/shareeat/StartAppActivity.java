package com.example.shareeat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class StartAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Référence à l'ImageView
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);

        //Animation du background
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        // Handler pour déclencher une action après un délai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Transition vers l'activité principale
                startActivity(new Intent(StartAppActivity.this, ConnexionActivity.class));
                finish();
                // ou masquage du layout temporaire : findViewById(R.id.yourLayoutId).setVisibility(View.GONE);
            }
        }, 5000); // Délai de 2000 millisecondes (2 secondes)
    }
}
