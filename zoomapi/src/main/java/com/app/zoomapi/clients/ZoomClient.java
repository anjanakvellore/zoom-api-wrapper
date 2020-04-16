package com.app.zoomapi.clients;

import java.util.HashMap;
import java.util.Map;

public class ZoomClient extends ApiClient{
    private final String APIBASEURI= "https://api.zoom.us/v2";
    private Map<String,Object> config;

    public ZoomClient(String apiKey, String apiSecret, Map<String,Object> varArgs){
        super( new HashMap<String, Object>() {{
            put("baseUri", "https://api.zoom.us/v2");
            put("timeOut", (varArgs!=null && varArgs.containsKey("timeOut"))?varArgs.get("timeOut"):15);
        }});

        config = new HashMap<>();
        config.put("apiKey",apiKey);
        config.put("apiSecret",apiSecret);
        config.put("dataType",(varArgs!=null && varArgs.containsKey("dataType"))?varArgs.get("dataType"):"json");

    }

}
