package com.app.zoomapi.components;

import com.app.zoomapi.clients.ApiClient;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseComponent extends ApiClient {

    public BaseComponent(){
        super();
    }

    public BaseComponent(Map<String,Object> variableArgs){
        super(variableArgs);
    }

    public HttpResponse<String> postRequest(String endPoint, Map<String,Object> variableArgs){

        if(!variableArgs.containsKey("headers")){
            Map<String,String> config = (Map<String,String>)variableArgs.get("config");
            String token = config.get("token");
            Map<String,String> headers = new HashMap<>();
            headers.put("Authorization",String.format("Bearer {0}",token));

        }
        return super.postRequest(endPoint,variableArgs);

    }

}
