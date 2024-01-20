package com.example.shareeat;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shareeat.adapter.IngrAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.FileUploadService;
import com.example.shareeat.modele.Ingredient;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
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



public class AddPlatActivity extends AppCompatActivity implements DialogCloseListener{
    ImageView imgP;
    EditText dateAjout;
    EditText titreP;
    EditText descriptionP;
    FloatingActionButton btnAddPhoto;
    private Uri selectedImageUri;
    private RecyclerView ingrRecyclerView;
    private IngrAdapter ingrAdapter;
    Button btnAddIngredient;
    Button btnValider;
    private BottomNavigationView bottomNavigationView;

    private List<Ingredient> ingredientList = new ArrayList<>();

    public ConnexionBD connBD = new ConnexionBD();

    public AddPlatActivity() throws SQLException, ClassNotFoundException {
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lier le layout à l'activity
        setContentView(R.layout.activity_add_plat);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Obtention des références sur les composants (ressources)
        dateAjout = (EditText) findViewById(R.id.dateFormAjoutPlat);
        imgP = (ImageView) findViewById(R.id.addImgPlat);
        btnAddPhoto = (FloatingActionButton) findViewById(R.id.btnAddPhoto);
        titreP = (EditText) findViewById(R.id.titrePlatInput);
        descriptionP = (EditText) findViewById(R.id.descPlatInput);
        btnAddIngredient = (Button) findViewById(R.id.btnAddNewIngr);
        btnValider = (Button) findViewById(R.id.btnValider);
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        // initialisation de la connexion bd
        try {
            ingredientList = connBD.getTousIngredients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    return true;
                }

                return false;
            }
        });


        //**************Partie liste des ingrédients ************************************
        ingrRecyclerView = (RecyclerView) findViewById(R.id.listIngrRecyclerView);

        // LayoutManager est responsable de la mesure et du positionnement
        // des éléments de la RecyclerView.
        // Nous devons attacher un layoutManager avant de pouvoir utiliser la RecyclerView.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ingrRecyclerView.setLayoutManager(layoutManager);

        ingrAdapter = new IngrAdapter(connBD, this, getSupportFragmentManager(), ingredientList);
        ingrRecyclerView.setAdapter(ingrAdapter);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewIngredient.newInstance().show(getSupportFragmentManager(), AddNewIngredient.TAG);
            }
        });
        //************** FIN (liste des ingrédients) ************************************

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            // Utilisation de la librairie ImagePicker
            public void onClick(View view) {
                ImagePicker.with(AddPlatActivity.this)
                        .cropSquare()	//Crop square image, its same as crop(1f, 1f) : ratio 1:1
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les données du formulaire
                String titre = titreP.getText().toString();
                String description = descriptionP.getText().toString();

                String date = dateAjout.getText().toString();
                // Formater la date dans le format attendu par la méthode ajouterRecette
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Garantit que si selectedImageUri est null, une chaîne vide sera utilisée à la place,
                // évitant ainsi la NullPointerException
                String imageUri = (selectedImageUri != null) ? selectedImageUri.getLastPathSegment() : "";

                // Récupérer l'objet Utilisateur depuis UserDataSingleton
                Utilisateur utilisateur = UserDataSingleton.getInstance().getUtilisateur();

                // Gestion des erreurs
                if (selectedImageUri == null) {
                    showToast("Veuillez sélectionner une image pour votre recette.");
                    return;
                } else if (titre.isEmpty()) {
                    showToast("Veuillez entrer un titre pour votre recette.");
                    return; // Sortir de la méthode sans enregistrer si le titre est vide
                } else if (utilisateur != null) {
                    // Enregistrement les données dans la table Recette
                    try {
                        uploadImageToServer(selectedImageUri);
                        Date parsedDate = inputFormat.parse(date);
                        String formattedDate = outputFormat.format(parsedDate);
                        connBD.ajouterRecette(utilisateur.getIdUtilisateur(), titre, description, formattedDate, imageUri);
                    } catch (SQLException | ParseException | IOException e) {
                        throw new RuntimeException(e);
                    }

                    showToast("Votre recette a bien été enregistrée et postée !");
                    // Redirection vers PlatActivity en passant l'ID de la recette comme extra
                    int newRecipeId = connBD.getLatestRecipeId();
                    Intent intent = new Intent(AddPlatActivity.this, PlatActivity.class);
                    intent.putExtra("postId", newRecipeId);
                    startActivity(intent);
                } else {
                    showToast("Problème d'idUtilisateur");
                }
            }
        });
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
                Call<ResponseBody> call = service.uploadRecipeImage(body);
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

        //si le résultat provient bien de l'activité d'image qu'on a lancée et que cette activité s'est terminée avec succès
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            selectedImageUri = data.getData();

            // L'image sélectionnée par l'utilisateur dans une autre activité (ici la galerie d'images)
            // est affichée dans la vue image (imgP).
            imgP.setImageURI(selectedImageUri);
        }
    }

    @Override
    protected void onResume () {
        super.onResume();

        // Obtenir la date actuelle
        Date dateDuJour = new Date();
        // Formater la date pour l'afficher dans le champ dateAjout
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(dateDuJour);
        dateAjout.setText(dateStr);
    }


    //S'occupe de la liste des ingrédients une fois le fragment d'ajout de nouvel ingrédient en bas de la page est fermé
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        try {
            ingredientList = connBD.getTousIngredients();
            ingrAdapter.setIngredients(ingredientList);
            ingrAdapter.notifyDataSetChanged(); //update le recyclerView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
