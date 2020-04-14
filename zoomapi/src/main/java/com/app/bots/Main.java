package com.app.bots;

import com.app.zoomapi.clients.ApiClient;
import com.app.zoomapi.utilities.Utility;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.ini4j.Wini;
import org.json.JSONObject;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    try {

        //TODO use credentials from here, replace hardcoded
        //reading from bot.ini file
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
        int PORT = 8080;
        NgrokTunnel tunnel = new NgrokTunnel(PORT);
        String url = tunnel.url();
        System.out.println("Redirect url: " + url);

        OAuthClientRequest request = OAuthClientRequest
                .authorizationLocation("https://zoom.us/oauth/authorize")
                .setClientId("nh3twPGRTa2dfaNX41kQww")
                .setRedirectURI(url)
                .setResponseType("code")
                .buildQueryMessage();
        System.out.println(request.getLocationUri());

        //TODO check for URL to open with Google chrome
        //Opens default browser
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(request.getLocationUri());
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Listening at 8080");
        Socket clientSocket = serverSocket.accept(); //browser
        System.out.println("Accepted 8080");
        InputStreamReader isr =  new InputStreamReader(clientSocket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        //TODO check with python
        String fromServer = reader.readLine().split("/",2)[1].split(" ",2)[0];
        String code = fromServer.split("=")[1];
        System.out.println("Code: " + code);

        //TODO remove hardcoded
        clientId = "nh3twPGRTa2dfaNX41kQww";
        clientSecret= "V1imXb3HqOjU9JHyiVgMB8YGtTEoPMzV";
        String encodedBytes = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        OAuthClientRequest request2 = OAuthClientRequest
                    .tokenLocation("https://zoom.us/oauth/token")
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setRedirectURI(url)
                    .setCode(code)
                    .buildQueryMessage();
        request2.setHeader("Authorization","Basic "+ encodedBytes);

        OAuthClient oAuthClient= new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse resourceResponse = oAuthClient.resource(request2, OAuth.HttpMethod.POST, OAuthResourceResponse.class);
        System.out.println("body:"+resourceResponse.getBody());
        JSONObject jsonObj = new JSONObject(resourceResponse.getBody());
        String access_token = jsonObj.get("access_token").toString();
        System.out.println(access_token);

        //TODO write proper
        while (true){
            String line = reader.readLine();
            if(line!=null)
                System.out.println(line);
        }

          /*Thread.sleep(10000);
        Scanner input = new Scanner(System.in);
        String code="code";
        String hostName = "localhost";
        int PORT = 3334;
        try(Socket clientSocket = new Socket(hostName,PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        ){
            System.out.println("Here");
            while (true) {
                String fromClient, fromServer;
                System.out.print("Client (Type input): ");
                fromClient = input.nextLine();
                out.println(fromClient);

               // StringBuilder stringBuilder = new StringBuilder();
                fromServer = in.readLine();
                code = fromServer.split("=")[1];
                System.out.println("Code: " + code);
                break;
            }
        } catch (Exception ex){
            System.err.println(ex);
        }*/


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
