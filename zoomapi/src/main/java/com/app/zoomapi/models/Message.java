package com.app.zoomapi.models;

public class Message {
    private String message;
    private  String sender;
    private String dateTime;

    public Message(String message,String sender,String dateTime){
        this.message = message;
        this.sender = sender;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getDateTime() {
        return dateTime;
    }
}
