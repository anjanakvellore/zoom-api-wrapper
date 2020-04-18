package com.app.zoomapi.components;

import com.app.zoomapi.clients.ApiClient;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseComponent extends ApiClient {

    private Map<String,String> config;

    //ToDo: how to prevent sending "null" to config?
    public BaseComponent(String baseUri, Integer timeOut,Map<String,String> config){
        super(config);
        this.config = config;
    }

    public BaseComponent(Map<String,String> config){
        super(config);
        this.config = config;
    }


    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, Map<String,Object> data,
                                            Map<String,String> cookies){
        if(headers == null || headers.size() == 0){
            return super.postRequest(endPoint,params,setHeaders(),data,cookies);
        }

        return super.postRequest(endPoint,params,headers,data,cookies);

    }

    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, String data,
                                            Map<String,String> cookies){


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
