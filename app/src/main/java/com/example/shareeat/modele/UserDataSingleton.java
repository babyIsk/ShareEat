package com.example.shareeat.modele;

public class UserDataSingleton {
    private static UserDataSingleton instance;
    private Utilisateur utilisateur;

    private UserDataSingleton() {
        // Constructeur privé pour assurer le singleton
    }

    public static synchronized UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
