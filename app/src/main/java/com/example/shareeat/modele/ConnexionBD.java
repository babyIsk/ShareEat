package com.example.shareeat.modele;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnexionBD {
    private static final String URL = "jdbc:mysql://mysql-shareeat.alwaysdata.net:3306/shareeat_bd";
    private static final String USER = "shareeat";
    private static final String PASSWORD = "GUqZtB#X@TG8d4U";
    private Connection conn;
    private PreparedStatement pStmInscritpion;
    private PreparedStatement pStmConnexion;
    private PreparedStatement pStmMessage;
    private PreparedStatement pStmEnvoieMessage;
    private PreparedStatement pStmTousIngredients;
    private PreparedStatement pStmAjoutIngr;
    private PreparedStatement pStmUpdateStatus;
    private PreparedStatement pStmUpdateIngr;
    private PreparedStatement pStmDeleteIngr;
    private PreparedStatement pStmAddPlat;
    private PreparedStatement pStmRecette;


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
        pStmEnvoieMessage = conn.prepareStatement("INSERT INTO Messagerie (IdSender,IdReceiver,Message,Heure) VALUES (?,?,?,NOW()) ");
        pStmConnexion = conn.prepareStatement("SELECT * FROM Utilisateurs WHERE Mail = ? AND Mdp = PASSWORD(?)");
        pStmMessage = conn.prepareStatement("SELECT * FROM Messagerie WHERE (IdSender = ? AND IdReceiver = ?) OR (IdSender = ? AND IdReceiver = ?) ORDER BY IdMessage ASC");
        pStmTousIngredients = conn.prepareStatement("SELECT * FROM Ingredients");
        pStmAjoutIngr = conn.prepareStatement("INSERT INTO Ingredients (statusCheck, nom) VALUES (0, ?)");
        pStmUpdateStatus = conn.prepareStatement("UPDATE Ingredients SET statusCheck = ? WHERE idIngredient = ?");
        pStmUpdateIngr = conn.prepareStatement("UPDATE Ingredients SET nom = ? WHERE idIngredient = ?");
        pStmDeleteIngr = conn.prepareStatement("DELETE FROM Ingredients WHERE idIngredient = ?");
        pStmAddPlat = conn.prepareStatement("INSERT INTO Recette (IdUtilisateur, Titre, Description, Date, ImageRecette) VALUES (?, ?, ?, ?, ?)");
        pStmRecette = conn.prepareStatement("SELECT * FROM Recette WHERE IdRecette = ?");
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
                        resultSet.getInt("IdUtilisateur"),
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

    public List<Message> getMessage(int userId, int contactId){
        List<Message> messages = new ArrayList<>();
        try {
            pStmMessage.setInt(1, userId);
            pStmMessage.setInt(2, contactId);
            pStmMessage.setInt(3, contactId);
            pStmMessage.setInt(4, userId);
            ResultSet resultSet = pStmMessage.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("IdSender"),resultSet.getInt("IdReceiver"),resultSet.getString("Message"),resultSet.getTimestamp("Heure"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message sendMessage(int userId, int contactId,String message){
        try {
            pStmEnvoieMessage.setInt(1,userId);
            pStmEnvoieMessage.setInt(2,contactId);
            pStmEnvoieMessage.setString(3,message);
            this.pStmEnvoieMessage.executeUpdate();
            Message m = new Message(userId, contactId,message);
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ingredient> getTousIngredients() throws SQLException {
        List<Ingredient> listeIngrs = new ArrayList<Ingredient>();
        ResultSet res = this.pStmTousIngredients.executeQuery();
        while (res.next()) {
            int id = res.getInt("idIngredient");
            int status = res.getInt("statusCheck");
            String nom = res.getString("nom");
            listeIngrs.add(new Ingredient(id, status, nom));
        }
        return listeIngrs;
    }

    public void ajouterIngr(String nom) throws SQLException  {
        this.pStmAjoutIngr.setString(1, nom);
        this.pStmAjoutIngr.executeUpdate();
    }

    public void updateStatusIngr(int status, int idIngredient) throws SQLException {
        System.out.println("Updating status for ingredient " + idIngredient + " to " + status);
        this.pStmUpdateStatus.setInt(1, status);
        this.pStmUpdateStatus.setInt(2, idIngredient);
        this.pStmUpdateStatus.executeUpdate();
    }

    public void updateIngredient(int idIngredient, String nom) throws SQLException {
        this.pStmUpdateIngr.setString(1, nom);
        this.pStmUpdateIngr.setInt(2, idIngredient);
        this.pStmUpdateIngr.executeUpdate();
    }

    public void deleteIngredient(int idIngredient) throws SQLException {
        this.pStmDeleteIngr.setInt(1, idIngredient);
        this.pStmDeleteIngr.executeUpdate();
    }

    public void ajouterRecette(String titre, String description, String date, String imageUri) throws SQLException {
        try {
            // Remplacez 1 par l'ID de l'utilisateur actuel (à récupérer depuis votre système de connexion utilisateur)
            pStmAddPlat.setInt(1, 1);
            pStmAddPlat.setString(2, titre);
            pStmAddPlat.setString(3, description);
            pStmAddPlat.setString(4, date);
            pStmAddPlat.setString(5, imageUri);
            pStmAddPlat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Plat getRecetteById(int idP) {
            try {
                pStmRecette.setInt(1, idP);

                ResultSet res = this.pStmRecette.executeQuery();
                if (res.next()) {
                    // Récupérer les données de la ligne
                    int idUtilisateur = res.getInt("IdUtilisateur");
                    String titre = res.getString("Titre");
                    String description = res.getString("Description");
                    String date = res.getString("Date");
                    String imageRecette = res.getString("ImageRecette");

                    // Créer et retourner un objet Recette avec les données récupérées
                    Plat plat = new Plat();
                    plat.setIdP(idP);
                    plat.setIdUtilisateur(idUtilisateur);
                    plat.setTitreP(titre);
                    plat.setDescriptionP(description);
                    plat.setDate(date);
                    plat.setImgRecette(imageRecette);
                    return plat;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        return null; // en cas d'erreur ou si aucune recette n'est trouvée
    }
}
