package com.app.bots;

import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Member;
import com.app.zoomapi.repo.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

public class Bot5 {
    public static void main(String[] args) {

        try {
            //test code
            String path = "C:/Spring/sqlite/mydb.db";
           // SQLiteGenericTableHandler<ChannelMaster> sqLiteGenericTableHandler = new SQLiteGenericTableHandler<>(path,new ChannelMaster());
            //SQLiteGenericTableHandler<ChannelMaster> sqLiteGenericTableHandler = new SQLiteGenericTableHandler<>(path,new ChannelMaster());
            SQLiteGenericTableHandler<ChannelMaster> sqLiteGenericTableHandler = new SQLiteGenericTableHandler<>(path,ChannelMaster::new);
            //sqLiteGenericTableHandler.insertRow(new ChannelMaster(3,"cid3","announcements",2));
            List<String> fields = Arrays.asList(new String[]{"channelName"});
            List<String> keys = Arrays.asList(new String[]{"'test'"});
            sqLiteGenericTableHandler.delete(fields,keys);
            List<ChannelMaster> channels = sqLiteGenericTableHandler.get(null);
            System.out.println(channels.size());

            /*

/**
             * to read credentials for bot.ini
             *//*

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

            */
/**
             * Creating ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             *//*


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

            */
/**
             * to list user's channels
             *//*

            HttpResponse<String> response = client.getChatChannelsComponent().list();
            JsonArray channels = JsonParser.parseString(response.body()).getAsJsonObject().get("channels").getAsJsonArray();
            List<String> channelsListName = new ArrayList<>();
            List<String> channelsListId = new ArrayList<>();
            List<Integer> channelsListType = new ArrayList<>();
            System.out.println("User's current channel list ");
            for(JsonElement channel: channels){
                channelsListId.add(channel.getAsJsonObject().get("id").getAsString());
                channelsListName.add(channel.getAsJsonObject().get("name").getAsString());
                channelsListType.add(channel.getAsJsonObject().get("type").getAsInt());
                System.out.println(channel.getAsJsonObject().get("name"));
            }
            System.out.println("-------------------------------------------------------------------------------------");

            */
/**
             * Creates new database when there is no entry for clientID
             *//*

            //TODO put client ID check
            DatabaseHelper db = new DatabaseHelper();
            String dbName = "testZoom3.db";
            db.createNewDatabase(dbName);

            //TODO make separate class?
            //TODO account for next page token
            ChannelMaster channelMaster = new ChannelMaster();
            channelMaster.createNewTable(dbName);
            for(int i=0; i<channelsListName.size(); i++){
                channelMaster.insert(dbName,channelsListId.get(i),channelsListName.get(i),channelsListType.get(i));
            }
*/

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
   }
}
