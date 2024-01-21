package com.example.shareeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    public RecetteAdapter(Context context, List<Plat> plats, Utilisateur user) {
        this.context = context;
        this.plats = plats;
        this.user = user;
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

            TextView utilisateur = convertView.findViewById(R.id.utilisateurRecette);
            TextView titre = convertView.findViewById(R.id.titreRecette);
            ImageView image = convertView.findViewById(R.id.imageRecette);
            ImageView photodeProfil = convertView.findViewById(R.id.ppUserRecette);
            Utilisateur userRecette;

            try {
                ConnexionBD bd = new ConnexionBD();
                userRecette = bd.getUtilisateurById(plat.getIdUtilisateur());
                utilisateur.setText(userRecette.getPseudo());
                String photoProfilUri = userRecette.getPhoto();
                if (photoProfilUri != null && !photoProfilUri.isEmpty()) {
                    Picasso.get().load("https://shareeat.alwaysdata.net/photoProfil/"+photoProfilUri).into(photodeProfil);
                } else {
                    // Si l'utilisateur n'a pas de photo, affiche une image par défaut
                    photodeProfil.setImageResource(R.drawable.profil_picture);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            titre.setText(plat.getTitreP());
            String photoPlatUri = "https://shareeat.alwaysdata.net/photoRecette/"+plat.getImageUrl();
            Picasso.get().load(photoPlatUri).into(image);
            return convertView;
        }
    }

