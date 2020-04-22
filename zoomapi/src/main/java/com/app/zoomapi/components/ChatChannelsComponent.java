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

/**
 * Zoom.us REST API Java client - Chat Channels Component
 * Component dealing with all chat channel related matters
 */
public class ChatChannelsComponent extends BaseComponent {

    private static ChatChannelsComponent chatChannelsComponent = null;

    /**
     * Create a new chat channel component
     * @param config The config details
     */
    private ChatChannelsComponent(Map<String,String> config){
        super(config);
    }

    /**
     * Creates an instance of Chat Channels Component and returns it
     * @param config The config details
     * @return instance of Chat Channels Component
     */
    public static ChatChannelsComponent getChatChannelsComponent(Map<String,String> config){
        if(chatChannelsComponent == null){
            chatChannelsComponent = new ChatChannelsComponent(config);
        }
        return chatChannelsComponent;
    }

    /**
     * lists all channels
     * @return Repsonse object of the request
     */
    public HttpResponse<String> list(){
        //TODO check with Kaj, include everywhere
        getRateLimiter().acquire();
        return getRequest("/chat/users/me/channels",null,null);
    }

    /**
     * Delete a channel
     * @param pathMap URL path parameters
     * @return Response object of the request
     */
    public HttpResponse<String> delete(Map<String,Object> pathMap){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return deleteRequest(String.format( "/chat/channels/%s",pathMap.get("channel_id")),null,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    /**
     * Create new channel
     * @param dataMap The data as hashmap to include with the request
     * @return Response object for this request
     */
    public HttpResponse<String> createChannel(Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"name","type"});
        if(Utility.requireKeys(dataMap,reqKeys)) {
            return postRequest("/chat/users/me/channels", null,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    /**
     * get the channel details
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> get(Map<String,Object> pathMap){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(pathMap,reqKeys))
            return getRequest(String.format("/chat/channels/%s",pathMap.get("channel_id")),null,null);
        else
            return null;
    }

    /**
     * update the channel name
     * @param pathMap URL path parameters
     * @param dataMap The data as hashmap to include with the request
     * @return Response object for the request
     */
    public HttpResponse<String> update(Map<String,Object> pathMap,Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        List<String> dataKeys = Arrays.asList(new String[]{"name"});
        if(Utility.requireKeys(pathMap, pathKeys) && Utility.requireKeys(dataMap,dataKeys)){
            return patchRequest(String.format("/chat/channels/%s",pathMap.get("channel_id")),null,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    /**
     * list channel members
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> listChannelMembers(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(pathMap,pathKeys)){
            return getRequest(String.format( "/chat/channels/%s/members",pathMap.get("channel_id")),null,null);
        }
        else{
            return null;
        }
    }

    /**
     * invite channel members
     * @param pathMap URL path parameters
     * @param dataMap The data as hashmap to include with the request
     * @return Response object for the request
     */
    public HttpResponse<String> inviteChannelMembers(Map<String,Object> pathMap,Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        List<String> dataKeys = Arrays.asList(new String[]{"members"});
        if(Utility.requireKeys(pathMap,pathKeys) && Utility.requireKeys(dataMap,dataKeys)){
            return postRequest(String.format( "/chat/channels/%s/members",pathMap.get("channel_id")),null,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    /**
     * join channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> joinChannel(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(pathMap,pathKeys)){
            return postRequest(String.format("/chat/channels/%s/members/me",pathMap.get("channel_id")),null,null, (String) null,null);
        }
        else {
            return null;
        }
    }

    /**
     * leave channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> leaveChannel(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        if(Utility.requireKeys(pathMap,pathKeys)){
            return postRequest(String.format("/chat/channels/%s/members/me",pathMap.get("channel_id")),null,null,(String)null,null);
        }
        else{
            return null;
        }
    }

    /**
     * remove a member from the channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> remove(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id","member_id"});
        if(Utility.requireKeys(pathMap,pathKeys)){
            return deleteRequest(String.format( "/chat/channels/%s/members/%s",pathMap.get("channel_id"),pathMap.get("member_id")),null,null, (String) null,null);
        }
        else{
            return null;
        }
    }
}