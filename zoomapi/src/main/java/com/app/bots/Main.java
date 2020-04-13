package com.app.bots;

import com.app.zoomapi.clients.ApiClient;
import com.app.zoomapi.utilities.Utility;
import org.ini4j.Wini;
import org.json.JSONObject;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
    try {

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
        NgrokTunnel tunnel = new NgrokTunnel(8080);
        String url = tunnel.url();
        System.out.println("Redirect url: " + url);

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
