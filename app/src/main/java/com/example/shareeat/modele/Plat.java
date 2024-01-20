package com.example.shareeat.modele;

import java.util.ArrayList;
import java.util.List;

public class Plat {
    int idP;
    int idUtilisateur;
    String titreP;
    String descriptionP;
    private List<Ingredient> ingredientsDuPlat;
    String date;
    String imgRecette;

    public Plat(int idP, int idUtilisateur, String titreP, String descriptionP, String date, String imgRecette) {
        this.idP = idP;
        this.idUtilisateur = idUtilisateur;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.date = date;
        this.imgRecette = imgRecette;
        this.ingredientsDuPlat = new ArrayList<>();
    }

    //public Plat(int idP, int idUtilisateur, String titreP, String descriptionP, String date, String imgRecette, List<Ingredient> ingrédients) {
        //this.idP = idP;
        //this.idUtilisateur = idUtilisateur;
        //this.titreP = titreP;
        //this.descriptionP = descriptionP;
        //this.date = date;
        //this.imgRecette = imgRecette;
        //this.ingredientsDuPlat = ingrédients;
        //this.aPostePlat = false;
    //}
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

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
