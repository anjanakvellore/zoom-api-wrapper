package com.app.zoomapi.events.threads;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.EventFramework;
import com.app.zoomapi.events.process.ProcessMessageEvents;
import com.app.zoomapi.models.Message;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MessageThread extends Thread{
    private String channelName;
    private boolean isStop;
    private List<Message> currentState;
    private ZoomClient client;
    private ProcessMessageEvents processMessageEvents = null;

    public MessageThread(String channelName,ZoomClient client){
        this.channelName = channelName;
        this.currentState = new ArrayList<>();
        this.isStop = false;
        this.client = client;
        System.out.println("Thread has started for channel "+channelName);
        this.processMessageEvents = new ProcessMessageEvents();
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
                    this.processMessageEvents.findNewMessages(allMessages,this.currentState,this.channelName);
                    this.processMessageEvents.findUpdatedMessages(allMessages,this.currentState,this.channelName);
                    currentState = allMessages;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void stopThread(){

        this.isStop = true;
        System.out.println("Stopping the thread for channel name "+channelName);
    }

    public String getChannelName(){
        return this.channelName;
    }

    private List<Message> getMessages(){
        //ToDo: should the date be passed from the client or do we need to consider the current date only?
        LocalDate date = LocalDate.now(ZoneId.of("GMT"));
        HttpResponse<Object> response = ((OAuthClient)client).getChat().history(channelName, LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth()),LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth()));
        int statusCode = response.statusCode();
        Object body = response.body();
        if(statusCode== 200){
            return (List<Message>)body;
        }
        return null;
    }


}

