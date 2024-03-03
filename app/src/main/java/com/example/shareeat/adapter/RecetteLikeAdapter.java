package com.example.shareeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.shareeat.R;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.Utilisateur;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

public class RecetteLikeAdapter extends BaseAdapter  {

    private Context context;
    private List<Plat> plats;
    private Utilisateur user;
    ConnexionBD bd;

    public RecetteLikeAdapter(Context context, List<Plat> plats, Utilisateur user) throws SQLException, ClassNotFoundException {
        this.context = context;
        this.plats = plats;
        this.user = user;
        this.bd = new ConnexionBD();
    }


        @Override
        public int getCount() {
            return plats.size();
        }

        @Override
        public Object getItem(int position) {
            return plats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Plat plat = plats.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.recette, parent, false);

            // Récupération des éléments de la vue
            TextView titre = convertView.findViewById(R.id.titre);
            ImageView image = convertView.findViewById(R.id.image);
            ImageButton boutonLike = convertView.findViewById(R.id.like);

            // Affichage du titre de la recette
            titre.setText(plat.getTitreP());
            // Chargement de l'image de la recette
            String photoPlatUri = "https://shareeat.alwaysdata.net/photoRecette/"+plat.getImageUrl();
            Picasso.get().load(photoPlatUri).into(image);

            // Gestion de l'état du bouton like
            if (bd.likeExists(user.getIdUtilisateur(),  plat.getIdP())) {
                boutonLike.setColorFilter(ContextCompat.getColor(context, R.color.rouge_shareeat));
            }

            // Gestion du clic sur le bouton like
            boutonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int recetteId = plat.getIdP();
                    int userId = user.getIdUtilisateur();

                    if (!bd.likeExists(userId, recetteId)) {
                        bd.like(userId, recetteId);
                        boutonLike.setColorFilter(ContextCompat.getColor(context, R.color.rouge_shareeat));

                    } else {
                        bd.Unlike(userId, recetteId);
                        boutonLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
                    }

                }
            });

            return convertView;
        }

    }

