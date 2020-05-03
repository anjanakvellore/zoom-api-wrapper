package com.app.zoomapi.extended;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.models.Message;
import org.apache.oltu.oauth2.common.domain.client.ClientInfo;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageThread extends Thread{
    private String channelName;
    private boolean isStop;
    private List<Message> currentState;
    //any other way to handle this?
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
                Thread.sleep(20000);
                List<Message> allMessages = getMessages();
                if(allMessages!=null){
                    List<Message> newMessages = findNewMessages(allMessages);
                    if(newMessages.size()>0){
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
