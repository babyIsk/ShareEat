package com.example.shareeat.modele;

import java.sql.Date;
import java.sql.Timestamp;

public class Commentaire {

    private int idCommentaire;
    private int idRecette;
    private int idUtilisateur;
    private String contenu;
    private Timestamp date;

    public Commentaire(int idCommentaire, int idUtilisateur, int idRecette, String contenu, Timestamp date) {
        this.idCommentaire = idCommentaire;
        this.idRecette = idRecette;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.date = date;
    }

    public Commentaire(int idUtilisateur, int idRecette, String contenu, Timestamp date) {
        this.idRecette = idRecette;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.date = date;
    }



    public int getIdCommentaire() {
        return idCommentaire;
    }

    public int getIdRecette() {
        return idRecette;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getContenu() {
        return contenu;
    }

    public Timestamp getDate() {
        return date;
    }
}
