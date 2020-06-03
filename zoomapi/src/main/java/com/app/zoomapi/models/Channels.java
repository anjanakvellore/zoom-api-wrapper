package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

public class Channels {
    public String getZoomClientId() {
        return zoomClientId;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @PrimaryKey
    private String zoomClientId;
    @PrimaryKey
    private int channelId;
    private String timeStamp;

    public Channels(String zoomClientId,int channelId,String timeStamp){
        this.zoomClientId = zoomClientId;
        this.channelId = channelId;
        this.timeStamp = timeStamp;
    }

    //TODO remove?
    public Channels(){

    }

}
