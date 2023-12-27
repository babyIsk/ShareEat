package com.example.shareeat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PlatActivity extends Activity {
    ImageView imgP;
    EditText dateAjout;
    TextView txtTitreP;
    TextView txtDescriptionP;
    TextView txtIngredients;
    RecyclerView listIngredients;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //lier le layout à l'activity
        setContentView(R.layout.activity_plat);

        //Obtention des références sur les composants (ressources)
        txtTitreP = (TextView) findViewById(R.id.titrePlat);
        txtDescriptionP = (TextView) findViewById(R.id.descriptionPlat);
        imgP = (ImageView) findViewById(R.id.imgPlat);
        dateAjout = (EditText) findViewById(R.id.dateAjoutPlat);
        txtIngredients = (TextView) findViewById(R.id.ingredientsLabel);
        listIngredients = (RecyclerView) findViewById(R.id.ingredientsList);
    }

    @Override
    protected void onStart () {
        super.onStart();
    }
    @Override
    protected void onResume () {
        super.onResume();

        String titre = "Burrata du chef";
        txtTitreP.setText(titre);
        String resume = "Magnifique plat/entrée d'origine italienne !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
        txtDescriptionP.setText(resume);

        String[] ingredients = {"Burrata", "Tomate", "Huile d'olive"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        //listIngredients.setAdapter(adapter);

        // Obtenir la date actuelle
        Date dateDuJour = new Date();
        // Formater la date pour l'afficher dans le champ dateAjout
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(dateDuJour);
        dateAjout.setText(dateStr);
    }
    @Override
    protected void onPause () {
        super.onPause();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }
}
