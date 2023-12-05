package com.example.shareeat.modele;

public class Plat {
    int idP;
    int idUtilisateur;
    String titreP;
    String descriptionP;
    String[] ingrédients;

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
}
