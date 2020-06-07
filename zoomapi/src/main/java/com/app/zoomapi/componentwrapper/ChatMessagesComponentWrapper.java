package com.app.zoomapi.componentwrapper;

import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Messages;
import com.app.zoomapi.repo.cachehelpers.*;
import com.app.zoomapi.utilities.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ChatMessagesComponent wrapper class that supports persistence of data.
 */
public class ChatMessagesComponentWrapper {
    private ChatMessagesComponent chatMessagesComponent = null;
    private static ChatMessagesComponentWrapper chatMessagesComponentWrapper= null;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ChannelMasterHelper channelMasterHelper = null;
    private MessagesHelper messagesHelper = null;
    private UserHelper userHelper = null;

    private ChatMessagesComponentWrapper(ChatMessagesComponent chatMessagesComponent,String dbPath) throws SQLException {
        this.chatMessagesComponent = chatMessagesComponent;
        this.channelMasterHelper = new ChannelMasterHelper(dbPath);
        this.messagesHelper = new MessagesHelper(dbPath);
        this.userHelper = new UserHelper(dbPath);
    }

    public static ChatMessagesComponentWrapper getChatMessagesComponentWrapper(ChatMessagesComponent chatMessagesComponent,String dbPath) throws SQLException {
        if(chatMessagesComponentWrapper == null){
            chatMessagesComponentWrapper = new ChatMessagesComponentWrapper(chatMessagesComponent,dbPath);
        }
        return chatMessagesComponentWrapper;
    }

    public HttpResponse<String> list(boolean cache,String zoomClientId,Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        try{
            if(!cache){

                HttpResponse<String> response = this.chatMessagesComponent.list(pathMap, initialParamMap);

                if(response.statusCode() == 200) {

                    try{
                        int channelId = initialParamMap.get("to_channel").hashCode();
                        String email = userHelper.getUserRecordByZoomClientId(zoomClientId).getEmail();

                        messagesHelper.deleteMessagesRecordBySenderAndChannel(email,channelId);

                        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));

                        List<Messages> messages = new ArrayList<>();
                        JsonObject messageBody = JsonParser.parseString(response.body()).getAsJsonObject();
                        JsonArray messageArray = messageBody.get("messages").getAsJsonArray();
                        for (JsonElement message : messageArray) {
                            String zoomMessageId = message.getAsJsonObject().get("id").getAsString();
                            String messageContent = message.getAsJsonObject().get("message").getAsString();
                            String sender = message.getAsJsonObject().get("sender").getAsString();
                            String zoomDateTime = message.getAsJsonObject().get("date_time").getAsString();
                            String zoomTimeStamp = message.getAsJsonObject().get("timestamp").getAsString();
                            messages.add(new Messages(channelId,zoomMessageId,messageContent, sender, zoomDateTime,zoomTimeStamp,dateTime.format(formatter)));
                        }
                        this.messagesHelper.insertMessagesRecords(messages);
                    }catch(Exception ex){
                        //return Utility.getStringHttpResponse(400,ex.getMessage());
                    }
                }
                return response;
            }
            else {
                try{
                    String email = userHelper.getUserRecordByZoomClientId(zoomClientId).getEmail();
                    int channelId = initialParamMap.get("to_channel").hashCode();

                    List<Messages> messagesList = this.messagesHelper.getMessagesRecordsBySenderAndChannel(email,channelId);
                    if (messagesList.size() == 0 || Utility.invalidateCache(messagesList.get(0).getTimeStamp())) {
                        return list(false, zoomClientId,pathMap,initialParamMap);
                    } else {
                        JSONObject jResult = new JSONObject();
                        JSONArray jArray = new JSONArray();

                        for (Messages message : messagesList) {
                            JSONObject jGroup = new JSONObject();
                            jGroup.put("id", message.getZoomMessageId());
                            jGroup.put("message", message.getMessage());
                            jGroup.put("sender", message.getSender());
                            jGroup.put("date_time", message.getDateTime());
                            jGroup.put("timestamp", message.getZoomTimeStamp());
                            jArray.put(jGroup);
                            jResult.put("messages", jArray);
                        }
                        return Utility.getStringHttpResponse(200, jResult.toString());
                    }
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> post(Map<String,Object> dataMap){
        try{
            HttpResponse<String> response = this.chatMessagesComponent.post(dataMap);
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap ,Map<String,Object> initialParamMap, Map<String,Object> dataMap){
        try {
            HttpResponse<String> response = chatMessagesComponent.update(pathMap, initialParamMap, dataMap);
            if (response.statusCode() == 204) {
                try{
                    String zoomMessageId = pathMap.get("messageId").toString();
                    Messages message = messagesHelper.getMessagesRecordByZoomMessageId(zoomMessageId);
                    if(message!=null) {
                        messagesHelper.deleteMessagesRecordByZoomMessageId(zoomMessageId);
                        message.setMessage(dataMap.get("message").toString());
                        messagesHelper.insertMessagesRecord(message);
                    }
                }catch(Exception ex){
                    //return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> delete(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        try {
            HttpResponse<String> response = this.chatMessagesComponent.delete(pathMap, initialParamMap);
            if (response.statusCode() == 204) {
                try{
                    String zoomMessageId = pathMap.get("messageId").toString();
                    messagesHelper.deleteMessagesRecordByZoomMessageId(zoomMessageId);
                }catch(Exception ex){
                    //return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }
}