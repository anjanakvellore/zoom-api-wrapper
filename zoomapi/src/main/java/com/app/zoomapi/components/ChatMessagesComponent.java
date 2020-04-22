package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zoom.us REST API Java client - Chat Messages Component
 * Component dealing with all chat message related matters
 */
public class ChatMessagesComponent extends BaseComponent {
    private static ChatMessagesComponent chatMessagesComponent = null;

    private ChatMessagesComponent(Map<String,String> config){
        super(config);
    }

    public static ChatMessagesComponent getChatMessagesComponent(Map<String,String> config){
        if(chatMessagesComponent== null){
            chatMessagesComponent = new ChatMessagesComponent(config);
        }
        return chatMessagesComponent;
    }

    public HttpResponse<String> post(Map<String,Object> data){
        List<String> dataKeys = Arrays.asList(new String[]{"message"});
        if(Utility.requireKeys(data,dataKeys)) {
            return postRequest("/chat/users/me/messages",null,null,(Map<String,Object>) data,null);
        }
        else {
            return null;
        }
    }
}
