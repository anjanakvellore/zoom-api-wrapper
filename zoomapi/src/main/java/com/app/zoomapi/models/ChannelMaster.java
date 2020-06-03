package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

/**
 * Models the ChannelMaster table in the database.
 */
public class ChannelMaster {
    @PrimaryKey
    private int channelId;
    private String zoomChannelId;
    private String channelName;
    private int type;
    private String timeStamp;

    public ChannelMaster(int channelId, String zoomChannelId, String channelName, int type,String timeStamp) {
        this.channelId = channelId;
        this.zoomChannelId = zoomChannelId;
        this.channelName = channelName;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public ChannelMaster(String zoomChannelId, String channelName, int type,String timeStamp){
        this.zoomChannelId = zoomChannelId;
        this.channelName = channelName;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public ChannelMaster(){

    }

    public int getChannelId() {
        return channelId;
    }

    public String getZoomChannelId() {
        return zoomChannelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String name){this.channelName = name;}

    public int getType() {
        return type;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

}
