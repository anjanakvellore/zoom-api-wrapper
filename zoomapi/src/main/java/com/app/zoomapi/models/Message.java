package com.app.zoomapi.models;

/**
 * models the Message object
 */
public class Message {
    private String message;
    private  String sender;
    private String dateTime;

    public Message(String message,String sender,String dateTime){
        this.message = message;
        this.sender = sender;
        this.dateTime = dateTime;
    }

    /**
     * gets the message text
     * @return message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * gets the sender email address
     * @return sender email address
     */
    public String getSender() {
        return sender;
    }

    /**
     * gets the date and time of the message
     * @return date and time value
     */
    public String getDateTime() {
        return dateTime;
    }
}
