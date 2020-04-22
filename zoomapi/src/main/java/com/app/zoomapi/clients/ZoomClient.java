package com.app.zoomapi.clients;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple wrapper for Zoom.us REST API Java client
 */
public class ZoomClient extends ApiClient{

    private Map<String,String> config;

    /**
     * Create a new Zoom API client
     * @param apiKey The Zoom.us API key
     * @param apiSecret The Zoom.us API secret
     * @param dataType The expected return data type. Either 'json' or 'xml'
     * @param timeOut The time out to use for API requests
     */
    public ZoomClient(String apiKey, String apiSecret, String dataType, Integer timeOut){

        super("https://api.zoom.us/v2",timeOut!=null ? timeOut:15);
        /**
         * Set up the config details
         */
        config = new HashMap<>();
        config.put("apiKey",apiKey);
        config.put("apiSecret",apiSecret);
        config.put("dataType", dataType!=null ? dataType : "json");

    }


}
