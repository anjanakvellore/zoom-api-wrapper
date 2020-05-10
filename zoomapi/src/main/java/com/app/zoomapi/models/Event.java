package com.app.zoomapi.models;

import java.time.LocalDateTime;
import java.util.function.Consumer;

//ToDo : map between event and the handler??
public class Event {
    private Consumer handler;
    private LocalDateTime dateTimeGmt;

    public Event(Consumer handler, LocalDateTime dateTimeGmt){
        this.handler = handler;
       this.dateTimeGmt = dateTimeGmt;
    }

    public Consumer getHandler(){
        return this.handler;
    }

    public LocalDateTime getDateTimeGmt() {
        return dateTimeGmt;
    }
}
