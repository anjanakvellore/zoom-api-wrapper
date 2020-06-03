package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;
import com.mashape.unirest.http.ObjectMapper;

import javax.net.ssl.SSLSession;
import javax.swing.text.html.HTMLDocument;
import java.awt.image.AreaAveragingScaleFilter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Zoom.us REST API Java client - Chat Channels Component
 * Component dealing with all chat channel related matters
 */
public class ChatChannelsComponent extends BaseComponent {

    private static ChatChannelsComponent chatChannelsComponent = null;

    /**
     * Create a new chat channel component
     * @param config config details
     */
    private ChatChannelsComponent(Map<String,String> config){
        super(config);
    }

    /**
     * Creates an instance of Chat Channels Component and returns it
     * @param config config details
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
        try {
            return getRequest("/chat/users/me/channels", null, null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }

    }

    /**
     * delete a channel
     * @param pathMap URL path parameters
     * @return Response object of the request
     */
    public HttpResponse<String> delete(Map<String,Object> pathMap){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            return deleteRequest(String.format( "/chat/channels/%s",pathMap.get("channel_id")),null,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * Create new channel
     * @param dataMap The data as hashmap to include with the request
     * @return Response object for this request
     */
    public HttpResponse<String> createChannel(Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"name","type"});
        try{
            //if(Utility.requireKeys(dataMap,reqKeys)) {
            Utility.requireKeys(dataMap,reqKeys);
            return postRequest("/chat/users/me/channels", null,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * get the channel details
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> get(Map<String,Object> pathMap){
        List<String> reqKeys = Arrays.asList(new String[]{"channel_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            return getRequest(String.format("/chat/channels/%s",pathMap.get("channel_id")),null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
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
        try{
            Utility.requireKeys(pathMap, pathKeys);
            Utility.requireKeys(dataMap,dataKeys);
            return patchRequest(String.format("/chat/channels/%s",pathMap.get("channel_id")),null,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * list channel members
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> listChannelMembers(Map<String,Object> pathMap,Map<String,Object> initialParamMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format( "/chat/channels/%s/members",pathMap.get("channel_id")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
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
        try{
            Utility.requireKeys(pathMap,pathKeys);
            Utility.requireKeys(dataMap,dataKeys);
            return postRequest(String.format( "/chat/channels/%s/members",pathMap.get("channel_id")),null,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * join channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> joinChannel(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            return postRequest(String.format("/chat/channels/%s/members/me",pathMap.get("channel_id")),null,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * leave channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> leaveChannel(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            return deleteRequest(String.format("/chat/channels/%s/members/me",pathMap.get("channel_id")),null,null,(String)null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    /**
     * remove a member from the channel
     * @param pathMap URL path parameters
     * @return Response object for the request
     */
    public HttpResponse<String> remove(Map<String,Object> pathMap){
        List<String> pathKeys = Arrays.asList(new String[]{"channel_id","member_id"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            return deleteRequest(String.format( "/chat/channels/%s/members/%s",pathMap.get("channel_id"),pathMap.get("member_id")),null,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }
}