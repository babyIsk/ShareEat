package com.example.shareeat.modele;

import java.sql.Date;

public class Plat {
    int idP;
    int idUtilisateur;
    String titreP;
    String descriptionP;
    String imgRecette;
    String ingredients;
    private Date date;

    public Plat(int idP, int idUtilisateur, String titreP, String descriptionP, Date date, String imgRecette, String ingredients) {
        this.idP = idP;
        this.idUtilisateur = idUtilisateur;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.date = date;
        this.imgRecette = imgRecette;
        this.ingredients = ingredients;
    }

    public int getIdUtilisateur() { return idUtilisateur; }


    public void setIdP(int idP) {
        this.idP = idP;
    }
    public int getIdP() {
        return idP;
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

    public String getIngrédients() {
        return ingredients;
    }

    public String setIngrédients(String ingredients) {
        return this.ingredients = ingredients;
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

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

}
