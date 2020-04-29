package com.app.bots;

import com.app.zoomapi.models.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.*;

public class PopulateBot {
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

            HttpResponse<String> response = null;
            /*System.out.println("Create new channel ");
            Scanner in = new Scanner(System.in);
            System.out.println("Enter channel name: ");
            String channelName = "history";//in.nextLine();
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
            final String cid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id: "+cid);
            System.out.println("-------------------------------------------------------------------------------------");
*/
            String cid = null;
            response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<Message> messages = new ArrayList<>();
            for(JsonElement channel:channels){
                if(channel.getAsJsonObject().get("name").getAsString().equals("history")){
                    cid = channel.getAsJsonObject().get("id").getAsString();
                    break;
                }
            }
            for(int i=0;i<50;i++) {
                System.out.println("Enter message: ");
                String message = "new test";//in.nextLine();
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("message", message);
                dataMap.put("to_channel", cid);
                response = client.getChatMessagesComponent().post(dataMap);
                System.out.println("Message sent to Channel " + cid);
                String mId = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
                System.out.println("-------------------------------------------------------------------------------------");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
