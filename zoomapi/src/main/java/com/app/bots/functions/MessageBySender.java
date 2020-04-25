package com.app.bots.functions;

import com.app.zoomapi.models.IFunction;
import com.app.zoomapi.models.Message;

import java.util.List;

public class MessageBySender implements IFunction {
    private String sender;
    public MessageBySender(String sender){
        this.sender = sender;
    }
    @Override
    public Object call(Object arg) {
        ((List<Message>)arg).removeIf(message -> !message.getSender().equals(this.sender));
        return arg;
    }
}
