package com.app.zoomapi.extended;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.components.UserComponent;
import com.app.zoomapi.models.Message;
import com.app.zoomapi.models.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /**
     * returns the instance of Chat class
     * @param chatChannelsComponent
     * @param chatMessagesComponent
     * @param userComponent
     * @return instance of Chat class
     */
    public static Chat getChatComponent(ChatChannelsComponent chatChannelsComponent,ChatMessagesComponent chatMessagesComponent,UserComponent userComponent){
        if(chat == null){
            chat = new Chat(chatChannelsComponent,chatMessagesComponent,userComponent);
        }
        return chat;
    }

    /**
     * gets list of messages for the given date and channel
     * @param toChannel channel id
     * @param date date
     * @param pageSize maximum 50
     * @param userId user Id
     * @return list of messages
     * @throws Exception
     */
    private List<Message> getMessage(String toChannel, LocalDate date,int pageSize, String userId) throws Exception {
        String nextPageToken = "";
        List<Message> messages = new ArrayList<>();
        do{
            String dateString = date.toString();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("to_channel", toChannel);
            paramMap.put("date", dateString);
            paramMap.put("page_size", 50);
            paramMap.put("next_page_token", nextPageToken);
            Map<String,Object> pathMap = new HashMap<>();
            pathMap.put("userId", userId);
            HttpResponse<String> messageResponse = this.chatMessagesComponent.list(pathMap, paramMap);
            if(messageResponse.statusCode() == 200) {
                JsonObject messageBody = JsonParser.parseString(messageResponse.body()).getAsJsonObject();
                nextPageToken = messageBody.get("next_page_token").getAsString();
                JsonArray messageArray = messageBody.get("messages").getAsJsonArray();
                for (JsonElement message : messageArray) {
                    String sender = message.getAsJsonObject().get("sender").getAsString();
                    String dateTime = message.getAsJsonObject().get("date_time").getAsString();
                    String messageContent = message.getAsJsonObject().get("message").getAsString();
                    String messageId = message.getAsJsonObject().get("id").getAsString();
                    messages.add(new Message(messageContent, sender, dateTime,messageId));
                }
            }
            else{
                throw new Exception(messageResponse.body());
            }
        }
        while (!nextPageToken.isEmpty() && !nextPageToken.isBlank());
        return messages;
    }

    /**
     * get the user id of the current user
     * @return user id
     */
    private String getUserId(){
        Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
        HttpResponse<String> userResponse = this.userComponent.get(pathMap,null);
        String userId = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("id").getAsString();
        return userId;
    }

    /**
     * get the channel id given channel name
     * @param channelName
     * @return channel id
     */
    private String getChannelId(String channelName){
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
        return channelId;
    }

    /**
     * sends given message to a given channel
     * @param channelName
     * @param message
     * @return Result object
     */

    public Result sendMessage(String channelName, String message) {
        try {
            String channelId = getChannelId(channelName);
            if(channelId==null){
                throw new Exception("Invalid channel name");
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("message", message);
            dataMap.put("to_channel", channelId);
            HttpResponse<String> response = this.chatMessagesComponent.post(dataMap);
            Result result = new Result();
            result.setStatus(response.statusCode());
            if(response.statusCode()==201){
                result.setData(response.body());
            }
            else{
                throw new Exception(response.body());
            }
            return result;
        }catch (Exception ex){
            Result result = new Result();
            result.setStatus(0);
            result.setErrorMessage(ex.getMessage());
            return result;
        }
    }

    /**
     * get the history of a given channel between two dates
     * @param channelName
     * @param fromDate start date
     * @param toDate end date
     * @return Result object
     */
    public Result history(String channelName, LocalDate fromDate, LocalDate toDate){
        try {
            List<Message> messages = getHistory(channelName, fromDate, toDate);
            Result result = new Result();
            result.setStatus(200);
            result.setData(messages);
            return result;
        }
        catch (Exception ex){
            Result result = new Result();
            result.setStatus(0);
            result.setErrorMessage(ex.getMessage());
            return result;
        }

    }

    /**
     * gets the list of messages from a given channel between two dates
     * @param channelName
     * @param fromDate start date
     * @param toDate end date
     * @return list of messages
     * @throws Exception
     */
    private List<Message> getHistory(String channelName,LocalDate fromDate, LocalDate toDate) throws Exception {

        long days = ChronoUnit.DAYS.between(fromDate, toDate);
        if(days>5){
            throw  new Exception("Number of days should be less than 5.");
        }
        if(days<0){
            throw new Exception("Start date must be less than end date.");
        }
        String userId = getUserId();
        String channelId = getChannelId(channelName);
        List<Message> messages = new ArrayList<>();

        if(channelId!=null) {
            for (LocalDate date = fromDate; date.isBefore(toDate) || date.isEqual(toDate); date = date.plusDays(1)) {
                /**
                 * if date is greater than current date, zoom api would retrieve the messages from the current date every time
                 * which could result in duplication of data
                 */
                messages.addAll(getMessage(channelId, date, 50, userId));

            }
        }
        else{
            throw new Exception("Invalid channel name");
        }

        return messages;
    }

    /**
     * searching for specific events related to chat between two given dates on a given channel
     * @param channelName
     * @param fromDate start date
     * @param toDate end date
     * @param predicate specifies the condition for filtering the messages
     * @return Result object
     */
    public Result search(String channelName, LocalDate fromDate, LocalDate toDate, Predicate<Message> predicate) {
        try {
            List<Message> messages = getHistory(channelName, fromDate, toDate);
            List<Message> output = messages.stream().filter(predicate).collect(Collectors.toList());
            Result result = new Result();
            result.setStatus(200);
            result.setData(output);
            return result;
        }catch (Exception ex){
            Result result = new Result();
            result.setStatus(0);
            result.setErrorMessage(ex.getMessage());
            return result;
        }

    }

}
