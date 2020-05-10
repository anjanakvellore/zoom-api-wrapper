package com.app.zoomapi.extended;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.UserComponent;
import com.app.zoomapi.models.Member;
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
import java.util.*;

public class Members {
    private ChatChannelsComponent chatChannelsComponent = null;
    private UserComponent userComponent = null;
    private static Members members = null;

    private Members(ChatChannelsComponent channelComponent,UserComponent userComponent){
        this.chatChannelsComponent = channelComponent;
        this.userComponent = userComponent;
    }

    /**
     * returns the instance of Members class
     * @param chatChannelsComponent
     * @param userComponent
     * @return instance of Members class
     */
    public static Members getMembersComponent(ChatChannelsComponent chatChannelsComponent,UserComponent userComponent){
        if(members == null){
            members = new Members(chatChannelsComponent,userComponent);
        }
        return members;
    }

    /**
     * get the channel id for a given channel name
     * @param channelName
     * @return channel id
     */
    private String getChannelId(String channelName){
        HttpResponse<String> response = this.chatChannelsComponent.list();
        String channelId = null;
        JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
        for(JsonElement channel:channels){
            if(channel.getAsJsonObject().get("name").getAsString().equals(channelName)){
                channelId = channel.getAsJsonObject().get("id").getAsString();
                break;
            }
        }
        return channelId;
    }

    /**
     * gets details of the members in a given channel
     * @param channelName
     * @return http response object
     */
    public HttpResponse<Object> details(String channelName) {
        try{
            String channelId = getChannelId(channelName);
            if(channelId==null){
                throw new Exception("Invalid channel name");
            }
            List<Member> members = getMember(channelId);
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
                    return members;
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

    /**
     * gets the list of members from a given channel
     * @param channelId
     * @return list of members
     * @throws Exception
     */
    private List<Member> getMember(String channelId) throws Exception {
        String nextPageToken = "";
        System.out.println("In getmembers:"+channelId);
        List<Member> members = new ArrayList<>();
        do{
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("page_size", 50);
            paramMap.put("next_page_token", nextPageToken);

            Map<String,Object> pathMap = new HashMap<>();
            pathMap.put("channel_id", channelId);

            HttpResponse<String> memberResponse = this.chatChannelsComponent.listChannelMembers(pathMap,paramMap);
            JsonObject memberBody = JsonParser.parseString(memberResponse.body()).getAsJsonObject();
            nextPageToken = memberBody.get("next_page_token").getAsString();
            JsonArray memberArray = memberBody.get("members").getAsJsonArray();
            if(memberResponse.statusCode() == 200) {
                for(JsonElement member:memberArray){
                    String email = member.getAsJsonObject().get("email").getAsString();
                    String memberId = member.getAsJsonObject().get("id").getAsString();
                    String firstName = member.getAsJsonObject().get("first_name").getAsString();
                    String lastName = member.getAsJsonObject().get("last_name").getAsString();
                    String role = member.getAsJsonObject().get("role").getAsString();
                    System.out.println(email);
                    members.add(new Member(memberId, email,firstName,lastName,role));
                }
            }
            else{
                throw new Exception(memberResponse.body());
            }
        }
        while (!nextPageToken.isEmpty() && !nextPageToken.isBlank());
        return members;
    }
}