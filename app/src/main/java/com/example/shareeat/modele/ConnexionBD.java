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

    // Déclarations des variables pour la connexion à la base de données
    private static final String URL = "jdbc:mysql://mysql-shareeat.alwaysdata.net:3306/shareeat_bd";
    private static final String USER = "shareeat";
    private static final String PASSWORD = "GUqZtB#X@TG8d4U";
    private Connection conn;

    // Déclarations des objets PreparedStatement pour les requêtes SQL
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
    private PreparedStatement pStmGetLike;


    public ConnexionBD() throws SQLException, ClassNotFoundException {
        // Initialisation de la connexion à la base de données et préparation des requêtes SQL
        this.conn = seConnecterBD();
        this.preparerRequetes();
    }

    public Connection seConnecterBD() throws SQLException, ClassNotFoundException {
        // Établir la connexion à la base de données
        Class.forName("com.mysql.jdbc.Driver");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return DriverManager.getConnection(this.URL, USER, PASSWORD);
    }

    private void preparerRequetes() throws SQLException {
        // Préparer les requêtes SQL
        // Chaque requête est préparée avec un objet PreparedStatement pour être exécutée plus tard
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
        pStmGetLike = conn.prepareStatement("SELECT R.* FROM Recette R INNER JOIN A_like L ON R.IdRecette = L.IdRecette WHERE L.IdUtilisateur = ?");
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
        // Inscription d'un nouvel utilisateur dans la base de donnée
        try {
            // Définir les paramètres de la requête avec les valeurs fournies
            this.pStmInscritpion.setString(1, pseudo);
            this.pStmInscritpion.setString(2, prenom);
            this.pStmInscritpion.setString(3, nom);
            this.pStmInscritpion.setString(4, email);
            this.pStmInscritpion.setString(5, mdp);
            // Executer la requête
            this.pStmInscritpion.executeUpdate();
            // Créer l'utilisateur avec les paramètres fournis
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
        // Authentifier les utilisateur dans la base de donnée
        try {
            // Définir les paramètres de la requête
            this.pStmConnexion.setString(1, email);
            this.pStmConnexion.setString(2, mdp);
            // Exécuter la requête
            ResultSet resultSet = this.pStmConnexion.executeQuery();
            // Vérifier si un utilisateur a été trouvé
            if (resultSet.next()) {
                // Créer l'utilisateur avec les bons paramètres
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
        // Récupérer les messages entre un utilisateur et un contact donnés
        List<Message> messages = new ArrayList<>();
        try {
            // Définir les paramètres de la requête
            pStmMessage.setInt(1, userId);
            pStmMessage.setInt(2, contactId);
            pStmMessage.setInt(3, contactId);
            pStmMessage.setInt(4, userId);
            // Exécuter la requête
            ResultSet resultSet = pStmMessage.executeQuery();

            // Créer des messages à partir des données récupérées
            // et les ajouter dans la liste après
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
        // Récupérer le dernier message
        try {
            // Définir les paramètres de la requête
            pStmDernierMessage.setInt(1, userId);
            pStmDernierMessage.setInt(2, contactId);
            pStmDernierMessage.setInt(3, contactId);
            pStmDernierMessage.setInt(4, userId);
            // Executer la requête
            ResultSet resultSet = pStmDernierMessage.executeQuery();

            // Vérifier si un message a été trouvé
            if (resultSet.next()) {
                // Créer le message avec les bons paramètres
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
        // Récupérer les plats pour l'acceuil
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
                // Ajouter le plat à la liste des plats
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
        // Récupérer les utilisateurs avec lesquels l'utilisateur a eu une conversation
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            // Préparer la requête puis l'executer
            pStmDerniereConversation.setInt(1, userId);
            pStmDerniereConversation.setInt(2, userId);
            pStmDerniereConversation.setInt(3, userId);
            ResultSet resultSet = pStmDerniereConversation.executeQuery();
            // Parcourir les résultats et créer des utilisateirs avec les bons paramères
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
        //Récupérer les commentaires d'une recette spécifique
        List<Commentaire> commentaires = new ArrayList<>();
        try {
            // Préparer et executer la requête
            pStmCommentaireParPoste.setInt(1, recetteId);
            ResultSet resultSet = pStmCommentaireParPoste.executeQuery();
            // Parcourir les résultats et ajouter chaque commentaire à la liste
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
            // Préparer et executer la requête
            pStmCompteCommentaire.setInt(1, recetteId);
            ResultSet resultSet = pStmCompteCommentaire.executeQuery();
            // Vérifier s'il y a des résultats
            if (resultSet.next()) {
                // Récupérer et retourner le nombre de commentaires
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fermerConnexion();
        }
        // Retourner 0 si aucun commentaire n'est trouvé ou s'il y a une erreur
        return 0;
    }



    public Message sendMessage(int userId, int contactId,String message){
        try {
            // Préaprer et executer la requête qui ajoute un nouveau message
            pStmEnvoieMessage.setInt(1,userId);
            pStmEnvoieMessage.setInt(2,contactId);
            pStmEnvoieMessage.setString(3,message);
            this.pStmEnvoieMessage.executeUpdate();
            // Créer un nouveau message
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
            // Préparer et executer la requête qui ajoute un nouveau commentaire
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
            // Préparer et executer la requête qui ajoute une nouvelle recette
            Log.d("ConnexionBD", "Ajout de recette - ID utilisateur : " + idUtilisateur);
            pStmAddPlat.setInt(1, idUtilisateur);
            pStmAddPlat.setString(2, titre);
            pStmAddPlat.setString(3, description);

            pStmAddPlat.setDate(4, new java.sql.Date(date.getTime())); // Utilise java.sql.Date pour la compatibilité avec JDBC
            pStmAddPlat.setString(5, imageUri);
            pStmAddPlat.setString(6, ingredients);

            pStmAddPlat.executeUpdate();
            // Afficher un message de succès dans les logs
            Log.d("ConnexionBD", "Recette ajoutée avec succès !");
        } catch (SQLException e) {
            // En cas d'erreur, afficher un message d'erreur dans les logs
            Log.e("ConnexionBD", "Erreur lors de l'ajout de la recette : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public Plat getRecetteById(int idP) {
        // récuprer les recette à partir de son id
            try {

                // Préparer et executer la requête
                pStmRecetteById.setInt(1, idP);

                // Vérifier s'il y a un résultat
                ResultSet res = this.pStmRecetteById.executeQuery();
                if (res.next()) {
                    // Récupérer les données du résultat
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
        // Obtenir l'ID de la dernière recette ajoutée à la base de données
        int latestRecipeId = -1; // -1 par défaut

        try {
            // Préparer et executer la requête qui récupère la dernière recette ajoutée
            ResultSet res = this.pStmGetLatestRecipeId.executeQuery();
            if (res.next()) {
                latestRecipeId = res.getInt("LatestRecipeId");
            }
            pStmGetLatestRecipeId.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        // Retourner l'ID de la dernière recette ajoutée
        return latestRecipeId;
    }

    public Utilisateur getUtilisateurById(int userId) {
        // Obenir un utilisateur à partir de son ID
        try {
            // Préparer et executer la requête
            pStmGetUtilisateur.setInt(1, userId);
            ResultSet resultSet = pStmGetUtilisateur.executeQuery();
            // Vérifier s'il y a un résultat
            if (resultSet.next()) {
                // Récupérer les informations de l'utilisateur
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
                // Retourner l'utilisateur trouvé
                return utilisateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return null;
    }

    public void updateUtilisateur(Utilisateur utilisateurConnecte) {
        // Mettre à jour les informations d'un utilisateur dans la base de données.
        try {
            // Définir les paramètres de la requête avec les nouvelles valeurs
            pStmUpdateUser.setString(1, utilisateurConnecte.getPrenom());
            pStmUpdateUser.setString(2, utilisateurConnecte.getPseudo());
            pStmUpdateUser.setString(3, utilisateurConnecte.getBio());
            pStmUpdateUser.setString(4, utilisateurConnecte.getPhoto());
            pStmUpdateUser.setInt(5, utilisateurConnecte.getIdUtilisateur());
            pStmUpdateUser.executeUpdate();
            // Executer la requête
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
        // Obtenir toutes les recettes d'un utilisateur à partir de la base de données
        List<Plat> recettes = new ArrayList<>();
        try {
            // Parcourir toutes les dates fournies
            for (String formattedDate : dates) {
                Log.d("ConnexionBD", "Formatted Date : " + formattedDate);
                // Préparer et executer la requête
                pStmRecetteByIdAndDate.setInt(1, utilisateur.getIdUtilisateur());
                pStmRecetteByIdAndDate.setString(2, formattedDate);
                Log.d("ConnexionBD", "Requête SQL : " + pStmRecetteByIdAndDate);
                ResultSet res = pStmRecetteByIdAndDate.executeQuery();

                // Parcourir les résultats
                while (res.next()) {
                    int idRecette = res.getInt("IdRecette");
                    String titre = res.getString("Titre");
                    String description = res.getString("Description");
                    Date dateRecette = res.getDate("Date");
                    String imageRecette = res.getString("ImageRecette");
                    String ingredients = res.getString("Ingredient");

                    // Créer un plat avec les bons paramètres récupérés
                    Plat plat = new Plat(idRecette, utilisateur.getIdUtilisateur(), titre, description, dateRecette, imageRecette, ingredients);
                    plat.setIdP(idRecette);
                    plat.setIdUtilisateur(utilisateur.getIdUtilisateur());
                    plat.setTitreP(titre);
                    plat.setDescriptionP(description);
                    plat.setDate(dateRecette);
                    plat.setImgRecette(imageRecette);
                    plat.setIngrédients(ingredients);

                    // Ajouteer le plat à la liste
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
        // Aimer un plat
        try {
            // Prépare et execute la requête
            pStmLike.setInt(2,userId);
            pStmLike.setInt(1,recetteId);
            this.pStmLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Unlike(int userId, int recetteId){
        // Ne plus aimer un plat
        try {
            // Prépare et execute la requête
            pStmUnLike.setInt(1,userId);
            pStmUnLike.setInt(2,recetteId);
            this.pStmUnLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean likeExists(int userId, int recetteId) {
        // Vérifier si un like exsit ou pas sur un plat
        boolean likeExists = false;

        try {
            // Préparer et executer la requête
            pStmLikeExists.setInt(1, userId);
            pStmLikeExists.setInt(2, recetteId);

            ResultSet resultSet = pStmLikeExists.executeQuery();
            //true ou false en fonction de si ça renvoie quelquechose ou pas
            likeExists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fermerConnexion();
        return likeExists;
    }

    public List<Plat> getPlatLike(int userId) {
        // Obtenir la liste des plats aimés par un utilisateur donné
        List<Plat> plats = new ArrayList<>();
        try {
            // Préparer et executer la requête
            pStmGetLike.setInt(1, userId);
            ResultSet resultSet = pStmGetLike.executeQuery();
            while (resultSet.next()) {
                // Parcourir les résultats de la requête et ajouter les plats aimés à la liste
                Plat plat = new Plat(
                        resultSet.getInt("IdRecette"),
                        resultSet.getInt("IdUtilisateur"),
                        resultSet.getString("Titre"),
                        resultSet.getString("Description"),
                        resultSet.getDate("Date"),
                        resultSet.getString("ImageRecette"),
                        resultSet.getString("Ingredient")
                );
                plats.add(plat);
            }
            resultSet.close();
            pStmGetLike.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }


}
