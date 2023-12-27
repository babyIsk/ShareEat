package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareeat.modele.ConnexionBD;
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
        getSupportActionBar().hide();

        inscription = findViewById(R.id.cliquezIciTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        inscription.setOnClickListener(view -> {
            Intent main = new Intent(getApplicationContext(), InscriptionActivity.class);
            startActivity(main);
        });

        loginButton.setOnClickListener(view -> {
            Utilisateur utilisateur = connexion(emailEditText.getText().toString(), passwordEditText.getText().toString());
            if (utilisateur != null) {
                Intent intent = new Intent(getApplicationContext(), AddPlatActivity.class);
                intent.putExtra("user", utilisateur);
                startActivity(intent);
                finish();
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