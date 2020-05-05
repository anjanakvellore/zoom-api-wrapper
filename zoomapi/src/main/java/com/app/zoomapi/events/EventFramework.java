package com.app.zoomapi.events;

import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.extended.MessageThread;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventFramework {
    static List<Consumer> newMessageHandler = new ArrayList<>();
    static List<Consumer> updateMessageHandler = new ArrayList<>();
    static List<MessageThread> messageThreads = new ArrayList<>();

    //registering for new message event
    public void registerForNewMessageEvent(Event event, ZoomClient client){
        newMessageHandler.add(event.getHandler());
        //create a new thread for the channel; new thread will be created only if there is no existing thread for the channel
        //ToDo: Is thread per channel per event the right way to do?
        //ToDo: Message-Channel; Members-Channel
        createMessageThreadForChannel(event.getChannelName(),client);
    }

    //unsubscribing from the new message event
    public void unregisterFromNewMessageEvent(Event event){
        newMessageHandler.remove(event.getHandler());
        //ToDo: remove the thread for this channel only if there are no other events registered for this channel
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

    //this will call the callback function
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

