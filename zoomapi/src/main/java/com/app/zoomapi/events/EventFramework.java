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
    private static Map<String,List<Event>> updateMessageEventMap = new HashMap<>();
    private static Map<String,MessageThread> messageThreadMap = new HashMap<>();
    private static Map<String,List<Event>> newMessageEventMap = new HashMap<>();
    private static List<Event> newMemberEventList = new ArrayList<>();
    private static MemberThread newMemberThread;
    private ZoomClient client = null;

    public EventFramework(ZoomClient client){
        this.client = client;
    }

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
        List<Event> events = newMemberEventList;
        Event newEvent = new Event(handler, LocalDateTime.now(ZoneId.of("GMT")));
        events.add(newEvent);
        newMemberThread = new MemberThread(client);
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
        List<Event> events = newMemberEventList;
        if(events!=null){
            for(Event event:events){
                if(event.getHandler()==handler){
                    events.remove(event);
                    break;
                }
            }
        }
        newMemberThread.stopThread();
    }

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

    public static void triggerNewMemberEvent(Member member){
        for(Event event:newMemberEventList){
            event.getHandler().accept(member);
        }
    }
}
