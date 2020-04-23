package com.app.zoomapi.components;

import com.app.zoomapi.clients.ApiClient;
import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Base component
 */
public class BaseComponent extends ApiClient {
    private Map<String,String> config;

    /**
     * Set up a base component
     * @param baseUri The base URI to the API
     * @param timeOut The timeout to use for requests
     * @param config The config details
     */
    public BaseComponent(String baseUri, Integer timeOut,Map<String,String> config){
        super(config);
        this.config = config;
    }

    /**
     * Set up a base component
     * @param config The config details
     */
    public BaseComponent(Map<String,String> config){
        super(config);
        this.config = config;
    }

    /**
     * Helper function for POST requests
     * Since the Zoom.us API only uses POST requests and each post request
     * must include all of the config data, this method ensures that all
     * of that data is there
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as hashmap to include with the POST
     * @param cookies request cookies
     * @return The response object for this request
     */
    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, Map<String,Object> data,
                                            Map<String,String> cookies) throws IOException, InterruptedException {
        if(headers == null || headers.size() == 0){
            return super.postRequest(endPoint,params,setHeaders(),data,cookies);
        }

        return super.postRequest(endPoint,params,headers,data,cookies);

    }

    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, String data,
                                            Map<String,String> cookies) throws IOException, InterruptedException {


        if(headers == null || headers.size() == 0){
            return super.postRequest(endPoint,params,setHeaders(),data,cookies);
        }

        return super.postRequest(endPoint,params,headers,data,cookies);


    }

    private Map<String,String> setHeaders(){
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization",String.format("Bearer %s",this.config.get("token")));
        return headerMap;

    }

}
