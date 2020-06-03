package com.app.bots;

import com.app.zoomapi.utilities.CredentialsHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.*;

public class Bot5 {
    public static void main(String[] args) {

        try {

            /**
             * Create ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             */
            CredentialsHandler credentialsHandler = new CredentialsHandler("bot.ini");


            com.app.zoomapi.clients.OAuthClient client = credentialsHandler.getOAuthClient();
            String clientId = credentialsHandler.getClientId();
                    /*new com.app.zoomapi.clients.OAuthClient
                    (credentialsHandler.getClientId(), credentialsHandler.getClientSecret(), credentialsHandler.getPort(),
                            url, credentialsHandler.getBrowserPath(), null, null,credentialsHandler.getDbPath());*/

            Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
            HttpResponse<String> response = client.getUserComponentWrapper().get(clientId,pathMap,null);
            System.out.println(response);
            JsonObject userResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            String userId = userResponse.get("id").getAsString();
            String email = userResponse.getAsJsonObject().get("email").getAsString();
            System.out.println("User ID: "+userId+" User email: "+email);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * to list user's channels
             */
            response = client.getChatChannelsComponentWrapper().list(false,clientId);
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                System.out.print("Channel Name: "+channel.getAsJsonObject().get("name"));
                System.out.println(" ID: "+channel.getAsJsonObject().get("id"));

            }
            System.out.println("-------------------------------------------------------------------------------------");


            /**
             * to create channel
             */
            System.out.println("Create new channel ");
            Scanner in = new Scanner(System.in);
            System.out.println("Enter channel name: ");
            String channelName = in.nextLine();
            String channelType = "1";
            List<Map<String,String>> members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","anjanak1@uci.edu");}});
            Map<String,Object> channelMap = new HashMap<>();
            channelMap.put("name",channelName);
            channelMap.put("type",channelType);
            channelMap.put("members",members);
            System.out.println("Adding anjanak1@uci.edu to the channel");
            response = client.getChatChannelsComponentWrapper().createChannel(clientId,channelMap);
            System.out.println(response);
            //String channelid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            final String cid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * invite members to the channel - can add 5 members at a time as per Zoom API
             */
            System.out.println("Inviting members to the channel");
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","santhiyn@uci.edu");}});
            Map<String,Object> memberMap = new HashMap<>();
            memberMap.put("members",members);
            System.out.println("Adding santhiyan@uci.edu");
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponentWrapper().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");


            /**
             * list channel members
             */
            System.out.println("Getting list of members in the channel "+cid);
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponentWrapper().listChannelMembers(false,pathMap,null);
            System.out.println(response);
            JsonArray array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("(Immediate Cache)Getting list of members in the channel "+cid);
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponentWrapper().listChannelMembers(true,pathMap,null);
            System.out.println(response);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }

            /**
             * update channel name
             */
            System.out.println("Updating channel name of the channel "+cid);
            System.out.println("Enter new name: ");
            channelName = in.nextLine();
            Map<String,Object> data = new HashMap<>();
            data.put("name",channelName);
            pathMap = new HashMap<>();
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponentWrapper().update(pathMap,data);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");

            //test - channel cid
            String chid = "109ab13498c64fd5911a42be1076ea6b";
            System.out.println("Getting channel details for channel "+chid);
            pathMap = new HashMap<>();
            pathMap.put("channel_id",chid);
            response = client.getChatChannelsComponentWrapper().get(false,pathMap);
            System.out.println(response.body());
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * get channel details
             */
            /*System.out.println("Getting channel details for channel "+cid);
            pathMap = new HashMap<>();
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().get(pathMap);
            channelName = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();
            System.out.println("Name: " + channelName);
            System.out.println("-------------------------------------------------------------------------------------"); */

