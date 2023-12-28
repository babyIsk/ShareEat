package com.example.shareeat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Plat;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PlatActivity extends Activity {
    ImageView imgP;
    EditText dateAjout;
    TextView txtTitreP;
    TextView txtDescriptionP;
    TextView txtIngredients;
    RecyclerView listIngredients;
    private ProgressBar loading;

    private ConnexionBD connBD;
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
        loading = findViewById(R.id.loading);

        try {
            connBD = new ConnexionBD();
            System.out.println("Connexion à la base de données établie.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Recupération ID du plat depuis intent
        int postId = getIntent().getIntExtra("postId", -1);

        // Charge les details du plat
        loadDetailsPlat(postId);
    }

    private void loadDetailsPlat(int idPlat) {
        //pour tester décommenter :
        //idPlat = 77;
        if (idPlat != -1) {
            // Affiche le cercle de loading
            loading.setVisibility(View.VISIBLE);

            Plat plat = connBD.getRecetteById(idPlat);
            updateUI(plat);
        }
    }

    private void updateUI(Plat plat) {
        if (plat != null) {
            txtTitreP.setText(plat.getTitreP());
            txtDescriptionP.setText(plat.getDescriptionP());
            // Formatage de la date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = inputFormat.parse(plat.getDate());
                String formattedDate = outputFormat.format(date);
                dateAjout.setText(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
                dateAjout.setText(plat.getDate()); // cas : erreur, afficher la date brute
            }

            Picasso.get().load(plat.getImageUrl()).into(imgP);

            // Cache le cercle de loading
            loading.setVisibility(View.GONE);
        } else {
            // cas : aucun plat n'est trouvé
            loading.setVisibility(View.GONE);
        }
    }
}
