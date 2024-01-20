package com.example.shareeat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.adapter.GaleryAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.FileUploadService;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
public class ProfilGaleryActivity extends AppCompatActivity implements OnItemListener {

    ImageView imgProfil;
    EditText userNameInput, userPseudoInput, userBioInput;
    View dividerProfil;
    Button btnModifier, btnEnregistrer, btnDeconnexion;
    ImageButton btnFlecheRetour, btnFlecheSuivant;
    FloatingActionButton btnAddPhoto;
    private Uri selectedImageUri;
    TextView tvMoisAnnee;
    private RecyclerView galeryRecyclerView;
    private Calendar selectedDate;
    private ArrayList<String> daysInMonth;
    private GaleryAdapter galeryAdapter;
    public ConnexionBD connBD;
    private Utilisateur utilisateurConnecte;
    List<Plat> recettesPourChaqueJour = new ArrayList<>();
    List<Plat> listeRecettesUtilisateur = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_galery);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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
        tvMoisAnnee = (TextView) findViewById(R.id.tvMoisAnnee);
        btnFlecheRetour = (ImageButton) findViewById(R.id.btnFlecheRetour);
        btnFlecheSuivant = (ImageButton) findViewById(R.id.btnFlecheSuivant);

        try {
            connBD = new ConnexionBD();
            System.out.println("Connexion à la base de données établie.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Récupérer l'objet Utilisateur depuis UserDataSingleton
        Utilisateur utilisateur = UserDataSingleton.getInstance().getUtilisateur();

        // Appel de la fonction getUtilisateurById pour obtenir les données
        utilisateurConnecte = connBD.getUtilisateurById(utilisateur.getIdUtilisateur());
        Log.d("ProfilGaleryActivity", "ID de l'utilisateur: " + utilisateurConnecte.getIdUtilisateur());

        selectedDate = Calendar.getInstance();
        Log.d("ProfilGaleryActivity", "Selected date initialized: " + selectedDate.getTime());

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
                String imageUri = (selectedImageUri != null) ? selectedImageUri.getLastPathSegment() : "";

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

                if (selectedImageUri != null) {
                    try {
                        uploadImageToServer(selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                showToast(utilisateurConnecte.getPhoto());
                updateUI(utilisateurConnecte);

                userNameInput.setEnabled(false);
                userPseudoInput.setEnabled(false);
                userBioInput.setEnabled(false);
                btnEnregistrer.setVisibility(View.GONE);
                btnAddPhoto.setVisibility(View.GONE);
                btnDeconnexion.setEnabled(true);
            }
        });

        // *******************PARTIE CALENDRIER / GALERIE******************************************
        setMonthView();
        try {
            RecyclerView.LayoutManager layoutManager= new GridLayoutManager(getApplicationContext(), 7);
            galeryRecyclerView.setLayoutManager(layoutManager);
            galeryAdapter = new GaleryAdapter(this, daysInMonth, recettesPourChaqueJour, this);
            galeryAdapter.setlisteRecettesUtilisateur(listeRecettesUtilisateur);
            galeryAdapter.setDaysOfMonth(daysInMonth);
            galeryAdapter.setRecettesPourChaqueJour(recettesPourChaqueJour);
            galeryRecyclerView.setAdapter(galeryAdapter);
            galeryAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ProfilGaleryActivity", "Month view set for date: " + selectedDate.getTime());
        btnFlecheRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(v);
            }
        });
        btnFlecheSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(v);
            }
        });
    }

    private void setMonthView() {
        tvMoisAnnee.setText(monthYearFromDate(selectedDate));
        daysInMonth = daysInMonthArray(selectedDate);

        Log.d("ProfilGaleryActivity", "Days in Month: " + TextUtils.join(", ", daysInMonth));

        // Formater les jours du mois sous la forme "yyyy/MM/dd"
        ArrayList<String> formattedDays = new ArrayList<>();
        for (String day : daysInMonth) {
            if (!day.isEmpty()) {
                int dayOfMonth = Integer.parseInt(day);
                String formattedDay = new SimpleDateFormat("yyyy/MM/dd").format(selectedDate.getTime());
                formattedDay = formattedDay.substring(0, 8) + String.format("%02d", dayOfMonth);
                formattedDays.add(formattedDay);
            } else {
                formattedDays.add("");
            }
        }
        Log.d("ProfilGaleryActivity", "Days in Month formatted: " + TextUtils.join(", ", formattedDays));

        // Afficher les recettes propre à l'utilisateur qu'il a posté en fonction de chaque jour du calendrier
        listeRecettesUtilisateur = connBD.getTousRecetteByIdUser(utilisateurConnecte, formattedDays);
        Log.d("ProfilGaleryActivity", "Liste des recettes utilisateur : " + listeRecettesUtilisateur);
        // Pour chaque date dans le calendrier, récupérez la recette de l'utilisateur pour cette date
        for (String dateCalendrier : formattedDays) {
            Plat recettePourCetteDate = null;
            Log.d("ProfilGaleryActivity", "Recette/ plat : " + recettePourCetteDate);
            Log.d("ProfilGaleryActivity", "Date: " + dateCalendrier);

            // Parcourez la liste des recettes de l'utilisateur
            for (Plat recette : listeRecettesUtilisateur) {

                // Convertissez la date de recette en un objet Date car getDate() renvoi un String et pas une Date
                Date recetteDate;
                try {
                    recetteDate = new SimpleDateFormat("yyyy-MM-dd").parse(recette.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de conversion de la date
                    continue;
                }

                // Comparez les dates en les convertissant au format souhaité
                String recetteFormattedDate = new SimpleDateFormat("yyyy/MM/dd").format(recetteDate);
                if (dateCalendrier.equals(recetteFormattedDate)) {
                    recettePourCetteDate = recette;
                    Log.d("ProfilGaleryActivity", "Recette/ plat : " + recettePourCetteDate);
                    Log.d("ProfilGaleryActivity", "Recette trouvée pour la date: " + recetteFormattedDate);
                    break;
                }
            }
            // Ajoutez la recette (ou null) à la liste
            recettesPourChaqueJour.add(recettePourCetteDate);
        }
    }

    private ArrayList<String> daysInMonthArray(Calendar  date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        int daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        date.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        // Ajoutez des espaces vides pour les jours avant le premier jour du mois
        for (int i = 2; i < dayOfWeek; i++) {
            daysInMonthArray.add("");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthArray.add(String.valueOf(i));
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        String result = formatter.format(date.getTime());
        Log.d("MonthYearFromDate", "Result: " + result);
        return result;
    }

    public void previousMonthAction(View view) {
        Log.d("ProfilGaleryActivity", "Previous Month Clicked");
        selectedDate.add(Calendar.MONTH, -1);
        setMonthView();
        galeryAdapter.setDaysOfMonth(daysInMonth);
        galeryAdapter.setRecettesPourChaqueJour(recettesPourChaqueJour);
        galeryAdapter.setlisteRecettesUtilisateur(listeRecettesUtilisateur);
        Log.d("ProfilGaleryActivity", "daysInMonth : " + daysInMonth);
        Log.d("ProfilGaleryActivity", "listeRecettesUtilisateur : " + daysInMonth);
        galeryAdapter.notifyDataSetChanged();
    }

    public void nextMonthAction(View view) {
        Log.d("ProfilGaleryActivity", "Next Month Clicked");
        selectedDate.add(Calendar.MONTH, 1);
        setMonthView();
        galeryAdapter.setDaysOfMonth(daysInMonth);
        galeryAdapter.setRecettesPourChaqueJour(recettesPourChaqueJour);
        galeryAdapter.setlisteRecettesUtilisateur(listeRecettesUtilisateur);
        galeryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText.equals("")) {
            showToast("Selected date : " + dayText + " " + monthYearFromDate(selectedDate));
        }
    } //******************** FIN PARTIE CALENDRIER / GALERIE***************************************

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
            String photoUri = "https://shareeat.alwaysdata.net/photoProfil/"+user.getPhoto();


            if (photoUri != null && !photoUri.isEmpty()) {
                Picasso.get().load(photoUri).into(imgProfil);
            } else {
                // Si l'utilisateur n'a pas de photo, affiche une image par défaut
                imgProfil.setImageResource(R.drawable.profil_picture);
            }
        }
    }




    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void uploadImageToServer(Uri imageUri) throws IOException {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            // Envoi des bytes au serveur (implémentez cette partie selon votre API)
            // Utilisez des bibliothèques comme Retrofit, Volley ou OkHttpClient pour envoyer la requête POST
            // Assurez-vous de gérer les détails d'authentification ou de sécurité nécessaires
            // Exemple avec Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://shareeat.alwaysdata.net/")
                    .build();

            FileUploadService service = retrofit.create(FileUploadService.class);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageUri.getLastPathSegment(), requestFile);

            try {
                Call<ResponseBody> call = service.uploadImage(body);
                Response<ResponseBody> response = call.execute();

                if (response.isSuccessful()) {
                    // Image téléchargée avec succès
                    showToast("reussi");
                    Log.d("Server Response", response.body().string());
                } else {
                    // Gestion des erreurs
                    showToast("Failed to upload image. Error: " + response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToast("Error reading image file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error reading image file");
        }
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
}
