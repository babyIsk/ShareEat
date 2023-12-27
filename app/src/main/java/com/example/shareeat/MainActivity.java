package com.example.shareeat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button redirection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        redirection = (Button) findViewById(R.id.btnRedirection);

        redirection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Redirection vers formulaire post un plat
                Intent intent = new Intent(MainActivity.this, AddPlatActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}