package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
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
     * @param initialParamMap URL query parameters
     * @return Repsonse object of the request
     */
    public HttpResponse<String> list(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null) {
                paramMap = Utility.convertMap(initialParamMap);
            }
            return getRequest(String.format( "/chat/users/%s/messages",pathMap.get("userId")),paramMap,null);
        }catch (Exception ex){
            return new HttpResponse<String>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return ex.getMessage();
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };
        }
    }

    /**
     * sends a message
     * @param dataMap The data as hashmap to include with the request
     * @return Repsonse object of the request
     */
    public HttpResponse<String> post(Map<String,Object> dataMap){
        List<String> dataKeys = Arrays.asList(new String[]{"message"});
        try{
            Utility.requireKeys(dataMap,dataKeys);
            return postRequest("/chat/users/me/messages",null,null,dataMap,null);
        }catch (Exception ex){
            return new HttpResponse<String>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return ex.getMessage();
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };
        }
    }

    /**
     * updates a message
     * @param pathMap URL path parameters
     * @param initialParamMap URL query parameters
     * @param dataMap The data as hashmap to include with the request
     * @return Repsonse object of the request
     */
    public HttpResponse<String> update(Map<String,Object> pathMap ,Map<String,Object> initialParamMap, Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"messageId"});
        List<String> dataKeys = Arrays.asList(new String[]{"message"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            Utility.requireKeys(dataMap,dataKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return putRequest(String.format("/chat/users/me/messages/%s",pathMap.get("messageId")),paramMap,null,dataMap,null);
        }catch (Exception ex){
            return new HttpResponse<String>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return ex.getMessage();
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };
        }
    }

    /**
     * deletes a message
     * @param pathMap URL path parameters
     * @param initialParamMap URL path parameters
     * @return Response object of the request
     */
    public HttpResponse<String> delete(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> pathKeys = Arrays.asList(new String[]{"messageId"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return deleteRequest(String.format("/chat/users/me/messages/%s",pathMap.get("messageId")),paramMap,null,(String) null,null);
        }catch (Exception ex){
            return new HttpResponse<String>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return ex.getMessage();
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };
        }
    }
}
