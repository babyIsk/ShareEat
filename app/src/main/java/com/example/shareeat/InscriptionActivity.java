package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Utilisateur;

import java.sql.SQLException;

public class InscriptionActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private EditText prenomEditText;
    private EditText nomEditText;
    private EditText pseudoEditText;
    private EditText emailEditText;
    private EditText confirmationEmailEditText;
    private EditText mdpEditText;
    private EditText confirmationMdpEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        getSupportActionBar().hide();
        viewFlipper = findViewById(R.id.view_flipperInscription);
        prenomEditText = findViewById(R.id.prenomEditText);
        nomEditText = findViewById(R.id.nomEditText);
        pseudoEditText = findViewById(R.id.pseudoEditText);
        emailEditText = findViewById(R.id.emailEditText);
        confirmationEmailEditText = findViewById(R.id.confirmationemailEditText);
        mdpEditText = findViewById(R.id.mdpEditText);
        confirmationMdpEditText = findViewById(R.id.confirmationmdpEditText);
        signUpButton = findViewById(R.id.SignUpButton);

        signUpButton.setOnClickListener(view -> {
            if (champsVides(prenomEditText, nomEditText, pseudoEditText, emailEditText, confirmationEmailEditText, mdpEditText, confirmationMdpEditText)) {
                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else if (!confirmation(mdpEditText, confirmationMdpEditText)) {
                Toast.makeText(InscriptionActivity.this, "Les mots de passe rentré ne sont pas identique", Toast.LENGTH_SHORT).show();
            } else if (!confirmation(emailEditText, confirmationEmailEditText)) {
                Toast.makeText(InscriptionActivity.this, "Les emails ne sont pas identique", Toast.LENGTH_SHORT).show();
            } else if (inscription(pseudoEditText.getText().toString(), nomEditText.getText().toString(), prenomEditText.getText().toString(), emailEditText.getText().toString(), mdpEditText.getText().toString())) {
                Toast.makeText(InscriptionActivity.this, "inscription réussite", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }


    public void previousView(View v) {
        int currentViewIndex = viewFlipper.getDisplayedChild();
        if (currentViewIndex > 0) {
            viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
            viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
            viewFlipper.showPrevious();
        }else{
            finish();
        }
    }


    public void nextView(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showNext();
    }

    private boolean champsVides(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean confirmation(EditText mot, EditText confirmationMot) {
        if (mot.getText().toString().equals(confirmationMot.getText().toString()))
            return true;
        return false;
    }

    public boolean inscription(String pseudo, String nom, String prenom, String email, String mdp) {
        try {
            ConnexionBD bd = new ConnexionBD();
            if (bd.inscription(pseudo, nom, prenom, email, mdp) != null)
                return true;
        } catch (SQLException | ClassNotFoundException e) {
            Toast.makeText(InscriptionActivity.this, "Erreur de connexion impossible de proceder a l'inscription ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }
}