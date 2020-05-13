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


            System.out.println("Subscribing to new members....");
            EventFramework eventFramework = new EventFramework(client);
            eventFramework.registerForNewMemberEvent(EventHandler.getNewMembers);
            Thread.sleep(50000);
            eventFramework.unRegisterFromNewMemberEvent(EventHandler.getNewMembers);
            while (true){

            }


            /*
            *//**
             * create a channel for testing
             *//*
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
            HttpResponse<String> response = client.getChatChannelsComponent().createChannel(channelMap);
            System.out.println(response);
            String cid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");


            *//**
             * to list user's channels
             *//*
            response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<String> channelsList = new ArrayList<>();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                channelsList.add(channel.getAsJsonObject().get("name").getAsString());
                System.out.println(channel.getAsJsonObject().get("name"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            System.out.println("Subscribing to new members....");
            EventFramework eventFramework = new EventFramework(client);
            eventFramework.registerForNewMemberEvent(EventHandler.getNewMembers);
            Thread.sleep(10000);

            *//**
             * invite members to the channel for testing subscription
             *//*
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

            Thread.sleep(20000);

            System.out.println("Unregistering....");
            eventFramework.unRegisterFromNewMemberEvent(EventHandler.getNewMembers);
            Thread.sleep(20000);

            *//**
             * invite new members to the channel after unregistering
             *//*
            System.out.println("Inviting members to the channel");
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","santhiya.naga@gmail.com");}});
            memberMap = new HashMap<>();
            memberMap.put("members",members);
            System.out.println("Adding santhiya.naga@gmail.com");
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponent().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);
            System.out.println("-------------------------------------------------------------------------------------");
            Thread.sleep(10000);


            while (true){

            }
            //TODO reimplement*/

            /*

            //registering for the new messages event by passing the new message event handler and channel name
            //EventFramework eventFramework = new EventFramework(client);
            //register
            boolean success = eventFramework.registerForNewMessageEvent(EventHandler.getNewMessages, "history");
            eventFramework.registerForUpdateMessageEvent(EventHandler.getUpdatedMessages,"history");

            boolean isUpdate = true;
            //cid = "";
            String mId = "";
            String mid = "";
            //response = null;
            int cnt = 0;

            //response = client.getChatChannelsComponent().list();
            //channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<Message> messages = new ArrayList<>();
            for(JsonElement channel:channels){
                if(channel.getAsJsonObject().get("name").getAsString().equals("history")){
                    cid = channel.getAsJsonObject().get("id").getAsString();
                    break;
                }
            }

            //TODO do something with while true?
            int count = 0;
            //adding couple of messages to test the new message event
            while(true) {

                Map<String, Object> dataMap = new HashMap<>();
                for(int i=0;i<10;i++) {
                    //System.out.println("Enter message: ");
                    String message = "brand new test" + count++;//in.nextLine();
                    dataMap = new HashMap<>();
                    dataMap.put("message", message);
                    dataMap.put("to_channel", cid);
                    response = client.getChatMessagesComponent().post(dataMap);
                   // System.out.println("Message sent to Channel " + cid);
                    mid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
                    //to test update message event
                    if(cnt == 0){
                        mId = mid;
                    }
                   // System.out.println("-------------------------------------------------------------------------------------");
                }

                if(cnt++ == 3){
                    break;
                }
            }
            //test update message event
            //System.out.println("Updating message");
            Map<String, Object> dataMap = new HashMap<>();
            String message = "updated message";
            dataMap = new HashMap<>();
            pathMap = new HashMap<>();
            pathMap.put("messageId",mId);
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = client.getChatMessagesComponent().update(pathMap,null,dataMap);

            Thread.sleep(10000);

            System.out.println("Unregistering....");
            eventFramework.unRegisterFromNewMessageEvent(EventHandler.getNewMessages, "history");
            //checking if new message event triggers after unregistering
            for(int i=0;i<10;i++) {
                //System.out.println("Enter message: ");
                message = "after new test" + count++;//in.nextLine();
                dataMap = new HashMap<>();
                dataMap.put("message", message);
                dataMap.put("to_channel", cid);
                response = client.getChatMessagesComponent().post(dataMap);
                // System.out.println("Message sent to Channel " + cid);
                mid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
                if(cnt == 0){
                    mId = mid;
                }
                // System.out.println("-------------------------------------------------------------------------------------");

            }
            Thread.sleep(20000);
            System.out.println("Unregistering....");
            eventFramework.unRegisterFromUpdateMessageEvent(EventHandler.getUpdatedMessages, "history");

             */
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

//call back functions
class EventHandler{
    static Consumer<Message> getNewMessages = (message)->{
        System.out.println("New message: ");
        System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());

    };

    static Consumer<Message> getUpdatedMessages = (message)->{
        System.out.println("Updated message: ");
        System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());
    };

    static Consumer<Member> getNewMembers = (member)->{
        System.out.println(member.getFirstName() + " " + member.getLastName() + "(" + member.getEmail() + ") added as "+
                member.getRole()+ " in " +member.getChannel());
    };
}
