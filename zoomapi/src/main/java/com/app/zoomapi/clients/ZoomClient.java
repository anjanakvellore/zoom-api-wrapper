package com.app.zoomapi.clients;

import java.util.HashMap;
import java.util.Map;

public class ZoomClient extends ApiClient{
    private final String APIBASEURI= "https://api.zoom.us/v2";
    private Map<String,String> config;

    public ZoomClient(String apiKey, String apiSecret, String dataType, Integer timeOut){

        super("https://api.zoom.us/v2",timeOut!=null ? timeOut:15);

        config = new HashMap<>();
        config.put("apiKey",apiKey);
        config.put("apiSecret",apiSecret);
        config.put("dataType", dataType!=null ? dataType : "json");

    }


}
