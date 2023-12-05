package com.example.shareeat.modele;

import java.sql.Date;

public class Message {
    private int IdSender;
    private int IdReceiver;
    private String Message;
    private Date date;

    public Message(int idSender, int idReceiver, String message) {
        IdSender = idSender;
        IdReceiver = idReceiver;
        Message = message;
        this.date = new Date(System.currentTimeMillis());
    }

    public Message(int idSender, int idReceiver, String message, Date date) {
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

    public Date getDate() {
        return date;
    }
}
