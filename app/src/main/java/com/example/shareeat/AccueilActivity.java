package com.example.shareeat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shareeat.AddPlatActivity;
import com.example.shareeat.R;
import com.example.shareeat.adapter.RecetteAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Utilisateur user;
    private ListView listView;
    private ConnexionBD connexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        user = UserDataSingleton.getInstance().getUtilisateur();
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        listView = findViewById(R.id.recettes);

        if (user != null) {
            try {
                connexionBD = new ConnexionBD();
                if (connexionBD != null) {
                    List<Plat> plats = connexionBD.getRecetteAccueil(user.getIdUtilisateur());
                    if (plats != null) {
                        RecetteAdapter recetteAdapter = new RecetteAdapter(this, plats, user);
                        listView.setAdapter(recetteAdapter);
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
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddPlatActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_message) {
                    startActivity(new Intent(getApplicationContext(), MPActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
