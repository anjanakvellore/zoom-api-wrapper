package com.app.bots;

import com.app.zoomapi.ApiClient;
import org.json.JSONObject;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello");
        String endpoint = "get";
        String baseUri = "https://httpbin.org";
        Map<String,String> headers = new HashMap();
        headers.put("User-Agent", "Java 11 HttpClient Bot");
        Map<String,String> params = new HashMap<>();
        params.put("name","mkyong2");
        Map<String,Map<String,String>> paramMap = new HashMap<>();
        paramMap.put("headers",headers);
        paramMap.put("params",params);
        ApiClient apiClient = new ApiClient(baseUri,15);
        HttpResponse<String> response = apiClient.getRequest(endpoint,paramMap);
        JSONObject jsonObj = new JSONObject(response.body());
        //JSONObject value1 = jsonObj.getJSONArray("key1").getJSONObject(0);
        //JSONObject value2 = jsonObj.getJSONObject("key2);



    }
}
