package com.app.zoomapi.componentwrapper;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Channels;
import com.app.zoomapi.repo.cachehelpers.ChannelMasterHelper;
import com.app.zoomapi.repo.cachehelpers.ChannelsHelper;
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

    private ChatChannelsComponentWrapper(ChatChannelsComponent chatChannelsComponent,String dbPath) throws SQLException {
        this.chatChannelsComponent = chatChannelsComponent;
        this.channelsHelper = new ChannelsHelper(dbPath);
        this.channelMasterHelper = new ChannelMasterHelper(dbPath);
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

    //ToDo: check if the retuen type is fine or not
    //ToDo: Zoom is sending some jid
    public HttpResponse<String> list(boolean cache,String zoomClientId){
        try{
            if(!cache){

                HttpResponse<String> response = this.chatChannelsComponent.list();

                if(response.statusCode() == 200) {

                    //get all the channels of the client from db;
                    List<Channels> channelsList = channelsHelper.getChannelsByZoomClientId(zoomClientId);

                    //delete records from Channels and ChannelMaster Table
                    channelMasterHelper.deleteChannelMasterRecordsByZoomClientId(channelsList);

                    channelsHelper.deleteChannelsByZoomClientID(zoomClientId);
                    JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
                    List<Integer> dbChannelIds = new ArrayList<>();

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
                }
                return response;
            }
            else {
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
            }
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
                    channelMasterHelper.deleteChannelMasterRecordByZoomChannelId((pathMap.get("channel_id").toString()));
                    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                    JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                    String cname = obj.get("name").getAsString();
                    String cid = obj.get("id").getAsString();
                    int type = obj.get("type").getAsInt();
                    ChannelMaster channelMaster = new ChannelMaster(cid.hashCode(), cid, cname, type, dateTime.format(formatter).toString());
                    channelMasterHelper.insertChannelMasterRecord(channelMaster);

                }
                return response;
            } else {
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
            }
        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap,Map<String,Object> dataMap){
        try {
            //have to update in zoom server no matter what
            HttpResponse<String> response = chatChannelsComponent.update(pathMap, dataMap);
            if (response.statusCode() == 204) {
                //ToDo: should the exception here be contained within this method so that we can return the actual HttpResponse?
                channelMasterHelper.deleteChannelMasterRecordByZoomChannelId(pathMap.get("channel_id").toString());
                //ToDo: how to update the channel name in cache?
                // if the record is existing in the cache for the given channel id, can we delete the record and reinsert it with the updated name
                // or should we support update function in SQL handler?
            }

            return response;


        }catch (Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }




}
