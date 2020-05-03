package com.app.zoomapi.models;

import java.util.function.Consumer;

public class Event {
    private Consumer handler;
    private String channelName;

    public Event(Consumer handler,String channelName){
        this.channelName = channelName;
        this.handler = handler;
    }

    public Consumer getHandler(){
        return this.handler;
    }

    public String getChannelName(){
        return this.channelName;
    }
}
