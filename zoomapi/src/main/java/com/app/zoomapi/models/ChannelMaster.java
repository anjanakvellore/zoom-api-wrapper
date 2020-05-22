package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

public class ChannelMaster {
    @PrimaryKey
    private int channelId;
    private String zoomChannelId;
    private String channelName;
    private int type;

    public ChannelMaster(int channelId, String zoomChannelId, String channelName, int type) {
        this.channelId = channelId;
        this.zoomChannelId = zoomChannelId;
        this.channelName = channelName;
        this.type = type;
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

    public int getType() {
        return type;
    }

}
