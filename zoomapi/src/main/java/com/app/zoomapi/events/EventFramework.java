package com.app.zoomapi.events;

import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.threads.MessageThread;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Message;
import jdk.jfr.EventType;

import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

public class EventFramework {
    //ToDo: chapter 15
    private static Map<String,List<Event>> updateMessageEventMap = new HashMap<>();
    private static Map<String,MessageThread> messageThreadMap = new HashMap<>();
    private static Map<String,List<Event>> newMessageEventMap = new HashMap<>();
    private ZoomClient client = null;

    public EventFramework(ZoomClient client){
        this.client = client;
    }

    //ToDo: see if we can put a check to see if the channel extists? else give an error message; return boolean
    public void  registerForNewMessageEvent(Consumer handler,String channelName){
        List<Event> events = newMessageEventMap.getOrDefault(channelName,new ArrayList());
        Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
        System.out.println(newEvent.getDateTimeGmt().toString());
        events.add(newEvent);
        newMessageEventMap.put(channelName,events);
        createMessageThreadForChannel(channelName);
    }

    public void  registerForUpdateMessageEvent(Consumer handler,String channelName){
        List<Event> events = updateMessageEventMap.getOrDefault(channelName,new ArrayList<Event>());
        Event newEvent = new Event(handler,LocalDateTime.now(ZoneId.of("GMT")));
        System.out.println(newEvent.getDateTimeGmt().toString());
        events.add(newEvent);
        updateMessageEventMap.put(channelName,events);
        createMessageThreadForChannel(channelName);
    }

    private void createMessageThreadForChannel(String channelName){
        if(!messageThreadMap.containsKey(channelName)){
            MessageThread newThread = new MessageThread(channelName,client);
            messageThreadMap.put(channelName,newThread);
        }
    }

    public void unRegisterFromNewMessageEvent(Consumer handler,String channelName){
        List<Event> events = newMessageEventMap.get(channelName);
        if(events!=null){
            for(Event event:events){
                if(event.getHandler()==handler){
                    events.remove(event);
                    break;
                }
            }
        }
        removeMessageThread(channelName);
    }

    public void unRegisterFromUpdateMessageEvent(Consumer handler,String channelName){
        List<Event> events = updateMessageEventMap.get(channelName);
        if(events!=null){
            for(Event event:events){
                if(event.getHandler()==handler){
                    events.remove(event);
                    break;
                }
            }
        }
        removeMessageThread(channelName);
    }

    private void removeMessageThread(String channelName){
        int newMessageCount = newMessageEventMap.getOrDefault(channelName,new ArrayList<>()).size();
        int updateMessageCount = updateMessageEventMap.getOrDefault(channelName,new ArrayList<>()).size();

        if(newMessageCount == 0 && updateMessageCount == 0){
            messageThreadMap.get(channelName).stopThread();
            messageThreadMap.remove(channelName);
        }
    }

    //ToDo: null pointer exception
    public static void triggerNewMessageEvent(Message message, String channelName){
        for(Event event:newMessageEventMap.get(channelName)){
            LocalDateTime messageTime = LocalDateTime.parse(message.getDateTime().substring(0,message.getDateTime().length()-1));
            if(!messageTime.isBefore(event.getDateTimeGmt())) {
                event.getHandler().accept(message);
            }
        }
    }

    public static void triggerUpdateMessageEvent(Message message,String channelName){
        for(Event event:updateMessageEventMap.get(channelName)){
            LocalDateTime messageTime = LocalDateTime.parse(message.getDateTime().substring(0,message.getDateTime().length()-1));
            if(!messageTime.isBefore(event.getDateTimeGmt())) {
                event.getHandler().accept(message);
            }
        }
    }


}

