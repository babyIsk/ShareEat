package com.example.shareeat.modele;

import java.sql.Date;

public class Plat {
    int idP;
    int idUtilisateur;
    private String titreP;
    private String descriptionP;
    private String[] ingrédients;
    private boolean aPostePlat;
    private Date date;
    private String imgRecette;

    public Plat(int idP, int idUtilisateur, String titreP, String descriptionP, Date date, String imgRecette) {
        this.idP = idP;
        this.idUtilisateur = idUtilisateur;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.date = date;
        this.imgRecette = imgRecette;
    }

    public int getIdUtilisateur() { return idUtilisateur; }

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

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAPostePlat() {
        return aPostePlat;
    }
    public void setAPostePlat(boolean aPostePlat) {
        this.aPostePlat = aPostePlat;
    }
}
