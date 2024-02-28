package com.example.shareeat.modele;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
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
    private PreparedStatement pStmAddPlat;
    private PreparedStatement pStmRecetteById;
    private PreparedStatement pStmGetLatestRecipeId;
    private PreparedStatement pStmGetUtilisateur;
    PreparedStatement pStmUpdateUser;
    private PreparedStatement pStmRecetteByIdAndDate;
    private PreparedStatement pStmRecetteAccueil;
    private PreparedStatement pStmDerniereConversation;
    private PreparedStatement pStmDernierMessage;
    private PreparedStatement pStmCommentaireParPoste;
    private PreparedStatement pStmSendCommentaire;
    private PreparedStatement pStmLike;
    private PreparedStatement pStmUnLike;
    private PreparedStatement pStmCompteCommentaire;
    private PreparedStatement pStmLikeExists;


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
        pStmAddPlat = conn.prepareStatement("INSERT INTO Recette (IdUtilisateur, Titre, Description, Date, ImageRecette) VALUES (?, ?, ?, ?, ?)");
        pStmRecetteById = conn.prepareStatement("SELECT * FROM Recette WHERE IdRecette = ?");
        pStmGetLatestRecipeId = conn.prepareStatement("SELECT LAST_INSERT_ID() AS LatestRecipeId");
        pStmGetUtilisateur = conn.prepareStatement("SELECT * FROM Utilisateurs WHERE IdUtilisateur = ?");
        pStmRecetteAccueil = conn.prepareStatement("SELECT * FROM Recette WHERE IdUtilisateur <> ? ORDER BY IdRecette DESC");
        pStmUpdateUser = conn.prepareStatement("UPDATE Utilisateurs SET Prenom = ?, Pseudo = ?, Bio = ?, Photo = ? WHERE IdUtilisateur = ?");
        pStmRecetteByIdAndDate = conn.prepareStatement("SELECT * FROM Recette WHERE IdUtilisateur = ? AND Date = ?");
        pStmDerniereConversation = conn.prepareStatement("SELECT U.*\n" +
                "FROM Utilisateurs U\n" +
                "LEFT JOIN (\n" +
                "    SELECT IdReceiver AS IdUtilisateur\n" +
                "    FROM Messagerie\n" +
                "    WHERE IdSender = ?\n" +
                "    UNION\n" +
                "    SELECT IdSender AS IdUtilisateur\n" +
                "    FROM Messagerie\n" +
                "    WHERE IdReceiver = ?) M  ON U.IdUtilisateur = M.IdUtilisateur \n" +
                "    WHERE U.IdUtilisateur <> ?\n" +
                "\tORDER BY M.IdUtilisateur DESC , U.Pseudo ASC;");
        pStmDernierMessage = conn.prepareStatement("SELECT *\n" +
                "FROM Messagerie\n" +
                "WHERE (IdSender = ? AND IdReceiver = ?) OR (IdSender = ? AND IdReceiver = ?)\n" +
                "ORDER BY Heure DESC\n" +
                "LIMIT 1;\n");
        pStmLike = conn.prepareStatement("Insert Into A_like (IdRecette, IdUtilisateur) values (?,?)");
        pStmUnLike = conn.prepareStatement("DELETE FROM A_like (IdRecette, IdUtilisateur) values (?,?)");
        pStmCommentaireParPoste = conn.prepareStatement("SELECT * FROM Commentaires WHERE IdRecette = ? ORDER BY Date DESC");
        pStmSendCommentaire =  conn.prepareStatement("Insert Into Commentaires (IdUtilisateur, IdRecette, Contenu, Date) VALUES (?, ?, ?, NOW())");
        pStmCompteCommentaire = conn.prepareStatement("SELECT COUNT(*) FROM Commentaires WHERE IdRecette = ?");
        pStmUnLike = conn.prepareStatement("DELETE FROM `A_like` WHERE `A_like`.`IdUtilisateur` = ? AND `A_like`.`IdRecette` = ?");
        pStmLikeExists = conn.prepareStatement("SELECT * FROM A_like WHERE IdUtilisateur = ? AND IdRecette = ?");
    }

    public void fermerConnexion() {
        /*try {
            // Close all prepared statements
            if (pStmInscritpion != null) pStmInscritpion.close();
            if (pStmEnvoieMessage != null) pStmEnvoieMessage.close();
            if (pStmConnexion != null) pStmConnexion.close();
            if (pStmMessage != null) pStmMessage.close();
            if (pStmTousIngredients != null) pStmTousIngredients.close();
            if (pStmAjoutIngr != null) pStmAjoutIngr.close();
            if (pStmUpdateStatus != null) pStmUpdateStatus.close();
            if (pStmUpdateIngr != null) pStmUpdateIngr.close();
            if (pStmDeleteIngr != null) pStmDeleteIngr.close();
            if (pStmAddPlat != null) pStmAddPlat.close();
            if (pStmRecetteById != null) pStmRecetteById.close();
            if (pStmGetLatestRecipeId != null) pStmGetLatestRecipeId.close();
            if (pStmGetUtilisateur != null) pStmGetUtilisateur.close();
            if (pStmUpdateUser != null) pStmUpdateUser.close();
            if (pStmRecetteByIdAndDate != null) pStmRecetteByIdAndDate.close();
            if (pStmRecetteAccueil != null) pStmRecetteAccueil.close();
            if (pStmDerniereConversation != null) pStmDerniereConversation.close();
            if (pStmDernierMessage != null) pStmDernierMessage.close();

            // Do not close the database connection here
            // if (conn != null && !conn.isClosed()) {
            //     conn.close();
            //     System.out.println("Connexion fermée avec succès.");
            // }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }*/
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
            fermerConnexion();
            return inscrit;
        } catch (SQLException e) {
            System.out.println("Erreur impossible de procéder");
            e.printStackTrace();
        }
        fermerConnexion();
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
                        resultSet.getString("Mdp"),
                        resultSet.getString("Bio"),
                        resultSet.getString("Photo")
                );
                System.out.println("Utilisateur trouvé : " + utilisateur);
                fermerConnexion();
                return utilisateur;
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email et ce mot de passe.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion");
            e.printStackTrace();
        }
        fermerConnexion();
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
            // Fermer le ResultSet ici
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }/* finally {
            // Fermer la connexion ici
            fermerConnexion();
        }*/
        return messages;
    }


    public Message getDernierMessage(int userId, int contactId) {
        try {
            pStmDernierMessage.setInt(1, userId);
            pStmDernierMessage.setInt(2, contactId);
            pStmDernierMessage.setInt(3, contactId);
            pStmDernierMessage.setInt(4, userId);
            ResultSet resultSet = pStmDernierMessage.executeQuery();
            if (resultSet.next()) {
                Message message = new Message(resultSet.getInt("IdSender"), resultSet.getInt("IdReceiver"),
                        resultSet.getString("Message"), resultSet.getTimestamp("Heure"));
                fermerConnexion();
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }


    public List<Plat> getRecetteAccueil(int userId){
        List<Plat> plats = new ArrayList<>();
        try {
            pStmRecetteAccueil.setInt(1, userId);  // Utilisation de la déclaration pStmRecetteAccueil spécifique aux recettes
            ResultSet resultSet = pStmRecetteAccueil.executeQuery();
            while (resultSet.next()) {
                Plat plat = new Plat(resultSet.getInt("IdRecette"),
                        resultSet.getInt("IdUtilisateur"),
                        resultSet.getString("Titre"),
                        resultSet.getString("Description"),
                        resultSet.getDate("Date"),
                        resultSet.getString("ImageRecette"),
                        resultSet.getString("Ingredient"));
                plats.add(plat);

                if (plats.isEmpty()) {
                    Log.d("PlatDebug", "La liste de plats est vide");
                } else {
                    for (Plat plast : plats) {
                        Log.d("PlatDebug", "Titre du plat : " + plast.getTitreP());
                    }
                }
            }
            fermerConnexion();
            return plats;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }

    public List<Utilisateur> getMP(int userId){
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            pStmDerniereConversation.setInt(1, userId);
            pStmDerniereConversation.setInt(2, userId);
            pStmDerniereConversation.setInt(3, userId);
            ResultSet resultSet = pStmDerniereConversation.executeQuery();
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getInt("IdUtilisateur"),
                        resultSet.getString("Prenom"),
                        resultSet.getString("Nom"),
                        resultSet.getString("Pseudo"),
                        resultSet.getString("Mail"),
                        resultSet.getString("Mdp"),
                        resultSet.getString("Bio"),
                        resultSet.getString("Photo")
                );
                utilisateurs.add(utilisateur);
            }
            return utilisateurs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }


    public List<Commentaire> getCommenaire(int recetteId){
        List<Commentaire> commentaires = new ArrayList<>();
        try {
            pStmCommentaireParPoste.setInt(1, recetteId);
            ResultSet resultSet = pStmCommentaireParPoste.executeQuery();
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire(
                        resultSet.getInt("IdCommentaire"),
                        resultSet.getInt("IdUtilisateur"),
                        resultSet.getInt("IdRecette"),
                        resultSet.getString("Contenu"),
                        resultSet.getTimestamp("Date")
                );
                commentaires.add(commentaire);
            }
            return commentaires;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }


    public int getNombreCommentaire(int recetteId) {
        try {
            pStmCompteCommentaire.setInt(1, recetteId);
            ResultSet resultSet = pStmCompteCommentaire.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fermerConnexion();
        }
        return 0;
    }



    public Message sendMessage(int userId, int contactId,String message){
        try {
            pStmEnvoieMessage.setInt(1,userId);
            pStmEnvoieMessage.setInt(2,contactId);
            pStmEnvoieMessage.setString(3,message);
            this.pStmEnvoieMessage.executeUpdate();
            Message m = new Message(userId, contactId,message);
            fermerConnexion();
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }

    public Commentaire sendCommentaire(int userId, int recetteId, String contenu) {
        try {
            pStmSendCommentaire.setInt(1, userId);
            pStmSendCommentaire.setInt(2, recetteId);
            pStmSendCommentaire.setString(3, contenu);
            this.pStmSendCommentaire.executeUpdate();

            // Obtenir la date et l'heure actuelles
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // Créer un nouvel objet Commentaire avec la date actuelle
            Commentaire commentaire = new Commentaire(userId, recetteId, contenu, timestamp);

            return commentaire;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }

    public void ajouterRecette(int idUtilisateur, String titre, String description, Date date, String imageUri, String ingredients) throws SQLException {
        try {
            Log.d("ConnexionBD", "Ajout de recette - ID utilisateur : " + idUtilisateur);
            pStmAddPlat.setInt(1, idUtilisateur);
            pStmAddPlat.setString(2, titre);
            pStmAddPlat.setString(3, description);

            pStmAddPlat.setDate(4, new java.sql.Date(date.getTime())); // Utilise java.sql.Date pour la compatibilité avec JDBC
            pStmAddPlat.setString(5, imageUri);
            pStmAddPlat.setString(6, ingredients);

            pStmAddPlat.executeUpdate();
            Log.d("ConnexionBD", "Recette ajoutée avec succès !");
        } catch (SQLException e) {
            Log.e("ConnexionBD", "Erreur lors de l'ajout de la recette : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public Plat getRecetteById(int idP) {
            try {
                pStmRecetteById.setInt(1, idP);

                ResultSet res = this.pStmRecetteById.executeQuery();
                if (res.next()) {
                    // Récupérer les données de la ligne
                    int idUtilisateur = res.getInt("IdUtilisateur");
                    String titre = res.getString("Titre");
                    String description = res.getString("Description");
                    Date date = res.getDate("Date");
                    String imageRecette = res.getString("ImageRecette");
                    String ingredients = res.getString("Ingredient");

                    // Créer et retourner un objet Recette avec les données récupérées
                    Plat plat = new Plat(idP, idUtilisateur, titre, description, date, imageRecette, ingredients);

                    plat.setIdP(idP);
                    plat.setIdUtilisateur(idUtilisateur);
                    plat.setTitreP(titre);
                    plat.setDescriptionP(description);
                    plat.setDate(date);
                    plat.setImgRecette(imageRecette);
                    plat.setIngrédients(ingredients);
                    fermerConnexion();
                    return plat;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        fermerConnexion();
        return null; // en cas d'erreur ou si aucune recette n'est trouvée
    }

    public int getLatestRecipeId() {
        int latestRecipeId = -1;

        try {
            ResultSet res = this.pStmGetLatestRecipeId.executeQuery();
            if (res.next()) {
                latestRecipeId = res.getInt("LatestRecipeId");
            }
            pStmGetLatestRecipeId.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return latestRecipeId;
    }

    public Utilisateur getUtilisateurById(int userId) {
        try {
            pStmGetUtilisateur.setInt(1, userId);
            ResultSet resultSet = pStmGetUtilisateur.executeQuery();
            if (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getInt("IdUtilisateur"),
                        resultSet.getString("Prenom"),
                        resultSet.getString("Nom"),
                        resultSet.getString("Pseudo"),
                        resultSet.getString("Mail"),
                        resultSet.getString("Mdp"),
                        resultSet.getString("Bio"),
                        resultSet.getString("Photo")
                );
                fermerConnexion();
                return utilisateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }

    public void updateUtilisateur(Utilisateur utilisateurConnecte) {
        try {
            // Définir les paramètres de la requête avec les nouvelles valeurs
            pStmUpdateUser.setString(1, utilisateurConnecte.getPrenom());
            pStmUpdateUser.setString(2, utilisateurConnecte.getPseudo());
            pStmUpdateUser.setString(3, utilisateurConnecte.getBio());
            pStmUpdateUser.setString(4, utilisateurConnecte.getPhoto());
            pStmUpdateUser.setInt(5, utilisateurConnecte.getIdUtilisateur());
            pStmUpdateUser.executeUpdate();

            pStmUpdateUser.close();
            Log.d("ConnexionBD", "Utilisateur mis à jour avec succès dans la base de données.");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("ConnexionBD", "Erreur lors de la mise à jour de l'utilisateur dans la base de données.");
        }

        fermerConnexion();
    }
    public List<Plat> getTousRecetteByIdUser(Utilisateur utilisateur, ArrayList<String> dates) {
        List<Plat> recettes = new ArrayList<>();
        try {
            for (String formattedDate : dates) {
                Log.d("ConnexionBD", "Formatted Date : " + formattedDate);
                pStmRecetteByIdAndDate.setInt(1, utilisateur.getIdUtilisateur());
                pStmRecetteByIdAndDate.setString(2, formattedDate);
                Log.d("ConnexionBD", "Requête SQL : " + pStmRecetteByIdAndDate);
                ResultSet res = pStmRecetteByIdAndDate.executeQuery();

                while (res.next()) {
                    int idRecette = res.getInt("IdRecette");
                    String titre = res.getString("Titre");
                    String description = res.getString("Description");
                    Date dateRecette = res.getDate("Date");
                    String imageRecette = res.getString("ImageRecette");
                    String ingredients = res.getString("Ingredient");

                    Plat plat = new Plat(idRecette, utilisateur.getIdUtilisateur(), titre, description, dateRecette, imageRecette, ingredients);
                    plat.setIdP(idRecette);
                    plat.setIdUtilisateur(utilisateur.getIdUtilisateur());
                    plat.setTitreP(titre);
                    plat.setDescriptionP(description);
                    plat.setDate(dateRecette);
                    plat.setImgRecette(imageRecette);
                    plat.setIngrédients(ingredients);

                    recettes.add(plat);
                }
            }
        } catch (SQLException e) {
            Log.e("ConnexionBD", "Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        Log.d("ConnexionBD", "Fin de la méthode getTousRecetteByIdUser");
        fermerConnexion();

        return recettes;
    }

    public void like(int userId, int recetteId){
        try {
            pStmLike.setInt(2,userId);
            pStmLike.setInt(1,recetteId);
            this.pStmLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Unlike(int userId, int recetteId){
        try {
            pStmUnLike.setInt(1,userId);
            pStmUnLike.setInt(2,recetteId);
            this.pStmUnLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean likeExists(int userId, int recetteId) {
        boolean likeExists = false;

        try {
            pStmLikeExists.setInt(1, userId);
            pStmLikeExists.setInt(2, recetteId);

            ResultSet resultSet = pStmLikeExists.executeQuery();
            //true ou false en fonction de si ça renvoie qqc ou pas
            likeExists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return likeExists;
    }


}
