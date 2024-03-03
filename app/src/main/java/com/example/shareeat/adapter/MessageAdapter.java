package com.example.shareeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.shareeat.R;
import com.example.shareeat.modele.Message;
import com.example.shareeat.modele.Utilisateur;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    private Context context;
    private List<Message> messages;
    private Utilisateur user;

    public MessageAdapter(Context context, List<Message> messages, Utilisateur user) {
        this.context = context;
        this.messages = messages;
        this.user = user;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return (message.getIdSender() == user.getIdUtilisateur()) ? TYPE_MESSAGE_SENT : TYPE_MESSAGE_RECEIVED;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messages.get(position);
        int viewType = getItemViewType(position);

        // Si la vue est réutilisée, convertView n'est pas null, sinon, inflate la vue appropriée
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (viewType == TYPE_MESSAGE_SENT) {
                convertView = inflater.inflate(R.layout.message_send, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.message_receive, parent, false);
            }
        }

        // Récupération des éléments de la vue
        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        // Mise à jour de l'interface utilisateur avec les données du message
        tvMessage.setText(message.getMessage());
        tvDate.setText(message.getDate().toString());  // Mettez à jour avec le format de date approprié

        return convertView;
    }
}
