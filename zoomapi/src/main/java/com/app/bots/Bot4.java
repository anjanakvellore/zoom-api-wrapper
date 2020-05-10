package com.app.bots;

import com.app.zoomapi.events.EventFramework;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            //registering for the new messages event by passing the new message event handler and channel name
            EventFramework eventFramework = new EventFramework(client);
            //register
            eventFramework.registerForNewMessageEvent(EventHandler.getNewMessages,"history");
            eventFramework.registerForUpdateMessageEvent(EventHandler.getUpdatedMessages,"history");

            boolean isUpdate = true;
            String mId = "";
            String cid = "";
            String mid = "";
            HttpResponse<String> response = null;
            int cnt = 0;

            response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<Message> messages = new ArrayList<>();
            for(JsonElement channel:channels){
                if(channel.getAsJsonObject().get("name").getAsString().equals("history")){
                    cid = channel.getAsJsonObject().get("id").getAsString();
                    break;
                }
            }

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
            Map<String,Object> pathMap = new HashMap<>();
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
}
