package com.example.shareeat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.adapter.GaleryAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class ProfilGaleryActivity extends AppCompatActivity {

    ImageView imgProfil;
    EditText userNameInput, userPseudoInput, userBioInput;
    View dividerProfil;
    Button btnModifier, btnEnregistrer, btnDeconnexion;
    FloatingActionButton btnAddPhoto;
    private Uri selectedImageUri;
    private RecyclerView galeryRecyclerView;
    private GaleryAdapter galeryAdapter;
    public ConnexionBD connBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_galery);
        getSupportActionBar().hide();

        // Références aux vues
        imgProfil = (ImageView) findViewById(R.id.imgProfil);
        btnAddPhoto = (FloatingActionButton) findViewById(R.id.btnAddPhoto);
        userNameInput = (EditText) findViewById(R.id.tvUserName);
        userPseudoInput = (EditText) findViewById(R.id.tvUserPseudo);
        userBioInput = (EditText) findViewById(R.id.tvUserBio);
        dividerProfil = (View) findViewById(R.id.dividerProfil);
        btnModifier = (Button) findViewById(R.id.btnModifier);
        btnEnregistrer = (Button) findViewById(R.id.btnEnregistrer);
        btnDeconnexion = (Button) findViewById(R.id.btnDeconnexion);
        galeryRecyclerView = (RecyclerView) findViewById(R.id.galeryRecyclerView);

        try {
            connBD = new ConnexionBD();
            System.out.println("Connexion à la base de données établie.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Récupérer l'objet Utilisateur depuis UserDataSingleton
        Utilisateur utilisateur = UserDataSingleton.getInstance().getUtilisateur();

        // Appel de la fonction getUtilisateurById pour obtenir les données
        Utilisateur utilisateurConnecte = connBD.getUtilisateurById(utilisateur.getIdUtilisateur());

        updateUI(utilisateurConnecte);

        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rendre les EditText en mode non éditable
                userNameInput.setFocusableInTouchMode(true);
                userNameInput.setCursorVisible(true);
                userNameInput.setFocusable(true);
                userPseudoInput.setFocusableInTouchMode(true);
                userPseudoInput.setCursorVisible(true);
                userPseudoInput.setFocusable(true);
                userBioInput.setFocusableInTouchMode(true);
                userBioInput.setCursorVisible(true);
                userBioInput.setFocusable(true);

                // Rendre visible les boutons d'édition
                btnEnregistrer.setVisibility(View.VISIBLE);
                btnAddPhoto.setVisibility(View.VISIBLE);

                // Désactiver le bouton de déconnexion
                btnDeconnexion.setEnabled(false);

                // Focus sur le premier EditText (userNameInput)
                userNameInput.requestFocus();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            // Utilisation de la librairie ImagePicker
            public void onClick(View view) {
                ImagePicker.with(ProfilGaleryActivity.this)
                        .cropSquare()	//Crop square image, its same as crop(1f, 1f) : ratio 1:1
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les nouvelles valeurs des EditText
                String nouveauPrenom = userNameInput.getText().toString();
                String nouveauPseudo = userPseudoInput.getText().toString();
                String nouvelleBio = userBioInput.getText().toString();
                String imageUri = (selectedImageUri != null) ? selectedImageUri.toString() : "";

                // Gestion erreur
                if (TextUtils.isEmpty(nouveauPrenom) || TextUtils.isEmpty(nouveauPseudo)) {
                    showToast("Le prénom et le pseudo ne peuvent pas être vides !");
                    return;
                }

                // Update l'objet Utilisateur
                utilisateurConnecte.setPrenom(nouveauPrenom);
                utilisateurConnecte.setPseudo(nouveauPseudo);
                utilisateurConnecte.setBio(nouvelleBio);
                utilisateurConnecte.setPhoto(imageUri);

                // Update BD table Utilisateur
                connBD.updateUtilisateur(utilisateurConnecte);

                showToast("Votre profil a bien été modifié !");
                updateUI(utilisateurConnecte);

                userNameInput.setEnabled(false);
                userPseudoInput.setEnabled(false);
                userBioInput.setEnabled(false);
                btnEnregistrer.setVisibility(View.GONE);
                btnAddPhoto.setVisibility(View.GONE);
                btnDeconnexion.setEnabled(true);
            }
        });

        initRecyclerView();
    }

    private void updateUI(Utilisateur user) {
        if (user != null) {
            userNameInput.setText(user.getPrenom());
            userPseudoInput.setText(user.getPseudo());
            userBioInput.setText(user.getBio());

            // Afficher une bio par défaut
            String bio = user.getBio();
            if (TextUtils.isEmpty(bio)) {
                userBioInput.setText("Ajoutez une bio !");
            } else {
                userBioInput.setText(bio);
            }

            // Mettre à jour la photo de profil
            String photoUri = user.getPhoto();
            if (photoUri != null && !photoUri.isEmpty()) {
                Picasso.get().load(photoUri).into(imgProfil);
            } else {
                // Si l'utilisateur n'a pas de photo, affiche une image par défaut
                imgProfil.setImageResource(R.drawable.profil_picture);
            }
            Log.d("ProfilGaleryActivity", "Bio récupérée de la base de données : " + bio);
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    // Sélection d'une image à partir de la galerie (utilisation ImagePicker librairie).
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            selectedImageUri = data.getData();

            imgProfil.setImageURI(selectedImageUri);
        }
    }
    
    private void initRecyclerView() {
        galeryAdapter = new GaleryAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        galeryRecyclerView.setLayoutManager(layoutManager);

        // Attribuer l'adaptateur à la RecyclerView
        galeryRecyclerView.setAdapter(galeryAdapter);
    }
}
