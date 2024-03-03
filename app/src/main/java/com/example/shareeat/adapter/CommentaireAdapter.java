package com.example.shareeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareeat.R;
import com.example.shareeat.modele.Commentaire;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.Utilisateur;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentaireAdapter  extends BaseAdapter {
    private Context context;
    private List<Commentaire> commentaires;
    private Utilisateur user;

    public CommentaireAdapter(Context context, List<Commentaire> commentaires, Utilisateur user) {
        this.context = context;
        this.commentaires = commentaires;
        this.user = user;
    }

    @Override
    public int getCount() {
        return commentaires.size();
    }

    @Override
    public Object getItem(int position) {
        return commentaires.get(position);
    }

    public void addCommentaire(Commentaire nouveauCommentaire) {
        commentaires.add(0,nouveauCommentaire);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commentaire commentaire = commentaires.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.commentaire, parent, false);

        // Récupération des éléments de la vue
        TextView pseudo = convertView.findViewById(R.id.pseudocommentaire);
        TextView contenu = convertView.findViewById(R.id.commentContenu);
        ImageView photodeProfil = convertView.findViewById(R.id.imagecommentaire);
        TextView commentaireDate = convertView.findViewById(R.id.commentaireDate);

        // Formatage de la date du commentaire
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateFormatee = sdf.format(commentaire.getDate());
        commentaireDate.setText(dateFormatee);

        contenu.setText(commentaire.getContenu());

        try {
            // Récupération de l'utilisateur associé au commentaire depuis la base de données
            ConnexionBD bd = new ConnexionBD();
            Utilisateur utilisateur = bd.getUtilisateurById(commentaire.getIdUtilisateur());
            if (utilisateur != null){
                pseudo.setText(utilisateur.getPseudo());
                // Chargement de la photo de profil de l'utilisateur s'il en a une
                if (utilisateur.getPhoto() != null && !utilisateur.getPhoto().isEmpty()) {
                    Picasso.get().load("https://shareeat.alwaysdata.net/photoProfil/"+utilisateur.getPhoto()).into(photodeProfil);
                } else {
                    // Si l'utilisateur n'a pas de photo de profil, afficher une image par défaut
                    photodeProfil.setImageResource(R.drawable.profil_picture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}

