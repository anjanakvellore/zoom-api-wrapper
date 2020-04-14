package com.app.bots;

import com.app.zoomapi.clients.ApiClient;
import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.utilities.Utility;
import org.ini4j.Wini;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.StandardSocketOptions;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {

    public static void main(String[] args) {
    try {

      /*  //reading from bot.ini file
        File file = new File(
                Main.class.getClassLoader().getResource("bot.ini").getFile()
        );
        Wini ini = new Wini(file);
        String clientId = ini.get("OAuth", "client_id");
        String clientSecret = ini.get("OAuth", "client_secret");
        String portStr = ini.get("OAuth", "port");
        int port = 4001;
        if(portStr != null){
            port = Integer.parseInt(portStr);
        }
        String browserPath = ini.get("OAuth", "browser_path");

         //creating ngrok tunnel - need to run ngrok start --none on terminal for this to work
        NgrokTunnel tunnel = new NgrokTunnel(8080);
        String url = tunnel.url();
        System.out.println("Redirect url: " + url);*/


      //get request test: calling get list of channels api - NEED TO MODIFY THIS
        ChatChannelsComponent channelsComponent = new ChatChannelsComponent();
        ChatMessagesComponent messagesComponent = new ChatMessagesComponent();
        HttpResponse<String> response = channelsComponent.list();
        JSONObject jsonObj = new JSONObject(response.body());
        JSONArray channels = jsonObj.getJSONArray("channels");
        String cid = "";
        for(int i=0; i<channels.length();i++){
            JSONObject channel = channels.getJSONObject(i);
            if(channel.getString("name").equals("test")){
                cid = channel.getString("id");
                break;
            }

        }
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your message: ");
        String message =in. nextLine();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("message",message);
        dataMap.put("to_channel",cid);
        response = messagesComponent.post(dataMap);










      //map to json conversion test
        /*Map<String, Object> map = new HashMap<>();
        List<Map<String,String >> mapList = new ArrayList();
        Map<String,String> emailMap = new HashMap<>();
        emailMap.put("email","anjana@gmail.com");
        mapList.add(emailMap);
        emailMap = new HashMap<>();
        emailMap.put("email","anjana@uci.edu");
        mapList.add(emailMap);
        emailMap = new HashMap<>();
        emailMap.put("email","anjana@yahoo.com");
        mapList.add(emailMap);
        map.put("anjana",mapList);
        String json = new com.google.gson.Gson().toJson(map);
        System.out.println(json);*/

        /*
        // write your code here
        System.out.println("Hello");
        String endpoint = "get";
        String baseUri = "https://httpbin.org";
        Map<String, String> headers = new HashMap();
        headers.put("User-Agent", "Java 11 HttpClient Bot");
        Map<String, String> params = new HashMap<>();
        params.put("name", "mkyong2");
        Map<String, Map<String, String>> paramMap = new HashMap<>();
        paramMap.put("headers", headers);
        paramMap.put("params", params);
        ApiClient apiClient = new ApiClient(baseUri, 15);
        HttpResponse<String> response = apiClient.getRequest(endpoint, paramMap);
        JSONObject jsonObj = new JSONObject(response.body());
        //JSONObject value1 = jsonObj.getJSONArray("key1").getJSONObject(0);
        //JSONObject value2 = jsonObj.getJSONObject("key2);
*/
    }
    catch (Exception ex){
        ex.getMessage();
    }

    }
}
