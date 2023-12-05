package com.example.shareeat.modele;

public class Plat {
    int idP;
    String titreP;
    String descriptionP;
    String[] ingrédients;

    public int getIdP() {
        return idP;
    }

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
