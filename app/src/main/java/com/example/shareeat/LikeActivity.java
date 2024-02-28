package com.example.shareeat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shareeat.adapter.RecetteLikeAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.List;

public class LikeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Utilisateur user;
    private GridView gridView;
    private ImageButton profil;
    private ConnexionBD connexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        user = UserDataSingleton.getInstance().getUtilisateur();
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        gridView = findViewById(R.id.recettes);
        profil = findViewById(R.id.toolbar_button);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, ProfilGaleryActivity.class);
                startActivity(intent);
            }
        });

        if (user != null) {
            try {
                connexionBD = new ConnexionBD();
                if (connexionBD != null) {
                    List<Plat> plats = connexionBD.getPlatLike(user.getIdUtilisateur());
                    if (plats != null) {
                        RecetteLikeAdapter recetteLikeAdapter = new RecetteLikeAdapter(this, plats, user);
                        gridView.setAdapter(recetteLikeAdapter);

                    } else {
                        Log.d("PlatDebug", "La liste de plats est null");
                    }
                } else {
                    Log.d("PlatDebug", "La connexionBD est null");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Gérez le cas où l'objet user est null
            Log.d("PlatDebug", "L'objet user est null");
            // Affichez un message d'erreur ou prenez une action appropriée
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddPlatActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_message) {
                    startActivity(new Intent(getApplicationContext(), MPActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_favourites) {
                    startActivity(new Intent(getApplicationContext(), LikeActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
