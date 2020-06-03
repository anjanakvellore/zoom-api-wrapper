package com.app.zoomapi.componentwrapper;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Channels;
import com.app.zoomapi.models.MemberMaster;
import com.app.zoomapi.repo.cachehelpers.ChannelMasterHelper;
import com.app.zoomapi.repo.cachehelpers.ChannelsHelper;
import com.app.zoomapi.repo.cachehelpers.MemberMasterHelper;
import com.app.zoomapi.repo.cachehelpers.MessagesHelper;
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
import java.util.*;

public class ChatChannelsComponentWrapper {

    private ChatChannelsComponent chatChannelsComponent = null;
    private static ChatChannelsComponentWrapper chatChannelsComponentWrapper = null;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ChannelsHelper channelsHelper = null;
    private ChannelMasterHelper channelMasterHelper = null;
    private MemberMasterHelper memberMasterHelper = null;
    private MessagesHelper messagesHelper = null;

    private ChatChannelsComponentWrapper(ChatChannelsComponent chatChannelsComponent,String dbPath) throws SQLException {
        this.chatChannelsComponent = chatChannelsComponent;
        this.channelsHelper = new ChannelsHelper(dbPath);
        this.channelMasterHelper = new ChannelMasterHelper(dbPath);
        this.memberMasterHelper = new MemberMasterHelper(dbPath);
        this.messagesHelper = new MessagesHelper(dbPath);
    }

    public static ChatChannelsComponentWrapper getChatChannelsComponentWrapper(ChatChannelsComponent chatChannelsComponent,String dbPath) throws SQLException {
        if(chatChannelsComponentWrapper == null){
            chatChannelsComponentWrapper = new ChatChannelsComponentWrapper(chatChannelsComponent,dbPath);
        }
        return chatChannelsComponentWrapper;
    }

    /** GENERAL RULE FOR THE GET METHODS
     * Check the cache flag.
     * If it is false
     *  1) delete all existing data from the cache
     *  2) fetch data from zoom api
     *  3) populate the cache with the newly fetched data
     * If the flag is true
     *  1) Get the data from the cache
     *  2) Check the timestamp in the data
     *  3) If the data is more than 30 minutes old, recursively call the same function with cache flag set to false
     *  4) If the data is within 30 minutes, return the data from the cache
     */

