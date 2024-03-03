package com.example.shareeat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.CommentaireActivity;
import com.example.shareeat.PlatActivity;
import com.example.shareeat.R;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.Utilisateur;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

public class RecetteAdapter extends BaseAdapter  {

    private Context context;
    private List<Plat> plats;
    private Utilisateur user;
    ConnexionBD bd;

    public RecetteAdapter(Context context, List<Plat> plats, Utilisateur user) throws SQLException, ClassNotFoundException {
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
            convertView = inflater.inflate(R.layout.recette_user, parent, false);

            // Récupération des éléments de la vue
            TextView utilisateur = convertView.findViewById(R.id.utilisateurRecette);
            TextView titre = convertView.findViewById(R.id.titreRecette);
            ImageView image = convertView.findViewById(R.id.imageRecette);
            ImageView photodeProfil = convertView.findViewById(R.id.ppUserRecette);
            TextView nbCommentaire = convertView.findViewById(R.id.nbCommentaire);
            ImageButton boutonLike = convertView.findViewById(R.id.boutonlike);

            try {
                // Récupération de l'utilisateur associé à la recette
                Utilisateur userRecette = bd.getUtilisateurById(plat.getIdUtilisateur());
                utilisateur.setText(userRecette.getPseudo());
                String photoProfilUri = userRecette.getPhoto();
                if (photoProfilUri != null && !photoProfilUri.isEmpty()) {
                    Picasso.get().load("https://shareeat.alwaysdata.net/photoProfil/"+photoProfilUri).into(photodeProfil);
                } else {
                    // Si l'utilisateur n'a pas de photo, affiche une image par défaut
                    photodeProfil.setImageResource(R.drawable.profil_picture);
                }
                // Récupération et affichage du nombre de commentaires associés à la recette
                int nbC = bd.getNombreCommentaire(plat.getIdP());
                if(nbC == 1){
                    nbCommentaire.setText(nbC + " Commentaire");
                }else{
                    nbCommentaire.setText(nbC + " Commentaires");
                }

                // Gestion du clic sur le nombre de commentaires
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lorsque l'utilisateur clique sur nbCommentaire, ouvrez CommentaireActivity
                        Intent intent = new Intent(context, PlatActivity.class);
                        // Ajoutez l'ID du plat comme extra à l'intent
                        intent.putExtra("postId", plat.getIdP());
                        context.startActivity(intent);
                    }
                });

                // Gestion du clic sur l'image de la recette
                nbCommentaire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lorsque l'utilisateur clique sur nbCommentaire, ouvrez CommentaireActivity
                        Intent intent = new Intent(context, CommentaireActivity.class);
                        // Ajoutez l'ID du plat comme extra à l'intent
                        intent.putExtra("recetteId", plat.getIdP());
                        context.startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

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

