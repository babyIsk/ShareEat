package com.example.shareeat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Message;
import com.example.shareeat.adapter.MessageAdapter;
import com.example.shareeat.modele.UserDataSingleton;
import com.example.shareeat.modele.Utilisateur;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Handler handler;
    private Utilisateur user;
    private Utilisateur contact;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private TextView tvUserName;
    private ImageView ppUserContactM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        user = UserDataSingleton.getInstance().getUtilisateur();
        try {
            contact = new ConnexionBD().getUtilisateurById(getIntent().getIntExtra("utilisateurId", 0));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        ppUserContactM = findViewById(R.id.ppUserContact);
            if (contact.getPhoto() != null && !contact.getPhoto().isEmpty()) {
                Picasso.get().load("https://shareeat.alwaysdata.net/photoProfil/"+contact.getPhoto()).into(ppUserContactM);
            } else {
                // Si l'utilisateur n'a pas de photo, affiche une image par défaut
                ppUserContactM.setImageResource(R.drawable.profil_picture);
            }

        // Initialise la liste des messages et l'adaptateur
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, user);

        tvUserName = findViewById(R.id.tvUserName);

        tvUserName.setText(contact.getPseudo());

        ListView listView = findViewById(R.id.lvMessages);
        listView.setAdapter(messageAdapter);

        ViewTreeObserver viewTreeObserver = listView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Supprimez le listener pour éviter d'appeler à plusieurs reprises
                listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Faites défiler la ListView vers le bas
                listView.smoothScrollToPosition(messageAdapter.getCount() - 1);
            }
        });

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        demarrerMessagerie(user, contact);

        // Gérer le clic sur le bouton "Envoyer"
        Button btnSend = findViewById(R.id.btnSend);
        EditText etMessage = findViewById(R.id.etMessage);

        btnSend.setOnClickListener(v -> {
            String messageText = etMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                // Envoyer le message
                if (envoyerMessage(user, contact, messageText) != null) {
                    // Effacer le champ de texte après l'envoi du message
                    etMessage.setText("");
                }
            }
        });
    }

    public void demarrerMessagerie(Utilisateur user, Utilisateur contact) {
        executorService.execute(() -> {
            try {
                ConnexionBD bd = new ConnexionBD();
                while (true) {
                    List<Message> nouveauxMessages = bd.getMessage(user.getIdUtilisateur(), contact.getIdUtilisateur());

                    // Mettre à jour l'interface utilisateur sur le thread principal
                    handler.post(() -> {
                        // Ajouter les nouveaux messages à la liste
                        messageList.clear();
                        messageList.addAll(nouveauxMessages);
                        // Rafraîchir l'adaptateur
                        messageAdapter.notifyDataSetChanged();

                        // Faire défiler la ListView vers le bas

                    });

                    Thread.sleep(500);
                }
            } catch (SQLException | ClassNotFoundException | InterruptedException e) {
                handler.post(() -> {
                    Toast.makeText(MessageActivity.this, "Erreur de connexion, impossible de récupérer les messages", Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }


    public Message envoyerMessage(Utilisateur user, Utilisateur contact, String message) {
        try {
            Message nouveauMessage = new ConnexionBD().sendMessage(user.getIdUtilisateur(), contact.getIdUtilisateur(), message);
            if (nouveauMessage != null) {
                // Ajouter le nouveau message à la liste et mettre à jour l'interface utilisateur
                handler.post(() -> {
                    messageList.add(nouveauMessage);
                    messageAdapter.notifyDataSetChanged();
                });

                ListView listView = findViewById(R.id.lvMessages);
                listView.smoothScrollToPosition(messageList.size() - 1);
                return nouveauMessage;
            }
        } catch (SQLException | ClassNotFoundException e) {
            handler.post(() -> {
                Toast.makeText(MessageActivity.this, "Erreur de connexion, impossible d'envoyer le message", Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }
        return null;
    }
}
