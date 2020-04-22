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

public class Main {

    public static void main(String[] args) {
        try {
            File file=new File(
                    Main.class.getClassLoader().getResource("bot.ini").getFile()
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

            /*
             * Creating ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             */

            NgrokTunnel tunnel=new NgrokTunnel(port);
            String url=tunnel.url();
            System.out.println("Redirect url:"+url);

            //TODO use user.get
            com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient
                    (clientId,clientSecret,port,url,browserPath,null,null);

            //actual testing begins here
            //list channels
            HttpResponse<String> response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            System.out.println("Getting user's channels: ");
            for(JsonElement channel: channels){
                System.out.println(channel.getAsJsonObject().get("name"));
            }

            //create channels
            System.out.println("Create new channel: ");
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
            response = client.getChatChannelsComponent().createChannel(channelMap);
            System.out.println(response);
            String cid = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
            System.out.println("Channel created with id - "+cid);

            //invite members
            System.out.println("Inviting members to the channel");
            members = new ArrayList<>();
            members.add(new HashMap<String,String>(){{put("email","santhiyn@uci.edu");}});
            Map<String,Object> memberMap = new HashMap<>();
            memberMap.put("members",members);
            Map<String,Object> pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponent().inviteChannelMembers(pathMap,memberMap);
            System.out.println(response);

            //list channel members
            System.out.println("Listing channel members");
            pathMap = new HashMap<>(){{put("channel_id",cid);}};
            response = client.getChatChannelsComponent().listChannelMembers(pathMap);
            JsonArray array = JsonParser.parseString(response.body()).getAsJsonObject().get("members").getAsJsonArray();
            for(JsonElement ar:array){
                System.out.println(ar.getAsJsonObject().get("email"));
            }

            //update channel name
            System.out.println("Updating channel name");
            System.out.println("Enter channel name: ");
            channelName = in.nextLine();
            Map<String,Object> data = new HashMap<>();
            data.put("name",channelName);
            pathMap = new HashMap<>();
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().update(pathMap,data);
            System.out.println(response);

            //get channel details
            System.out.println("Get channel details");
            pathMap = new HashMap<>();
            pathMap.put("channel_id",cid);
            response = client.getChatChannelsComponent().get(pathMap);
            channelName = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();
            System.out.println("Name: " + channelName);

        /*

        //delete channel
        HashMap<String,Object> data = new HashMap<>();
        data.put("channel_id",cid);
        response = client.getChatChannelsComponent().delete(data);
        System.out.println(response.body());*/

        /*ChatMessagesComponent messagesComponent = client.getChatMessagesComponent();
        boolean isStop = false;
        while (!isStop) {
            System.out.println("Enter your message: ");
            String message = in.nextLine();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("message", message);
            dataMap.put("to_channel", cid);
            response = messagesComponent.post(dataMap);
            if(message.equals("stop"))
                isStop = true;
        }*/

        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
