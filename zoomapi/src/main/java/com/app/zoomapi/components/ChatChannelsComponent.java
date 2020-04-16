package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;
import com.mashape.unirest.http.ObjectMapper;

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

    /*public HttpResponse<String> createChannel(Map<String,String> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"channels"});
        if(Utility.requireKeys(variableArguments,reqKeys)) {
            Map<String, Object> varArgs = new HashMap<>();
            varArgs.put("data", variableArguments.get("channels"));
            return postRequest("/chat/users/me/channels", varArgs);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> get(Map<String,Object> variableArguments){
            List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
            if(Utility.requireKeys(variableArguments,reqKeys))
                return getRequest(String.format("/chat/channels/{0}",variableArguments.get("channel_id")),null,null);
            else
                return null;
    }

    public HttpResponse<String> update(Map<String,Object> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id", "name"});
        if(Utility.requireKeys(variableArguments, reqKeys)){
            Map<String, Object> varArgs = new HashMap<>();
            varArgs.put("data", variableArguments.get("name"));
            return patchRequest(String.format("/chat/channels/{0}",variableArguments.get("channel_id")),varArgs);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> delete(Map<String,Object> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(variableArguments,reqKeys)){
            return deleteRequest(String.format( "/chat/channels/{0}",variableArguments.get("channel_id")));
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> listChannelMembers(Map<String,Object> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(variableArguments,reqKeys)){
            return getRequest(String.format( "/chat/channels/{0}/members",variableArguments.get("channel_id")),null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> inviteChannelMembers(Map<String,Object> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id","members"});
        if(Utility.requireKeys(variableArguments,reqKeys)){
            Map<String, Object> varArgs = new HashMap<>();
            varArgs.put("data", variableArguments.get("members"));
            return postRequest(String.format( "/chat/channels/{0}/members",variableArguments.get("channel_id")),varArgs);
        }
        else{
            return null;
        }
    }*/


}
