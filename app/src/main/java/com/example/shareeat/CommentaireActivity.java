package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareeat.adapter.CommentaireAdapter;
import com.example.shareeat.adapter.MPAdaptater;
import com.example.shareeat.modele.Commentaire;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class CommentaireActivity extends AppCompatActivity {
    private Utilisateur user;
    private ListView listView;
    private CommentaireAdapter comAdaptater;
    private TextView titreRecette;
    private TextView pseudoUtilisateur;
    private Plat recette;
    private EditText etCommentaire;
    private Button btnSendCommentaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaire);

        // Récupération de l'utilisateur connecté
        user = UserDataSingleton.getInstance().getUtilisateur();

        // Initialisation des éléments de la vue
        listView = findViewById(R.id.lvCommentaires);
        titreRecette = findViewById(R.id.titreplatCommenter);
        pseudoUtilisateur = findViewById(R.id.createur);
        etCommentaire = findViewById(R.id.etCommentaire);
        btnSendCommentaire = findViewById(R.id.btnSendCommentaire);

        try {
            ConnexionBD bd = new ConnexionBD();
            // Récupération de l'ID de la recette à partir de l'intent
            recette =  bd.getRecetteById(getIntent().getIntExtra("recetteId", 0));
            // Affichage du titre de la recette et du pseudo de l'utilisateur qui l'a créée
            titreRecette.setText(recette.getTitreP());
            pseudoUtilisateur.setText(bd.getUtilisateurById(recette.getIdUtilisateur()).getPseudo());
            // Récupération et affichage des commentaires de la recette
            List<Commentaire> commentaires = bd.getCommenaire(recette.getIdP());
            if(commentaires != null){
                comAdaptater = new CommentaireAdapter(this, commentaires, user);
                listView.setAdapter(comAdaptater);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Gestion du clic sur le bouton d'envoi de commentaire
        btnSendCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appeler la méthode envoyerCommentaire lorsque l'utilisateur appuie sur le bouton
                envoyerCommentaire();
            }
        });
    }


    public void envoyerCommentaire() {
        String contenuCommentaire = etCommentaire.getText().toString().trim();
        if (!contenuCommentaire.isEmpty()) {
            try {
                ConnexionBD bd = new ConnexionBD();
                // Appel à la méthode sendCommentaire pour envoyer le commentaire
                Commentaire envoie = bd.sendCommentaire(user.getIdUtilisateur(), recette.getIdP(), contenuCommentaire);
                if (envoie != null) {
                    // Si l'envoi est réussi, mettez à jour l'affichage en ajoutant le commentaire à la liste
                    comAdaptater.addCommentaire(envoie); // Ajoutez cette méthode à votre adapter
                    etCommentaire.setText(""); // Efface le champ de commentaire après l'envoi
                } else {
                    // Affichage d'un message d'erreur en cas d'échec d'envoi du commentaire
                    Toast.makeText(CommentaireActivity.this, "Échec de l'envoi du commentaire", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Affichage d'un message si le champ de commentaire est vide
            Toast.makeText(CommentaireActivity.this, "Veuillez saisir un commentaire", Toast.LENGTH_SHORT).show();
        }
    }

}