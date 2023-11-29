package com.example.shareeat.modele;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnexionBD {
    private static final String URL = "jdbc:mysql://mysql-shareeat.alwaysdata.net:3306/shareeat_bd";
    private static final String USER = "shareeat";
    private static final String PASSWORD = "GUqZtB#X@TG8d4U";
    private Connection conn;
    private PreparedStatement pStmInscritpion;
    private PreparedStatement pStmConnexion;

    public ConnexionBD() throws SQLException, ClassNotFoundException {
        this.conn = seConnecterBD();
        this.preparerRequetes();
    }

    public Connection seConnecterBD() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return DriverManager.getConnection(this.URL, USER, PASSWORD);
    }

    private void preparerRequetes() throws SQLException {
        pStmInscritpion = conn.prepareStatement("INSERT INTO `Utilisateurs` (`Pseudo`, `Prenom`, `Nom`, `Mail`,`Mdp`) VALUES (?, ?, ?, ?, PASSWORD(?));");
        pStmConnexion = conn.prepareStatement("SELECT * FROM Utilisateurs WHERE Mail = ? AND Mdp = PASSWORD(?);");
    }

    public Utilisateur inscription(String pseudo, String nom, String prenom, String email, String mdp) {
        try {
            this.pStmInscritpion.setString(1, pseudo);
            this.pStmInscritpion.setString(2, prenom);
            this.pStmInscritpion.setString(3, nom);
            this.pStmInscritpion.setString(4, email);
            this.pStmInscritpion.setString(5, mdp);
            this.pStmInscritpion.executeUpdate();

            Utilisateur inscrit = new Utilisateur(prenom, nom, pseudo, email, mdp);
            System.out.println("***********************" + inscrit + "*******************************************");
            return inscrit;
        } catch (SQLException e) {
            System.out.println("Erreur impossible de procéder");
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateur connexion(String email, String mdp) {
        try {
            this.pStmConnexion.setString(1, email);
            this.pStmConnexion.setString(2, mdp);
            ResultSet resultSet = this.pStmConnexion.executeQuery();
            if (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getString("Prenom"),
                        resultSet.getString("Nom"),
                        resultSet.getString("Pseudo"),
                        resultSet.getString("Mail"),
                        resultSet.getString("Mdp")
                );
                System.out.println("Utilisateur trouvé : " + utilisateur);
                return utilisateur;
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email et ce mot de passe.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion");
            e.printStackTrace();
        }
        return null;
    }

}
