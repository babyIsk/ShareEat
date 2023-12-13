package com.example.shareeat.modele;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;;

public class Message {
    private int IdSender;
    private int IdReceiver;
    private String Message;
    private Timestamp  date;

    public Message(int idSender, int idReceiver, String message) {
        IdSender = idSender;
        IdReceiver = idReceiver;
        Message = message;
        this.date = new Timestamp (System.currentTimeMillis());
    }

    public Message(int idSender, int idReceiver, String message, Timestamp date) {
        IdSender = idSender;
        IdReceiver = idReceiver;
        Message = message;
        this.date = date;
    }

    public int getIdSender() {
        return IdSender;
    }

    public int getIdReceiver() {
        return IdReceiver;
    }

    public String getMessage() {
        return Message;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
        return dateFormat.format(date);
    }
}
