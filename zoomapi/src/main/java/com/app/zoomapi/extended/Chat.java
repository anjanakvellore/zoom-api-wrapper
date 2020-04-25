package com.app.zoomapi.extended;

import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.components.BaseComponent;
import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.components.UserComponent;
import com.app.zoomapi.models.IFunction;
import com.app.zoomapi.models.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class Chat {
    private ChatChannelsComponent chatChannelsComponent = null;
    private ChatMessagesComponent chatMessagesComponent = null;
    private UserComponent userComponent = null;
    private static Chat chat = null;
    private Chat(ChatChannelsComponent channelComponent,ChatMessagesComponent messagesComponent,UserComponent userComponent){
       this.chatChannelsComponent = channelComponent;
       this.chatMessagesComponent = messagesComponent;
       this.userComponent = userComponent;
    }

    public static Chat getChatComponent(ChatChannelsComponent chatChannelsComponent,ChatMessagesComponent chatMessagesComponent,UserComponent userComponent){
        if(chat == null){
            chat = new Chat(chatChannelsComponent,chatMessagesComponent,userComponent);
        }
        return chat;
    }


    public List<Message> history(String channelName){
        Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
        HttpResponse<String> userResponse = this.userComponent.get(pathMap,null);
        String userId = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("id").getAsString();

        HttpResponse<String> response = this.chatChannelsComponent.list();
        String channelId = null;
        JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
        List<Message> messages = new ArrayList<>();
        for(JsonElement channel:channels){
            if(channel.getAsJsonObject().get("name").getAsString().equals(channelName)){
                channelId = channel.getAsJsonObject().get("id").getAsString();
                break;
            }
        }
        if(channelId!=null){
            String nextPageToken = "";
            do {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("to_channel", channelId);
                paramMap.put("date", "2020-04-06");
                paramMap.put("page_size", 100);
                paramMap.put("next_page_token", nextPageToken);
                pathMap = new HashMap<>();
                pathMap.put("userId", userId);
               /* Date date = new Date(2020,4,22);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = formatter.format(date);*/
                HttpResponse<String> messageResponse = this.chatMessagesComponent.list(pathMap, paramMap);
                JsonObject messageBody = JsonParser.parseString(messageResponse.body()).getAsJsonObject();
                JsonArray messageArray = messageBody.get("messages").getAsJsonArray();
                for (JsonElement message : messageArray) {
                    String sender = message.getAsJsonObject().get("sender").getAsString();
                    String dateTime = message.getAsJsonObject().get("date_time").getAsString();
                    String messageContent = message.getAsJsonObject().get("message").getAsString();
                    messages.add(new Message(messageContent,sender,dateTime));
                  /*  messages.add(message.getAsJsonObject().get("sender").getAsString() + "("+message.getAsJsonObject().get("date_time")
                            +"): " + message.getAsJsonObject().get("message").getAsString());*/
                }
                nextPageToken = messageBody.get("next_page_token").getAsString();
            }while (!nextPageToken.isEmpty() && !nextPageToken.isBlank());


        }
        return messages;
    }

    public Object search(String channelName, IFunction func){
            List<Message> messages = history(channelName);
            return func.call(messages);

    }

}
