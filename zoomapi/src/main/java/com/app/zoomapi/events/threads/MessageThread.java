package com.app.zoomapi.extended;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.threads.EventFramework;
import com.app.zoomapi.models.Message;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//thread per channel per event
public class MessageThread extends Thread{
    private String channelName;
    private boolean isStop;
    //maintains the current list of messages for future comparison
    private List<Message> currentState;
    //ToDo: any other way to handle this?
    private ZoomClient client;

    public MessageThread(String channelName,ZoomClient client){
        this.channelName = channelName;
        this.currentState = new ArrayList<>();
        this.isStop = false;
        this.client = client;
        this.start();
    }

    @Override
    public void run() {
        while (!isStop){
            try {
                //waiting for 20s to poll the Zoom server
                Thread.sleep(20000);
                //get all messages
                List<Message> allMessages = getMessages();
                if(allMessages!=null){
                    //find new messages
                    List<Message> newMessages = findNewMessages(allMessages);
                    if(newMessages.size()>0){
                        //trigger new message event for each new message
                        for(Message message:newMessages) {
                            EventFramework.triggerNewMessageEvent(message);
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void stopThread(){
        this.isStop = true;
    }

    public String getChannelName(){
        return this.channelName;
    }

    private List<Message> getMessages(){
        //ToDo: should the date be passed from the client or do we need to consider the current date only?
        HttpResponse<Object> response = ((OAuthClient)client).getChat().history(channelName, LocalDate.of(2020,5,3),LocalDate.of(2020,5,3));
        int statusCode = response.statusCode();
        Object body = response.body();
        if(statusCode== 200){
            return (List<Message>)body;
        }
        return null;
    }

    private List<Message> findNewMessages(List<Message> allMessages){
       List<Message> newMessages = allMessages.stream().filter(x->
           this.currentState.stream().noneMatch(y->y.getMessageId().equals(x.getMessageId())
       )).collect(Collectors.toList());

       this.currentState = allMessages;
       return newMessages;
    }
}

//get all messages of the channel
// send it to event framework
//let the event framework process and trigger the events by calling event handlers