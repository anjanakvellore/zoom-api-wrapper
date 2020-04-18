package com.app.bots;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.ini4j.Wini;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;

import org.apache.oltu.oauth2.client.OAuthClient;

import javax.print.DocFlavor;

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

        //creating ngrok tunnel-need to run ngrok start --none on terminal for this to work
        int PORT=8080;
        NgrokTunnel tunnel=new NgrokTunnel(PORT);
        String url=tunnel.url();
        System.out.println("Redirecturl:"+url);

        OAuthClientRequest request=OAuthClientRequest
                .authorizationLocation("https://zoom.us/oauth/authorize")
                .setClientId("410Lxj9DQ4qUthIkKf9aRg")
                .setRedirectURI(url)
                .setResponseType("code")
                .buildQueryMessage();
        System.out.println(request.getLocationUri());

        //TODO check for URL to open with Google chrome
        //Opens default browser
        try{
            Desktop desktop=java.awt.Desktop.getDesktop();
            URI oURL=new URI(request.getLocationUri());
            desktop.browse(oURL);
        }catch(Exception e){
            e.printStackTrace();
        }

        final ServerSocket serverSocket=new ServerSocket(PORT);
        System.out.println("Listeningat8080");
        Socket clientSocket=serverSocket.accept();//browser
        System.out.println("Accepted8080");
        InputStreamReader isr=new InputStreamReader(clientSocket.getInputStream());
        BufferedReader reader=new BufferedReader(isr);
        //TODO checkwithpython
        String readerStr = reader.readLine();
        //String fromServer=readerStr.split("/",2)[1].split("",2)[0];
        String code = readerStr.split("/")[1].split("=")[1].split(" ")[0];
        //String code=fromServer.split("=")[1];
        System.out.println("Code:"+code);

        //TODO removehardcoded
        clientId="410Lxj9DQ4qUthIkKf9aRg";
        clientSecret="aNLKtT5ouEDaIiHeYmI7fbfWiE7yuwxq";
        String encodedBytes=Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());

        OAuthClientRequest request2=OAuthClientRequest
                .tokenLocation("https://zoom.us/oauth/token")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setRedirectURI(url)
                .setCode(code)
                .buildQueryMessage();
        request2.setHeader("Authorization","Basic"+encodedBytes);

        OAuthClient oAuthClient=new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse resourceResponse=oAuthClient.resource(request2, OAuth.HttpMethod.POST,OAuthResourceResponse.class);
        System.out.println("body:"+resourceResponse.getBody());
        JSONObject jsonObj=new JSONObject(resourceResponse.getBody());
        String access_token=jsonObj.get("access_token").toString();
        System.out.println(access_token);

        //TODO write proper
        /*while(true){
            String line=reader.readLine();
            if(line!=null)
                System.out.println(line);
        }*/


        //ToDo: no need to pass token to OAuthClient once TokenHandler class is implemented
        Map<String,Object> varArgs = new HashMap<String,Object>();
        varArgs.put("token", access_token);
        com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient(clientId,clientSecret,port,url,browserPath,null,null,access_token);

        //actual testing begins here
        //list channels
        HttpResponse<String> response = client.getChatChannelsComponent().list();
        Gson gson = new Gson();
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
