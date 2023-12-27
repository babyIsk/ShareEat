package com.example.shareeat.modele;

import java.util.Date;

public class Plat {
    int idP;
    int idUtilisateur;
    String titreP;
    String descriptionP;
    String[] ingrédients;

    String date;
    String imgRecette;

    public int getIdP() {
        return idP;
    }
    public int getIdUtilisateur() {return idUtilisateur;}

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getTitreP() {
        return titreP;
    }

    public void setTitreP(String titreP) {
        this.titreP = titreP;
    }

    public String getDescriptionP() {
        return descriptionP;
    }

    public void setDescriptionP(String descriptionP) {
        this.descriptionP = descriptionP;
    }

    public String[] getIngrédients() {
        return ingrédients;
    }

    public void setIngrédients(String[] ingrédients) {
        this.ingrédients = ingrédients;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
    public String getImageUrl() {
        return imgRecette;
    }
    public void setImgRecette(String imageRecette) {
        this.imgRecette = imageRecette;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }




}
