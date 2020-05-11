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

public class EventFramework {
    //ToDo: chapter 15
    private static Map<String,List<Event>> updateMessageEventMap = new HashMap<>();
    private static Map<String,MessageThread> messageThreadMap = new HashMap<>();
    private static Map<String,List<Event>> newMessageEventMap = new HashMap<>();
    private static Map<String,List<Event>> newMemberEventMap = new HashMap<>();
    private static Map<String,MemberThread> memberThreadMap = new HashMap<>();
    private ZoomClient client = null;

    public EventFramework(ZoomClient client){
        this.client = client;
    }

    //ToDo: Boolean ok?
    public boolean registerForNewMessageEvent(Consumer handler,String channelName){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        if(channelsList.contains(channelName)) {
            List<Event> events = newMessageEventMap.getOrDefault(channelName, new ArrayList());
            Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
            System.out.println(newEvent.getDateTimeGmt().toString());
            events.add(newEvent);
            newMessageEventMap.put(channelName, events);
            createMessageThreadForChannel(channelName);
            return true;
        }
        else
            return false;
    }

    //ToDo: Boolean ok?
    public boolean registerForUpdateMessageEvent(Consumer handler,String channelName){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        if(channelsList.contains(channelName)){
            List<Event> events = updateMessageEventMap.getOrDefault(channelName,new ArrayList<Event>());
            Event newEvent = new Event(handler,LocalDateTime.now(ZoneId.of("GMT")));
            System.out.println(newEvent.getDateTimeGmt().toString());
            events.add(newEvent);
            updateMessageEventMap.put(channelName,events);
            createMessageThreadForChannel(channelName);
            return true;
        }
        else
            return false;
    }

    private void createMessageThreadForChannel(String channelName){
        if(!messageThreadMap.containsKey(channelName)){
            MessageThread newThread = new MessageThread(channelName,client);
            messageThreadMap.put(channelName,newThread);
        }
    }

    //ToDo: anything to handle? user not part of any channel
    public void registerForNewMemberEvent(Consumer handler){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        for(String channelName:channelsList){
            List<Event> events = newMemberEventMap.getOrDefault(channelName,new ArrayList());
            Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
            events.add(newEvent);
            newMemberEventMap.put(channelName,events);
            createMemberThreadForChannel(channelName);
        }
    }

    private void createMemberThreadForChannel(String channelName){
        if(!memberThreadMap.containsKey(channelName)){
            MemberThread newThread = new MemberThread(channelName,client);
            memberThreadMap.put(channelName,newThread);
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

    public void unRegisterFromNewMemberEvent(Consumer handler){
        List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
        for(String channelName:channelsList){
            List<Event> events = newMemberEventMap.get(channelName);
            if(events!=null){
                for(Event event:events){
                    if(event.getHandler()==handler){
                        events.remove(event);
                        break;
                    }
                }
            }
            removeMemberThread(channelName);
        }
    }

    private void removeMemberThread(String channelName){
        int newMemberCount = newMemberEventMap.getOrDefault(channelName,new ArrayList<>()).size();

        if(newMemberCount == 0){
            memberThreadMap.get(channelName).stopThread();
            memberThreadMap.remove(channelName);
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

    public static void triggerNewMemberEvent(Member member,String channelName){
        for(Event event:newMemberEventMap.get(channelName)){
            event.getHandler().accept(member);
        }
    }
}
