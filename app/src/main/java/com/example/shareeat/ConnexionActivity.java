package com.example.shareeat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;

import java.sql.SQLException;

public class ConnexionActivity extends AppCompatActivity {
    private TextView inscription;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Masque un éléments de la navbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialisation des éléments de la vue
        inscription = findViewById(R.id.cliquezIciTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Gestion du clic d'inscription
        inscription.setOnClickListener(view -> {
            Intent main = new Intent(getApplicationContext(), InscriptionActivity.class);
            startActivity(main);
        });

        // Gestion du clic sur le bouton de connexion
        loginButton.setOnClickListener(view -> {
            Utilisateur utilisateur = connexion(emailEditText.getText().toString(), passwordEditText.getText().toString());
            if (utilisateur != null) {
                // Stocker l'utilisateur dans la classe UserData
                UserDataSingleton.getInstance().setUtilisateur(utilisateur);


                /*Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("recetteId", 6);
                startActivity(intent);
                finish();*/

                // Naviguer vers l'activité suivante
                Intent intent = new Intent(getApplicationContext(), AccueilActivity.class);
                startActivity(intent);
                finish();

                //Intent intent = new Intent(getApplicationContext(), AddPlatActivity.class);
                //intent.putExtra("user", utilisateur);
                //startActivity(intent);
                //finish();
            } else {
                Toast.makeText(ConnexionActivity.this, "Connexion impossible veuillez vérifier votre eamil ou votre mot de passe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Utilisateur connexion(String email, String motDePasse) {
        try {
            return new ConnexionBD().connexion(email, motDePasse);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}