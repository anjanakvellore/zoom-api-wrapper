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

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    private List<Message> getMessage(String toChannel, Date date,int pageSize, String userId) throws ParseException {
        String nextPageToken = "";
        List<Message> messages = new ArrayList<>();
        do{
            //SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = date.getYear() + "-"+ String.valueOf(date.getMonth()+1)+"-"+date.getDate();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("to_channel", toChannel);
            paramMap.put("date", dateString);
            paramMap.put("page_size", 50);
            paramMap.put("next_page_token", nextPageToken);
            Map<String,Object> pathMap = new HashMap<>();
            pathMap.put("userId", userId);
            HttpResponse<String> messageResponse = this.chatMessagesComponent.list(pathMap, paramMap);
            JsonObject messageBody = JsonParser.parseString(messageResponse.body()).getAsJsonObject();
            JsonArray messageArray = messageBody.get("messages").getAsJsonArray();
            for (JsonElement message : messageArray) {
                String sender = message.getAsJsonObject().get("sender").getAsString();
                String dateTime = message.getAsJsonObject().get("date_time").getAsString();
                String messageContent = message.getAsJsonObject().get("message").getAsString();
                messages.add(new Message(messageContent, sender, dateTime));
            }
        }
        while (!nextPageToken.isEmpty() && !nextPageToken.isBlank());
        return messages;
    }

    private String getUserId(){
        Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
        HttpResponse<String> userResponse = this.userComponent.get(pathMap,null);
        String userId = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("id").getAsString();
        return userId;
    }

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
     * public function exposed to the client
     * @param channelName
     * @param fromDate
     * @param toDate
     * @return
     */
    public HttpResponse<Object> history(String channelName, Date fromDate, Date toDate){
        try {
            List<Message> messages = getHistory(channelName, fromDate, toDate);
            return new HttpResponse<Object>() {
                @Override
                public int statusCode() {
                    return 200;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<Object>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public Object body() {
                    return messages;
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
        catch (Exception ex){
            return new HttpResponse<Object>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<Object>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public Object body() {
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

    private List<Message> getHistory(String channelName,Date fromDate, Date toDate) throws Exception {
        Instant instantFromDate = fromDate.toInstant();
        Instant instantToDate = toDate.toInstant();
        long days = ChronoUnit.DAYS.between(instantFromDate, instantToDate);
        if(days>5){
            throw  new Exception("Number of days should be less than 5");
        }
        String userId = getUserId();
        String channelId = getChannelId(channelName);
        List<Message> messages = new ArrayList<>();

        if(channelId!=null) {
            Date current = fromDate;
            while (current.before(toDate)) {
                messages.addAll(getMessage(channelId,current,50,userId));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(current);
                calendar.add(Calendar.DATE, 1);
                current = calendar.getTime();
            }

        }
        else{
            throw new Exception("invalid channel id");
        }

        return messages;
    }

    /**
     * public function exposed to the client
     * @param channelName
     * @param fromDate
     * @param toDate
     * @param func
     * @return
     */
    public HttpResponse<Object> search(String channelName, Date fromDate, Date toDate, IFunction func) {
        try {
            List<Message> messages = getHistory(channelName, fromDate, toDate);
            Object functionOutput =  func.call(messages);
            return new HttpResponse<Object>() {
                @Override
                public int statusCode() {
                    return 200;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<Object>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public Object body() {
                    return functionOutput;
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
        }catch (Exception ex){
            return new HttpResponse<Object>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<Object>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public Object body() {
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
