package com.app.zoomapi.extended;

import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventFramework {
    static List<Consumer> newMessageHandler = new ArrayList<>();
    static List<Consumer> updateMessageHandler = new ArrayList<>();
    static List<MessageThread> messageThreads = new ArrayList<>();

    public void registerForNewMessageEvent(Event event, ZoomClient client){
        newMessageHandler.add(event.getHandler());
        createMessageThreadForChannel(event.getChannelName(),client);
    }

    public void unregisterFromNewMessageEvent(Event event){
        newMessageHandler.remove(event.getHandler());
        //removeMessageThread

    }

    public void registerForUpdateMessageEvent(Consumer handler){
        updateMessageHandler.add(handler);
    }

    public void unregisterForUpdateMessageEvent(Consumer handler){
        updateMessageHandler.remove(handler);
        /*if(updateMessageHandler.size() == 0){
            messageThread.stopThread();
        }*/
    }

    private void createMessageThreadForChannel(String channelName, ZoomClient client){
        if(messageThreads.stream().noneMatch(x->x.getChannelName().equals(channelName))){
            messageThreads.add(new MessageThread(channelName,client));
        }
    }

    protected static void triggerNewMessageEvent(Message message){
        for(Consumer handler:newMessageHandler){
            handler.accept(message);
        }
    }

    protected static void triggerUpdateMessageEvent(List<Message> messages){
        for(Consumer handler:updateMessageHandler){
            handler.accept(messages);
        }
    }
}

