package com.example.shareeat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.shareeat.MessageActivity;
import com.example.shareeat.R;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.Plat;
import com.example.shareeat.modele.Utilisateur;
import com.mysql.jdbc.Util;
import com.squareup.picasso.Picasso;
import java.sql.SQLException;
import java.util.List;

public class MPAdaptater extends BaseAdapter {

    private Context context;
    private List<Utilisateur> utilisateurs;
    private Utilisateur user;

    public MPAdaptater(Context context, List<Utilisateur> utilisateurs, Utilisateur user) {
        this.context = context;
        this.utilisateurs = utilisateurs;
        this.user = user;
    }

    @Override
    public int getCount() {
        return utilisateurs.size();
    }

    @Override
    public Object getItem(int position) {
        return utilisateurs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Utilisateur utilisateur = utilisateurs.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.mp, parent, false);

        // Récupération des éléments de la vue
        TextView pseudo = convertView.findViewById(R.id.PseudoMP);
        TextView text = convertView.findViewById(R.id.textMP);
        TextView heure = convertView.findViewById(R.id.heureMP);
        ImageView photodeProfil = convertView.findViewById(R.id.ppUserMP);

        pseudo.setText(utilisateur.getPseudo());
        // Chargement de la photo de profil de l'utilisateur s'il en a une
        if (utilisateur.getPhoto() != null && !utilisateur.getPhoto().isEmpty()) {
            Picasso.get().load("https://shareeat.alwaysdata.net/photoProfil/"+utilisateur.getPhoto()).into(photodeProfil);
        } else {
            // Si l'utilisateur n'a pas de photo, affiche une image par défaut
            photodeProfil.setImageResource(R.drawable.profil_picture);
        }

        // Récupération et affichage du dernier message entre l'utilisateur actuel et l'utilisateur en cours
        try {
            ConnexionBD bd = new ConnexionBD();
            Message message = bd.getDernierMessage(user.getIdUtilisateur(),utilisateur.getIdUtilisateur());
            if (message != null){
                text.setText(message.getMessage());
                heure.setText(message.getDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
