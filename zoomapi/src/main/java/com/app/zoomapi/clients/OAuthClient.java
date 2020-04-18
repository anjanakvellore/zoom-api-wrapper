package com.app.zoomapi.clients;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import org.apache.oltu.oauth2.common.token.OAuthToken;

import java.util.HashMap;
import java.util.Map;

public class OAuthClient extends ZoomClient {
    private Map<String,String> config;
    private ChatChannelsComponent chatChannelsComponent;
    private ChatMessagesComponent chatMessagesComponent;

    //ToDo: remove token : only for testing purpose
    public OAuthClient(String clientId, String clientSecret, int port, String redirectUrl, String browserPath,String dataType,Integer timeOut,String token){

        super(clientId,clientSecret,dataType!=null ? dataType:"json",timeOut!=null ? timeOut : 15);
        config = new HashMap<>();
        config.put("clientId",clientId);
        config.put("clientSecret",clientSecret);
        config.put("port",String.valueOf(port));
        config.put("redirectUrl",redirectUrl);
        config.put("browserPath",browserPath);
        //ToDo change this: create a function in util package that gets token
        config.put("token",token);

        //ToDo: should we create a hashmap like in Python?
        chatChannelsComponent = ChatChannelsComponent.getChatChannelsComponent(config);
        chatMessagesComponent = ChatMessagesComponent.getChatMessagesComponent(config);
    }

    public ChatChannelsComponent getChatChannelsComponent(){
        return chatChannelsComponent;
    }

    public ChatMessagesComponent getChatMessagesComponent(){
        return chatMessagesComponent;
    }
}
