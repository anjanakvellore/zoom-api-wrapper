package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;
import com.mashape.unirest.http.ObjectMapper;

import javax.swing.text.html.HTMLDocument;
import java.awt.image.AreaAveragingScaleFilter;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatChannelsComponent extends BaseComponent {

    private static ChatChannelsComponent chatChannelsComponent = null;

    private ChatChannelsComponent(Map<String,String> config){
        super(config);
    }

    public static ChatChannelsComponent getChatChannelsComponent(Map<String,String> config){
        if(chatChannelsComponent == null){
            chatChannelsComponent = new ChatChannelsComponent(config);
        }
        return chatChannelsComponent;
    }

    public HttpResponse<String> list(){
        return getRequest("/chat/users/me/channels",null,null);
    }

    public HttpResponse<String> delete(Map<String,Object> path){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(path,reqKeys)){
            return deleteRequest(String.format( "/chat/channels/%s",path.get("channel_id")),null,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> createChannel(Map<String,Object> data){
        List<String> reqKeys = Arrays.asList(new String[]{"name","type"});
        if(Utility.requireKeys(data,reqKeys)) {
            return postRequest("/chat/users/me/channels", null,null,data,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> get(Map<String,Object> path){
            List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
            if(Utility.requireKeys(path,reqKeys))
                return getRequest(String.format("/chat/channels/%s",path.get("channel_id")),null,null);
            else
                return null;
    }

    public HttpResponse<String> update(Map<String,Object> path,Map<String,Object> data){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        List<String> dataKeys = Arrays.asList(new String[]{"name"});
        if(Utility.requireKeys(path, pathKeys) && Utility.requireKeys(data,dataKeys)){
            return patchRequest(String.format("/chat/channels/%s",path.get("channel_id")),null,null,data,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> listChannelMembers(Map<String,Object> path){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(path,pathKeys)){
            return getRequest(String.format( "/chat/channels/%s/members",path.get("channel_id")),null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> inviteChannelMembers(Map<String,Object> path,Map<String,Object> data){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        List<String> dataKeys = Arrays.asList(new String[]{"members"});
        if(Utility.requireKeys(path,pathKeys) && Utility.requireKeys(data,dataKeys)){
            return postRequest(String.format( "/chat/channels/%s/members",path.get("channel_id")),null,null,data,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> joinChannel(Map<String,Object> path){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(path,pathKeys)){
            return postRequest(String.format("/chat/channels/%s/members/me",path.get("channel_id")),null,null, (String) null,null);
        }
        else {
            return null;
        }
    }

    public HttpResponse<String> leaveChannel(Map<String,Object> path){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(path,pathKeys)){
            return postRequest(String.format("/chat/channels/%s/members/me",path.get("channel_id")),null,null,(String)null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> remove(Map<String,Object> path){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id","member_id"});
        if(Utility.requireKeys(path,pathKeys)){
            return deleteRequest(String.format( "/chat/channels/%s/members/%s",path.get("channel_id"),path.get("member_id")),null,null, (String) null,null);
        }
        else{
            return null;
        }
    }



}