            /**
             * send a chat message
             */
            System.out.println("Enter message: ");
            String message = in.nextLine();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = client.getChatMessagesComponentWrapper().post(dataMap);
            System.out.println("Message sent to Channel "+cid);
            String mId = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * List user's chat messages
             */
            pathMap = new HashMap<>(){{put("userId",userId);}};
            Map<String, Object> paramMap = new HashMap<>(){{put("to_channel",cid);}};
            Thread.sleep(2000);
            response = client.getChatMessagesComponentWrapper().list(false,clientId,pathMap,paramMap);
            Thread.sleep(2000);
            System.out.println("Listing messages sent to Channel "+cid);
            System.out.println("For User ID: "+userId);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("messages").getAsJsonArray();
            Thread.sleep(2000);
            for(JsonElement ar:array){
                System.out.print(ar.getAsJsonObject().get("sender")+":");
                System.out.println(ar.getAsJsonObject().get("message"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            pathMap = new HashMap<>(){{put("userId",userId);}};
            paramMap = new HashMap<>(){{put("to_channel",cid);}};
            response = client.getChatMessagesComponentWrapper().list(true,clientId,pathMap,paramMap);
            System.out.println("(Immediate Cache) Listing messages sent to Channel "+cid);
            System.out.println("For User ID: "+userId);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("messages").getAsJsonArray();

            for(JsonElement ar:array){
                System.out.print(ar.getAsJsonObject().get("sender")+":");
                System.out.println(ar.getAsJsonObject().get("message"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * Update a chat message
             */
            System.out.println("Enter updated message for the above message: ");
            message = in.nextLine();
            pathMap.put("messageId",mId);
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = client.getChatMessagesComponentWrapper().update(pathMap,null,dataMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            pathMap = new HashMap<>(){{put("userId",userId);}};
            paramMap = new HashMap<>(){{put("to_channel",cid);}};
            response = client.getChatMessagesComponentWrapper().list(true,clientId,pathMap,paramMap);
            System.out.println("(Immediate Cache) Listing messages sent to Channel "+cid);
            System.out.println("For User ID: "+userId);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("messages").getAsJsonArray();

            for(JsonElement ar:array){
                System.out.print(ar.getAsJsonObject().get("sender")+":");
                System.out.println(ar.getAsJsonObject().get("message"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * Delete a message
             */
            System.out.println("Deleting the above message...");
            Thread.sleep(3000);
            pathMap.put("messageId",mId);
            response = client.getChatMessagesComponentWrapper().delete(pathMap,paramMap);
            System.out.println(response);
            System.out.println("Message deleted in Channel "+cid);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            pathMap = new HashMap<>(){{put("userId",userId);}};
            paramMap = new HashMap<>(){{put("to_channel",cid);}};
            response = client.getChatMessagesComponentWrapper().list(true,clientId,pathMap,paramMap);
            System.out.println("(Immediate Cache)Listing messages sent to Channel "+cid);
            System.out.println("For User ID: "+userId);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("messages").getAsJsonArray();

            for(JsonElement ar:array){
                System.out.print(ar.getAsJsonObject().get("sender")+":");
                System.out.println(ar.getAsJsonObject().get("message"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * remove a member
             */
            System.out.println("Removing santhiyn@uci.edu");
            pathMap.put("channel_id",cid);
            pathMap.put("member_id","santhiyn@uci.edu");
            response = client.getChatChannelsComponentWrapper().remove(pathMap);
            System.out.println(response);
            System.out.println("Removed member");
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            System.out.println("(Immediate Cache) Getting list of members in the channel "+cid);
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponentWrapper().listChannelMembers(true,pathMap,null);
            System.out.println(response);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * leave a channel
             */
            System.out.println("Channel id: "+cid);
            System.out.println("Leaving the channel...");
            Thread.sleep(3000);
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponentWrapper().leaveChannel(clientId,pathMap);
            System.out.println(response);
            System.out.println("Left channel with id: "+cid);
            System.out.println("The admin powers are given to the existing members!");
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(4000);

            System.out.println("(Immediate Cache) Getting list of members in the channel "+cid);
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponentWrapper().listChannelMembers(true,pathMap,null);
            System.out.println(response);
            array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }

            response = client.getChatChannelsComponentWrapper().list(true,clientId);
            channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            System.out.println("(Immediate Cache) User's current channel list ");
            for(JsonElement channel: channels){
                System.out.print("Channel Name: "+channel.getAsJsonObject().get("name"));
                System.out.println(" ID: "+channel.getAsJsonObject().get("id"));

            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * join a channel
             */
            //TODO check functioning
            System.out.println("Channel id: "+cid);
            System.out.println("Joining the channel...");
            Thread.sleep(3000);
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponentWrapper().joinChannel(clientId,pathMap);
            System.out.println(response);
            System.out.println("Joined channel with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            response = client.getChatChannelsComponentWrapper().list(true,clientId);
            channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            System.out.println("(Immediate Cache) User's current channel list ");
            for(JsonElement channel: channels){
                System.out.print("Channel Name: "+channel.getAsJsonObject().get("name"));
                System.out.println(" ID: "+channel.getAsJsonObject().get("id"));

            }
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
