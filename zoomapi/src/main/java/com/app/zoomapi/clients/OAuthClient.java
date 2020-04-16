package com.app.zoomapi.clients;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import org.apache.oltu.oauth2.common.token.OAuthToken;

import java.util.HashMap;
import java.util.Map;

public class OAuthClient extends ZoomClient {
    private Map<String,Object> config;
    private ChatChannelsComponent chatChannelsComponent;
    private ChatMessagesComponent chatMessagesComponent;

    public OAuthClient(String clientId, String clientSecret, int port, String redirectUrl, String browserPath,Map<String,Object> varArgs){

        super(clientId,clientSecret,new HashMap<String, Object>() {{
            put("timeOut", varArgs!=null && varArgs.containsKey("timeOut")?varArgs.get("timeOut"):15);
        }});
        config = new HashMap<>();
        config.put("clientId",clientId);
        config.put("clientSecret",clientSecret);
        config.put("port",port);
        config.put("redirectUrl",redirectUrl);
        config.put("browserPath",browserPath);
        //ToDo change this: create a function in util package that gets token
        config.put("token",varArgs.get("token"));

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
