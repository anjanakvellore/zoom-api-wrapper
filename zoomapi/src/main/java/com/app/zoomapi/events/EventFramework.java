package com.app.zoomapi.events;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.threads.MemberThread;
import com.app.zoomapi.events.threads.MessageThread;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Member;
import com.app.zoomapi.models.Message;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

/**
 * Event framework that handles new message, update message and new member
 * related subscription/unsubscription processes.
 */
public class EventFramework {
    private static Map<String,List<Event>> updateMessageEventMap = new HashMap<>();
    private static Map<String,MessageThread> messageThreadMap = new HashMap<>();
    private static Map<String,List<Event>> newMessageEventMap = new HashMap<>();
    private static List<Event> newMemberEventList = new ArrayList<>();
    private static MemberThread newMemberThread;
    private ZoomClient client = null;

    public EventFramework(ZoomClient client){
        this.client = client;
    }

    /**
     * Registers for new message event
     * @param handler Event handler
     * @param channelName
     * @return boolean depending on if the channel exists
     */
    public boolean registerForNewMessageEvent(Consumer handler,String channelName){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        if(channelsList.contains(channelName)) {
            List<Event> events = newMessageEventMap.getOrDefault(channelName, new ArrayList());
            Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
            events.add(newEvent);
            newMessageEventMap.put(channelName, events);
            createMessageThreadForChannel(channelName);
            return true;
        }
        else
            return false;
    }

    /**
     * Registers for update message event
     * @param handler Event handler
     * @param channelName
     * @return boolean depending on if the channel exists
     */
    public boolean registerForUpdateMessageEvent(Consumer handler,String channelName){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        if(channelsList.contains(channelName)){
            List<Event> events = updateMessageEventMap.getOrDefault(channelName,new ArrayList<Event>());
            Event newEvent = new Event(handler,LocalDateTime.now(ZoneId.of("GMT")));
            events.add(newEvent);
            updateMessageEventMap.put(channelName,events);
            createMessageThreadForChannel(channelName);
            return true;
        }
        else
            return false;
    }

    /**
     * Creates a message thread for given channel name
     * @param channelName
     */
    private void createMessageThreadForChannel(String channelName){
        if(!messageThreadMap.containsKey(channelName)){
            MessageThread newThread = new MessageThread(channelName,client);
            messageThreadMap.put(channelName,newThread);
        }
    }

    /**
     * Registers for new member event
     * @param handler event handler
     */
    public void registerForNewMemberEvent(Consumer handler){
        List<Event> events = newMemberEventList;
        Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
        events.add(newEvent);
        newMemberThread = new MemberThread(client);
    }

    /**
     * Unregisters from new message event
     * @param handler event handler
     * @param channelName
     */
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

    /**
     * Unregisters from update message event
     * @param handler event handler
     * @param channelName
     */
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

    /**
     * Removes a message thread associated with a given channel
     * @param channelName
     */
    private void removeMessageThread(String channelName){
        int newMessageCount = newMessageEventMap.getOrDefault(channelName,new ArrayList<>()).size();
        int updateMessageCount = updateMessageEventMap.getOrDefault(channelName,new ArrayList<>()).size();

        if(newMessageCount == 0 && updateMessageCount == 0){
            messageThreadMap.get(channelName).stopThread();
            messageThreadMap.remove(channelName);
        }
    }

    /**
     * Unregisters from new member event
     * @param handler event handler
     */
    public void unRegisterFromNewMemberEvent(Consumer handler){
        List<Event> events = newMemberEventList;
        if(events!=null){
            for(Event event:events){
                if(event.getHandler()==handler){
                    events.remove(event);
                    break;
                }
            }
        }
        if(events.size() == 0) {
            newMemberThread.stopThread();
        }
    }

    /**
     * Triggers new message event
     * @param message
     * @param channelName
     */
    public static void triggerNewMessageEvent(Message message, String channelName){
        if(newMessageEventMap.get(channelName) != null){
            for(Event event:newMessageEventMap.get(channelName)){
                LocalDateTime messageTime = LocalDateTime.parse(message.getDateTime().substring(0,message.getDateTime().length()-1));
                if(!messageTime.isBefore(event.getDateTimeGmt())) {
                    event.getHandler().accept(message);
                }
            }
        }
    }

    /**
     * Triggers update message event
     * @param message
     * @param channelName
     */
    public static void triggerUpdateMessageEvent(Message message,String channelName){
        if(updateMessageEventMap.get(channelName) != null){
            for(Event event:updateMessageEventMap.get(channelName)){
                LocalDateTime messageTime = LocalDateTime.parse(message.getDateTime().substring(0,message.getDateTime().length()-1));
                if(!messageTime.isBefore(event.getDateTimeGmt())) {
                    event.getHandler().accept(message);
                }
            }
        }
    }

    /**
     * Triggers new member event
     * @param member
     */
    public static void triggerNewMemberEvent(Member member){
        for(Event event:newMemberEventList){
            event.getHandler().accept(member);
        }
    }
}
