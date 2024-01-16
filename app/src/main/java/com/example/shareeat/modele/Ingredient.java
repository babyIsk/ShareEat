package com.example.shareeat.modele;

public class Ingredient {

    int id;
    int status; // 0 -> false nonchecked, 1 -> true checked
    String ingr;



    public Ingredient(int id, int status, String ingr) {
        this.id = id;
        this.status = status;
        this.ingr = ingr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIngr() {
        return ingr;
    }

    public void setIngr(String ingr) {
        this.ingr = ingr;
    }
}
