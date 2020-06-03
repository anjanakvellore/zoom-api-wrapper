package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

/**
 * Models the Messages table in the database.
 */
public class Messages {
    @PrimaryKey
    private int channelId;
    @PrimaryKey
    private String zoomMessageId;
    private String message;
    private String sender;
    private String dateTime;
    private String zoomTimeStamp;
    private String timeStamp;

    public Messages(int channelId, String zoomMessageId, String message,String sender, String dateTime,
                    String zoomTimeStamp, String timeStamp) {
        this.channelId = channelId;
        this.zoomMessageId = zoomMessageId;
        this.message = message;
        this.sender = sender;
        this.dateTime = dateTime;
        this.zoomTimeStamp = zoomTimeStamp;
        this.timeStamp = timeStamp;
    }

    public Messages (String zoomMessageId, String message,String sender, String dateTime,
                     String zoomTimeStamp, String timeStamp) {
        this.zoomMessageId = zoomMessageId;
        this.message = message;
        this.sender = sender;
        this.dateTime = dateTime;
        this.zoomTimeStamp = zoomTimeStamp;
        this.timeStamp = timeStamp;
    }

    public Messages(){
    }

    public int getChannelId() {
        return channelId;
    }

    public String getZoomMessageId() {
        return zoomMessageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){ this.message = message;}

    public String getSender() {
        return sender;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getZoomTimeStamp() {
        return zoomTimeStamp;
    }

    public String getTimeStamp(){
        return timeStamp;
    }
}