    public HttpResponse<String> list(boolean cache,String zoomClientId){
        try{
            if(!cache){

                HttpResponse<String> response = this.chatChannelsComponent.list();

                if(response.statusCode() == 200) {

                    try{
                        //get all the channels of the client from db;
                        List<Channels> channelsList = channelsHelper.getChannelsByZoomClientId(zoomClientId);

                        //delete records from Channels and ChannelMaster Table
                        channelMasterHelper.deleteChannelMasterRecordsByZoomClientId(channelsList);

                        channelsHelper.deleteChannelsByZoomClientID(zoomClientId);
                        JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();

                        //add new records
                        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                        List<ChannelMaster> channelMasterList = new ArrayList<>();
                        channelsList = new ArrayList<>();
                        for (JsonElement channel : channels) {
                            String name = channel.getAsJsonObject().get("name").getAsString();
                            String id = channel.getAsJsonObject().get("id").getAsString();
                            int type = channel.getAsJsonObject().get("type").getAsInt();
                            int channelId = id.hashCode();
                            channelMasterList.add(new ChannelMaster(channelId,id, name, type, dateTime.format(formatter)));
                            channelsList.add(new Channels(zoomClientId,channelId,dateTime.format(formatter)));
                        }
                        this.channelMasterHelper.insertChannelMasterRecords(channelMasterList);
                        this.channelsHelper.insertChannels(channelsList);
                    }catch(Exception ex){
                        return Utility.getStringHttpResponse(400,ex.getMessage());
                    }
                }
                return response;
            }
            else {
                try{
                    List<Channels> channelsList = this.channelsHelper.getChannelsByZoomClientId(zoomClientId);
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                    //TODO: make this separate method
                    if (channelsList.size() == 0 || LocalDateTime.parse(channelsList.get(0).getTimeStamp(), formatter).until(now, ChronoUnit.MINUTES) > 30) {
                        return list(false, zoomClientId);
                    } else {
                        List<ChannelMaster> channelMasterList = new ArrayList<>();
                        for (Channels ch : channelsList) {
                            channelMasterList.add(channelMasterHelper.getChannelMasterRecordByChannelId(ch.getChannelId()));
                        }

                        JSONObject jResult = new JSONObject();
                        JSONArray jArray = new JSONArray();

                        for (ChannelMaster ch : channelMasterList) {
                            JSONObject jGroup = new JSONObject();
                            jGroup.put("name", ch.getChannelName());
                            jGroup.put("id", ch.getZoomChannelId());
                            jGroup.put("type", ch.getType());
                            jArray.put(jGroup);
                            jResult.put("channels", jArray);

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

    public HttpResponse<String> delete(Map<String,Object> pathMap){
        try {
            HttpResponse<String> response = this.chatChannelsComponent.delete(pathMap);
            if (response.statusCode() == 204) {
                try{
                    ChannelMaster channelMaster = channelMasterHelper.getChannelMasterRecordByZoomChannelId((pathMap.get("channel_id").toString()));
                    //TODO to deal with null pointer ok? else I dont care
                    if(channelMaster !=null){
                        int channelId = channelMaster.getChannelId();
                        channelsHelper.deleteChannelsByChannelId(channelId);
                        channelMasterHelper.deleteChannelMasterRecordByChannelId(channelId);
                        memberMasterHelper.deleteMemberMasterRecordByChannelId(channelId);
                        messagesHelper.deleteMessagesRecordByChannelId(channelId);
                    }
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> createChannel(String zoomClientId, Map<String,Object> dataMap){
        try{
            HttpResponse<String> response = this.chatChannelsComponent.createChannel(dataMap);
            if(response.statusCode() == 200) {
                try{
                    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                    JsonElement channel= JsonParser.parseString(response.body());
                    String zoomChannelId = channel.getAsJsonObject().get("id").getAsString();
                    int channelId = zoomChannelId.hashCode();
                    String name = channel.getAsJsonObject().get("name").getAsString();
                    int type = channel.getAsJsonObject().get("type").getAsInt();
                    this.channelsHelper.insertChannels(Arrays.asList(new Channels(zoomClientId, channelId,dateTime.format(formatter))));
                    this.channelMasterHelper.insertChannelMasterRecord(new ChannelMaster(channelId,zoomChannelId, name, type, dateTime.format(formatter)));
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> get(boolean cache,Map<String,Object> pathMap){
        try {
            if (!cache) {
                HttpResponse<String> response = this.chatChannelsComponent.get(pathMap);
                if (response.statusCode() == 200) {
                    try{
                        channelMasterHelper.deleteChannelMasterRecordByZoomChannelId((pathMap.get("channel_id").toString()));
                        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                        JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                        String cname = obj.get("name").getAsString();
                        String cid = obj.get("id").getAsString();
                        int type = obj.get("type").getAsInt();
                        ChannelMaster channelMaster = new ChannelMaster(cid.hashCode(), cid, cname, type, dateTime.format(formatter).toString());
                        channelMasterHelper.insertChannelMasterRecord(channelMaster);
                    }catch(Exception ex){
                        return Utility.getStringHttpResponse(400,ex.getMessage());
                    }
                }
                return response;
            } else {
                try{
                    ChannelMaster channelMaster = channelMasterHelper.getChannelMasterRecordByZoomChannelId(pathMap.get("channel_id").toString());
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                    if (channelMaster == null || LocalDateTime.parse(channelMaster.getTimeStamp(), formatter).until(now, ChronoUnit.MINUTES) > 30) {
                        return get(false, pathMap);
                    } else {
                        JSONObject jGroup = new JSONObject();
                        jGroup.put("name", channelMaster.getChannelName());
                        jGroup.put("id", channelMaster.getZoomChannelId());
                        jGroup.put("type", channelMaster.getType());
                        return Utility.getStringHttpResponse(200, jGroup.toString());
                    }
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap,Map<String,Object> dataMap){
        try {
            HttpResponse<String> response = chatChannelsComponent.update(pathMap, dataMap);
            if (response.statusCode() == 204) {
                try{
                    ChannelMaster channelMaster = channelMasterHelper.getChannelMasterRecordByZoomChannelId(pathMap.get("channel_id").toString());
                    channelMasterHelper.deleteChannelMasterRecordByZoomChannelId(pathMap.get("channel_id").toString());
                    channelMaster.setChannelName(dataMap.get("name").toString());
                    channelMasterHelper.insertChannelMasterRecord(channelMaster);
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> listChannelMembers(boolean cache,
                                                   Map<String,Object> pathMap,Map<String,Object> initialParamMap){
        try{
            if(!cache){

                HttpResponse<String> response = this.chatChannelsComponent.listChannelMembers(pathMap,initialParamMap);

                if(response.statusCode() == 200) {
                    try{
                        ChannelMaster channelMaster = channelMasterHelper.getChannelMasterRecordByZoomChannelId((pathMap.get("channel_id").toString()));
                        //TODO so what if null? else..?
                        if(channelMaster!=null){
                            int channelId = channelMaster.getChannelId();

                            List<MemberMaster> memberMasterList = memberMasterHelper.getMemberMasterRecordsByChannelId(channelId);
                            memberMasterHelper.deleteMemberMasterRecordsByChannelId(memberMasterList);

                            JsonArray members = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();

                            //add new records
                            LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                            memberMasterList = new ArrayList<>();
                            for (JsonElement member : members) {
                                String zoomMemberId = member.getAsJsonObject().get("id").getAsString();
                                String email = member.getAsJsonObject().get("email").getAsString();
                                String firstName = member.getAsJsonObject().get("first_name").getAsString();
                                String lastName = member.getAsJsonObject().get("last_name").getAsString();
                                String role = member.getAsJsonObject().get("role").getAsString();
                                memberMasterList.add(new MemberMaster(channelId, zoomMemberId, firstName,lastName,email,
                                        role,dateTime.format(formatter)));
                            }
                            this.memberMasterHelper.insertMemberMasterRecords(memberMasterList);
                        }
                    }catch(Exception ex){
                        return Utility.getStringHttpResponse(400,ex.getMessage());
                    }
                }
                return response;
            }
            else {
                try{
                    ChannelMaster channelMaster = channelMasterHelper.getChannelMasterRecordByZoomChannelId((pathMap.get("channel_id").toString()));
                    int channelId = channelMaster.getChannelId();

                    List<MemberMaster> memberMasterList = this.memberMasterHelper.getMemberMasterRecordsByChannelId(channelId);
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                    //TODO: make this separate method
                    if (memberMasterList.size() == 0 || LocalDateTime.parse(memberMasterList.get(0).getTimeStamp(), formatter).until(now, ChronoUnit.MINUTES) > 30) {
                        return listChannelMembers(false, pathMap, initialParamMap);
                    } else {

                        JSONObject jResult = new JSONObject();
                        JSONArray jArray = new JSONArray();

                        for (MemberMaster mem : memberMasterList) {
                            JSONObject jGroup = new JSONObject();
                            jGroup.put("id", mem.getZoomMemberId());
                            jGroup.put("email", mem.getEmail());
                            jGroup.put("first_name", mem.getFirstName());
                            jGroup.put("last_name", mem.getLastName());
                            jGroup.put("role", mem.getRole());
                            jArray.put(jGroup);
                            jResult.put("members", jArray);
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

    public HttpResponse<String> inviteChannelMembers(Map<String,Object> pathMap,Map<String,Object> dataMap){
        try{
            HttpResponse<String> response = this.chatChannelsComponent.inviteChannelMembers(pathMap, dataMap);
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }


    //TODO joinChannel: should I use user component and store all the necessary info? Google Doc
    public HttpResponse<String> joinChannel(String zoomClientId, Map<String,Object> pathMap){
        try{
            HttpResponse<String> response = this.chatChannelsComponent.joinChannel(pathMap);
            if(response.statusCode() == 201) {
                try{
                    String zoomChannelId = pathMap.get("channel_id").toString();
                    int channelId = zoomChannelId.hashCode();
                    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                    this.channelsHelper.insertChannels(Arrays.asList(new Channels(zoomClientId, channelId, dateTime.format(formatter).toString())));
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }


    //TODO leaveChannel : Response : call credentials get email id remove from member master
    public HttpResponse<String> leaveChannel(String zoomClientId, Map<String,Object> pathMap){
        try{
            HttpResponse<String> response = this.chatChannelsComponent.leaveChannel(pathMap);
            if(response.statusCode() == 204){
                try{
                    String zoomChannelId = pathMap.get("channel_id").toString();
                    int channelId = zoomChannelId.hashCode();
                    this.channelsHelper.deleteChannelsByZoomClientIdAndChannelId(zoomClientId,channelId);
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> remove(Map<String,Object> pathMap){
        try {
            HttpResponse<String> response = this.chatChannelsComponent.remove(pathMap);
            if (response.statusCode() == 204) {
                try{
                    String zoomChannelId = pathMap.get("channel_id").toString();
                    int channelId = zoomChannelId.hashCode();
                    memberMasterHelper.deleteMemberMasterRecordsByChannelIdAndEmail(channelId,pathMap.get("member_id").toString());
                }catch(Exception ex){
                    return Utility.getStringHttpResponse(400,ex.getMessage());
                }
            }
            return response;
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }
}