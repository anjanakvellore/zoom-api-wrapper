package com.app.bots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;
import java.io.File;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;

/**
 * program exercises meaningful testing of Chat channels and Chat messages components
 * thread.sleep() statements are included for optimal user experience to
 * notice changes in the app corresponding to the terminal, 
 * program will work without those as rate limiting has been implemented.
 */
public class OAuthBot {

    public static void main(String[] args) {
        try {
            /**
             * to read credentials for bot.ini
             */
            File file=new File(
                    OAuthBot.class.getClassLoader().getResource("bot.ini").getFile()
            );
            Wini ini=new Wini(file);
            String clientId=ini.get("OAuth","client_id");
            String clientSecret=ini.get("OAuth","client_secret");
            String portStr=ini.get("OAuth","port");

            int port=4001;
            if(portStr!=null){
                port=Integer.parseInt(portStr);
            }
            String browserPath=ini.get("OAuth","browser_path");

            /**
             * Creating ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             */

            NgrokTunnel tunnel=new NgrokTunnel(port);
            String url=tunnel.url();
            System.out.println("Redirect url:"+url);


            com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient
                    (clientId,clientSecret,port,url,browserPath,null,null);
            Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
            HttpResponse<String> userResponse = client.getUserComponent().get(pathMap,null);
            System.out.println(userResponse);
            String userId = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("id").getAsString();
            String email = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("email").getAsString();
            System.out.println("User ID: "+userId+" User email: "+email);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * to list user's channels
             */
            HttpResponse<String> response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                System.out.println(channel.getAsJsonObject().get("name"));
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
            response = client.getChatChannelsComponent().createChannel(channelMap);
            System.out.println(response);
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
            response = client.getChatChannelsComponent().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * list channel members
             */
            System.out.println("Getting list of members in the channel "+cid);
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponent().listChannelMembers(pathMap,null);
            JsonArray array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

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
            response = client.getChatChannelsComponent().update(pathMap,data);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * get channel details
             */
            System.out.println("Getting channel details for channel "+cid);
            pathMap = new HashMap<>();
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().get(pathMap);
            channelName = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();
            System.out.println("Name: " + channelName);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * send a chat message
             */
            System.out.println("Enter message: ");
            String message = in.nextLine();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = client.getChatMessagesComponent().post(dataMap);
            System.out.println("Message sent to Channel "+cid);
            String mId = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * List user's chat messages
             */
            pathMap = new HashMap<>(){{put("userId",userId);}};
            Map<String, Object> paramMap = new HashMap<>(){{put("to_channel",cid);}};
            Thread.sleep(2000);
            response = client.getChatMessagesComponent().list(pathMap,paramMap);
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

            /**
             * Update a chat message
             */
            System.out.println("Enter updated message for the above message: ");
            message = in.nextLine();
            pathMap.put("messageId",mId);
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = client.getChatMessagesComponent().update(pathMap,null,dataMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            /**
             * Delete a message
             */
            System.out.println("Deleting the above message...");
            Thread.sleep(3000);
            pathMap.put("messageId",mId);
            response = client.getChatMessagesComponent().delete(pathMap,paramMap);
            System.out.println(response);
            System.out.println("Message deleted in Channel "+cid);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            /**
             * remove a member
             */
            System.out.println("Removing santhiyn@uci.edu");
            pathMap.put("channel_id",cid);
            pathMap.put("member_id","santhiyn@uci.edu");
            response = client.getChatChannelsComponent().remove(pathMap);
            System.out.println(response);
            System.out.println("Removed member");
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            /**
             * leave a channel
             */
            System.out.println("Channel id: "+cid);
            System.out.println("Leaving the channel...");
            Thread.sleep(3000);
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().leaveChannel(pathMap);
            System.out.println(response);
            System.out.println("Left channel with id: "+cid);
            System.out.println("The admin powers are given to the existing members!");
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(4000);

            /**
             * join a channel
             */
            System.out.println("Channel id: "+cid);
            System.out.println("Joining the channel...");
            Thread.sleep(3000);
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().joinChannel(pathMap);
            System.out.println(response);
            System.out.println("Joined channel with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            /**
             * create a new channel to test deletion
             * as you are not the admin for previously created after leaving the channel
             */
            System.out.println("You are not the admin anymore for the above channel.");
            System.out.println("Let's create new channel to test deleting! ");
            System.out.println("Enter channel name: ");
            channelName = in.nextLine();
            channelType = "1";
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","anjanak1@uci.edu");}});
            channelMap = new HashMap<>();
            channelMap.put("name",channelName);
            channelMap.put("type",channelType);
            channelMap.put("members",members);
            System.out.println("Adding anjanak1@uci.edu");
            response = client.getChatChannelsComponent().createChannel(channelMap);
            System.out.println(response);
            String newCid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id: "+newCid);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(3000);

            /**
             * delete a channel
             */
            System.out.println("Channel Id:"+newCid);
            System.out.println("Deleting the channel...");
            Thread.sleep(2000);
            dataMap.put("channel_id",newCid);
            response = client.getChatChannelsComponent().delete(dataMap);
            System.out.println(response);
            System.out.println("Deleted channel with id: "+newCid);
            Thread.sleep(3000);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
