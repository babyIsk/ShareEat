package com.example.shareeat.modele;

public class Utilisateur {

    private String prenom;
    private String nom;
    private String pseudo;
    private String email;
    private String motDePasse;

    public Utilisateur(String prenom, String nom, String pseudo, String email, String motDePasse) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    @Override
    public String toString() {
        return getPseudo() + "(" + getPrenom() + " " + getNom() +")";
    }
}
