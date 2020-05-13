package com.app.bots;

import com.app.zoomapi.events.EventFramework;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Member;
import com.app.zoomapi.models.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Consumer;

//Note: We have put enough delays in the bot program to see the results from the events
public class Bot4 {
    public static void main(String[] args) {
        try {
            /**
             * to read credentials for bot.ini
             */
            File file = new File(
                    OAuthBot.class.getClassLoader().getResource("bot.ini").getFile()
            );
            Wini ini = new Wini(file);
            String clientId = ini.get("OAuth", "client_id");
            String clientSecret = ini.get("OAuth", "client_secret");
            String portStr = ini.get("OAuth", "port");

            int port = 4001;
            if (portStr != null) {
                port = Integer.parseInt(portStr);
            }
            String browserPath = ini.get("OAuth", "browser_path");

            /**
             * Creating ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             */

            NgrokTunnel tunnel = new NgrokTunnel(port);
            String url = tunnel.url();
            System.out.println("Redirect url:" + url);

            com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient
                    (clientId, clientSecret, port, url, browserPath, null, null);

            Map<String,Object> pathMap = new HashMap<>(){{put("userId","me");}};
            HttpResponse<String> userResponse = client.getUserComponent().get(pathMap,null);
            System.out.println(userResponse);
            String userId = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("id").getAsString();
            String email = JsonParser.parseString(userResponse.body()).getAsJsonObject().get("email").getAsString();
            System.out.println("User ID: "+userId+" User email: "+email);
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * create a channel for testing
             */
            System.out.println("Let's create a new channel for testing! ");
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
            HttpResponse<String> response = client.getChatChannelsComponent().createChannel(channelMap);
            System.out.println(response);
            String cid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");


            /**
             * to list user's channels
             */
            response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<String> channelsList = new ArrayList<>();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                channelsList.add(channel.getAsJsonObject().get("name").getAsString());
                System.out.println(channel.getAsJsonObject().get("name"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * Subscribing to new members event
             */
            System.out.println("Subscribing to new members....");
            EventFramework eventFramework = new EventFramework(client);
            eventFramework.registerForNewMemberEvent(EventHandler.getNewMembers);
            Thread.sleep(20000);

            /**
             * invite members to the channel for testing subscription
             */
            System.out.println("Let's invite members to the channel to test subscription!");
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","santhiyn@uci.edu");}});
            Map<String,Object> memberMap = new HashMap<>();
            memberMap.put("members",members);
            System.out.println("Adding santhiyan@uci.edu");
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(50000);

            /**
             * To check unsubscription from new member event
             */
            System.out.println("Unregistering from new member event....");
            eventFramework.unRegisterFromNewMemberEvent(EventHandler.getNewMembers);
            Thread.sleep(20000);

            /**
             * invite new members to the channel after unregistering
             */
            System.out.println("Inviting members to the channel after unsubscription..");
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","santhiya.naga@gmail.com");}});
            memberMap = new HashMap<>();
            memberMap.put("members",members);
            System.out.println("Adding santhiya.naga@gmail.com");
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(10000);

            /**
             * to list user's channels
             */
            response = client.getChatChannelsComponent().list();
            channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            channelsList = new ArrayList<>();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                channelsList.add(channel.getAsJsonObject().get("name").getAsString());
                System.out.println(channel.getAsJsonObject().get("name"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            System.out.println("Enter channel name for testing new message and update message event! ");
            channelName = in.nextLine();

            /**
             * Registering for the new messages & update messages event by passing the new message event handler and channel name
             */
            if(eventFramework.registerForNewMessageEvent(EventHandler.getNewMessages, channelName)
                    && eventFramework.registerForUpdateMessageEvent(EventHandler.getUpdatedMessages,channelName)){

                for(JsonElement channel:channels){
                    if(channel.getAsJsonObject().get("name").getAsString().equals(channelName)){
                        cid = channel.getAsJsonObject().get("id").getAsString();
                        break;
                    }
                }


                /**
                 * To test new message event
                 */
                System.out.println("Adding couple of messages to test the new message event...");
                int msgNumber = 0, counter = 0;
                String mId = "", mid;
                while(true) {
                    Map<String, Object> dataMap = new HashMap<>();
                    for(int i=0;i<3;i++) {
                        String message = "Brand new test " + msgNumber++;
                        dataMap = new HashMap<>();
                        dataMap.put("message", message);
                        dataMap.put("to_channel", cid);
                        response = client.getChatMessagesComponent().post(dataMap);
                        System.out.println("(Bot) Message sent to Channel: " + message);
                        mid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();

                        //to test update message event
                        if(counter == 0){
                            mId = mid;
                        }
                    }

                    if(counter++ == 3){
                        break;
                    }
                }
                System.out.println("-------------------------------------------------------------------------------------");

                /**
                 * To test update message event
                 */
                System.out.println("Changing first message sent previously to test the update message event...");
                Map<String, Object> dataMap = new HashMap<>();
                String message = "Updated message";
                dataMap = new HashMap<>();
                pathMap = new HashMap<>();
                pathMap.put("messageId",mId);
                dataMap.put("message", message);
                dataMap.put("to_channel", cid);
                response = client.getChatMessagesComponent().update(pathMap,null,dataMap);
                System.out.println("(Bot) Message updated in Channel: " + message);
                Thread.sleep(20000);
                System.out.println("-------------------------------------------------------------------------------------");

                /**
                 * To check if new message event is triggered after unsubscription
                 */
                System.out.println("Unregistering from new message event...");
                eventFramework.unRegisterFromNewMessageEvent(EventHandler.getNewMessages, channelName);
                System.out.println("Checking if a new message event triggers after unregistering from new message event...");
                for(int i=0;i<3;i++) {
                    message = "After unregistering test " + msgNumber++;
                    dataMap = new HashMap<>();
                    dataMap.put("message", message);
                    dataMap.put("to_channel", cid);
                    response = client.getChatMessagesComponent().post(dataMap);
                    System.out.println("(Bot) Message sent to Channel: " + message);
                    mid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
                    if(counter == 0){
                        mId = mid;
                    }
                }
                Thread.sleep(20000);
                System.out.println("-------------------------------------------------------------------------------------");

                /**
                 * To check if update message event is triggered after unsubscription
                 */
                System.out.println("Unregistering from update message event...");
                eventFramework.unRegisterFromUpdateMessageEvent(EventHandler.getUpdatedMessages, channelName);
                System.out.println("Checking if an update message event triggers after unregistering from update message event...");
                message = "Updated message after unsubscribing";
                pathMap.put("messageId",mId);
                dataMap.put("message", message);
                dataMap.put("to_channel", cid);
                response = client.getChatMessagesComponent().update(pathMap,null,dataMap);
                System.out.println("(Bot) Message updated in Channel: " + message);
                Thread.sleep(20000);
                System.out.println("-------------------------------------------------------------------------------------");

            }
            else
                System.out.println("Subscription unsuccessful :(");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
