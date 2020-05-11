package com.app.zoomapi.events.threads;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.clients.ZoomClient;
import com.app.zoomapi.events.process.ProcessMemberEvents;
import com.app.zoomapi.models.Member;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MemberThread extends Thread{
    private String channelName;
    private boolean isStop;
    private List<Member> currentState;
    private ZoomClient client;
    private ProcessMemberEvents processMemberEvents = null;

    public MemberThread(String channelName,ZoomClient client){
        this.channelName = channelName;
        this.currentState = new ArrayList<>();
        this.isStop = false;
        this.client = client;
        this.processMemberEvents = new ProcessMemberEvents();
        this.start();
    }

    @Override
    public void run() {
        while (!isStop){
            try {
                //waiting for 10s to poll the Zoom server
                Thread.sleep(10000);
                //get all members
                List<Member> allMembers = getMembers();
                if(allMembers!=null){
                    this.processMemberEvents.findNewMembers(allMembers,this.currentState,this.channelName);
                    currentState = allMembers;
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

    private List<Member> getMembers(){
        HttpResponse<Object> response = ((OAuthClient)client).getMembers().details(channelName);
        int statusCode = response.statusCode();
        Object body = response.body();
        if(statusCode== 200){
            return (List<Member>)body;
        }
        return null;
    }
}
