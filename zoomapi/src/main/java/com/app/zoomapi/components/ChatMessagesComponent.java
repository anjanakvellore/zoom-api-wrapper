package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatMessagesComponent extends BaseComponent {
    private static ChatMessagesComponent chatMessagesComponent = null;

    private ChatMessagesComponent(Map<String,Object> varArgs){
        super(varArgs);
    }

    public static ChatMessagesComponent getChatMessagesComponent(Map<String,Object> varArgs){
        if(chatMessagesComponent== null){
            chatMessagesComponent = new ChatMessagesComponent(varArgs);
        }
        return chatMessagesComponent;
    }

    public HttpResponse<String> post(Map<String,Object> variableArguments){
        List<String> reqKeys = Arrays.asList(new String[]{"message"});
        if(Utility.requireKeys(variableArguments,reqKeys)) {
            Map<String, Object> varArgs = new HashMap<>();
            varArgs.put("data", variableArguments);
            return postRequest("/chat/users/me/messages",varArgs);
        }
        else {
            return null;
        }
    }
}
