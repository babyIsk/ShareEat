package com.example.shareeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shareeat.adapter.MPAdaptater;
import com.example.shareeat.adapter.MessageAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MPActivity extends AppCompatActivity {



    private ExecutorService executorService;
    private Handler handler;
    private BottomNavigationView bottomNavigationView;
    private Utilisateur user;
    private ListView listView;
    private ConnexionBD connexionBD;
    private List<Utilisateur> userList;
    private MPAdaptater mpAdaptater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp);


        user = UserDataSingleton.getInstance().getUtilisateur();
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_message);
        listView = findViewById(R.id.mps);

        userList = new ArrayList<>();
        mpAdaptater = new MPAdaptater(this, userList, user);

        listView.setAdapter(mpAdaptater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user from the list
                Utilisateur selectedUser = userList.get(position);

                // Open the MessageActivity for the selected user
                Intent intent = new Intent(MPActivity.this, MessageActivity.class);
                intent.putExtra("utilisateurId", selectedUser.getIdUtilisateur());
                startActivity(intent);
                finish();
            }
        });


        ViewTreeObserver viewTreeObserver = listView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Supprimez le listener pour éviter d'appeler à plusieurs reprises
                listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Faites défiler la ListView vers le bas
                listView.smoothScrollToPosition(mpAdaptater.getCount() - 1);
            }
        });

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        rechercheMessage();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddPlatActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_message) {
                    return true;
                }
                return false;
            }
        });
    }




    public void rechercheMessage() {
        executorService.execute(() -> {
            try {
                ConnexionBD bd = new ConnexionBD();
                while (true) {
                    List<Utilisateur> contacts = bd.getMP(user.getIdUtilisateur());
                    // Mettre à jour l'interface utilisateur sur le thread principal
                    handler.post(() -> {
                        // Ajouter les nouveaux messages à la liste
                        userList.clear();
                        userList.addAll(contacts);
                        // Rafraîchir l'adaptateur
                        mpAdaptater.notifyDataSetChanged();
                        // Faire défiler la ListView vers le bas
                    });

                    Thread.sleep(650);
                }
            } catch (SQLException | ClassNotFoundException | InterruptedException e) {
                handler.post(() -> {
                    Toast.makeText(MPActivity.this, "Erreur de connexion, impossible de récupérer les messages", Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }
}