package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.*;

/**
 * Zoom.us REST API Java client - Chat Messages Component
 * Component dealing with all chat message related matters
 */
public class ChatMessagesComponent extends BaseComponent {
    private static ChatMessagesComponent chatMessagesComponent = null;

    /**
     * create a new chat message component
     * @param config config details
     */
    private ChatMessagesComponent(Map<String,String> config){
        super(config);
    }

    /**
     * Creates an instance of Chat messages Component and returns it
     * @param config config details
     * @return instance of Chat messages Component
     */
    public static ChatMessagesComponent getChatMessagesComponent(Map<String,String> config){
        if(chatMessagesComponent== null){
            chatMessagesComponent = new ChatMessagesComponent(config);
        }
        return chatMessagesComponent;
    }

    /**
     * list user's chat messages
     * @param pathMap URL path parameters
     * @param paramMap URL query parameters
     * @return Repsonse object of the request
     */
    public HttpResponse<String> list(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap,reqKeys)) {
            return getRequest(String.format( "/chat/users/%s/messages",pathMap.get("userId")),paramMap,null);
        }
        else {
            return null;
        }
    }

    /**
     * sends a message
     * @param dataMap The data as hashmap to include with the request
     * @return Repsonse object of the request
     */
    public HttpResponse<String> post(Map<String,Object> dataMap){
        List<String> dataKeys = Arrays.asList(new String[]{"message"});
        if(Utility.requireKeys(dataMap,dataKeys)) {
            return postRequest("/chat/users/me/messages",null,null,dataMap,null);
        }
        else {
            return null;
        }
    }

    /**
     * updates a message
     * @param pathMap URL path parameters
     * @param paramMap URL query parameters
     * @param dataMap The data as hashmap to include with the request
     * @return Repsonse object of the request
     */
    public HttpResponse<String> update(Map<String,Object> pathMap ,Map<String,String> paramMap, Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"messageId"});
        List<String> dataKeys = Arrays.asList(new String[]{"message"});
        if(Utility.requireKeys(pathMap,pathKeys) && Utility.requireKeys(dataMap,dataKeys)) {
            return putRequest(String.format("/chat/users/me/messages/%s",pathMap.get("messageId")),paramMap,null,dataMap,null);
        }
        else {
            return null;
        }
    }

    /**
     * deletes a message
     * @param pathMap URL path parameters
     * @return Response object of the request
     */
    public HttpResponse<String> delete(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> pathKeys = Arrays.asList(new String[]{"messageId"});
        if(Utility.requireKeys(pathMap,pathKeys)) {
            return deleteRequest(String.format("/chat/users/me/messages/%s",pathMap.get("messageId")),paramMap,null,(String) null,null);
        }
        else {
            return null;
        }
    }
}
