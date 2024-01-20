package com.example.shareeat.modele;


import java.util.ArrayList;
import java.util.List;

import java.sql.Date;


public class Plat {

    int idP;
    int idUtilisateur;

    String titreP;
    String descriptionP;
    private List<Ingredient> ingredientsDuPlat;
    String imgRecette;
    private Date date;

    public Plat(int idP, int idUtilisateur, String titreP, String descriptionP, Date date, String imgRecette) {
        this.idP = idP;
        this.idUtilisateur = idUtilisateur;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.date = date;
        this.imgRecette = imgRecette;
        this.ingredientsDuPlat = new ArrayList<>();
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

    public List<Ingredient> getIngrédients() {
        return ingredientsDuPlat;
    }

    public void setIngrédients(List<Ingredient> ingrédients) {
        this.ingredientsDuPlat = ingrédients;
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
