package com.app.zoomapi.events.threads;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.process.ProcessMemberEvents;
import com.app.zoomapi.models.Member;
import com.app.zoomapi.models.Result;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberThread extends Thread{
    private boolean isStop;
    private HashMap<String,List<Member>> currentState;
    private ZoomClient client;
    private ProcessMemberEvents processMemberEvents = null;

    public MemberThread(ZoomClient client){
        this.currentState = new HashMap<>();
        this.isStop = false;
        this.client = client;
        this.processMemberEvents = new ProcessMemberEvents();
        this.start();
    }

    @Override
    public void run() {
        while (!isStop){
            try {
                Thread.sleep(10000);
                List<String> channelsList = ((OAuthClient)client).getMembers().getUserChannels();
                if(channelsList!=null){
                    for(String channelName:channelsList){
                        System.out.println(channelName);
                        List<Member> allMembers = getMembers(channelName);
                        if(allMembers!=null){
                            this.processMemberEvents.findNewMembers(allMembers,this.currentState.get(channelName));
                            this.currentState.put(channelName,allMembers);
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
        System.out.println("Stopping the thread for finding new members");
    }


    private List<Member> getMembers(String channelName){
        Result result = ((OAuthClient)client).getMembers().details(channelName);
        int statusCode = result.getStatus();
        if(statusCode== 200){
            Object body = result.getData();
            return (List<Member>)body;
        }
        return null;
    }
}
