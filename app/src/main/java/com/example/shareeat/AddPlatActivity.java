package com.example.shareeat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.adapter.IngrAdapter;
import com.example.shareeat.modele.Ingredient;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddPlatActivity extends Activity {
    ImageView imgP;
    EditText dateAjout;
    FloatingActionButton btnAddPhoto;
    private RecyclerView ingrRecyclerView;
    private IngrAdapter ingrAdapter;

    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lier le layout à l'activity
        setContentView(R.layout.activity_add_plat);

        ingredientList = new ArrayList<>();

        // Obtention des références sur les composants (ressources)
        dateAjout = (EditText) findViewById(R.id.dateFormAjoutPlat);
        imgP = (ImageView) findViewById(R.id.addImgPlat);
        btnAddPhoto = (FloatingActionButton) findViewById(R.id.btnAddPhoto);

        //**************Partie liste des ingrédients ************************************
        ingrRecyclerView = (RecyclerView) findViewById(R.id.listIngrRecyclerView);

        // LayoutManager est responsable de la mesure et du positionnement
        // des éléments de la RecyclerView.
        // Nous devons attacher un layoutManager avant de pouvoir utiliser la RecyclerView.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingrRecyclerView.setLayoutManager(layoutManager);

        ingrAdapter = new IngrAdapter(this, ingredientList);
        ingrRecyclerView.setAdapter(ingrAdapter);

        Ingredient ingr = new Ingredient();
        ingr.setIngr("C'est un ingredient test");
        ingr.setStatus(0); // 0 -> false, 1 -> true
        ingr.setId(1);

        ingredientList.add(ingr);
        ingredientList.add(ingr);
        ingredientList.add(ingr);
        ingredientList.add(ingr);
        ingredientList.add(ingr);
        ingredientList.add(ingr);

        if (ingredientList.isEmpty()) {
            // La liste d'ingrédients est vide, ajoutez des logs ou un message de débogage.
            Log.d("AddPlatActivity", "La liste d'ingrédients est vide.");
        } else {
            // La liste d'ingrédients contient des éléments, mettez à jour l'adaptateur.
            ingrAdapter.setIngredients(ingredientList);
        }
        //************** FIN (liste des ingrédients) ************************************

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            // Utilisation de la librairie ImagePicker
            public void onClick(View view) {
                ImagePicker.with(AddPlatActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    // Sélection d'une image à partir de la galerie (utilisation ImagePicker librairie).
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        // L'image sélectionnée par l'utilisateur dans une autre activité (ici la galerie d'images)
        // est affichée dans la vue image (imgP).
        imgP.setImageURI(uri);
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
}
