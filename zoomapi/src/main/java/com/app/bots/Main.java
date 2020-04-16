package com.app.bots;

import com.app.zoomapi.clients.ApiClient;
import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.utilities.Utility;
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

import org.apache.oltu.oauth2.client.OAuthClient;

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

      //get request test: calling get list of channels api - NEED TO MODIFY THIS
        /*ChatChannelsComponent channelsComponent = new ChatChannelsComponent();
        channelsComponent.token = access_token;*/
        //ToDo: no need to pass token to OAuthClient once TokenHandler class is implemented
        Map<String,Object> varArgs = new HashMap<String,Object>();
        varArgs.put("token", access_token);

        com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient(clientId,clientSecret,port,url,browserPath,varArgs);
        HttpResponse<String> response = client.getChatChannelsComponent().list();
        JSONObject json = new JSONObject(response.body());
        JSONArray channels = json.getJSONArray("channels");
        String cid = "";
        for(int i=0; i<channels.length();i++){
            JSONObject channel = channels.getJSONObject(i);
            if(channel.getString("name").equals("test")){
                cid = channel.getString("id");
                break;
            }

        }

        ChatMessagesComponent messagesComponent = client.getChatMessagesComponent();
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
        System.out.println(ex.getMessage());
    }

    }
}
