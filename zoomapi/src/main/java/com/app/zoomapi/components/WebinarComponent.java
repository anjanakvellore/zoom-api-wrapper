package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Zoom.us REST API Java client - Webinar Component
 * Component dealing with all webinar related matters
 */
public class WebinarComponent extends BaseComponent {

    private static WebinarComponent webinarComponent = null;

    public WebinarComponent(Map<String, String> config) {
        super(config);
    }

    public static WebinarComponent getWebinarComponent(Map<String,String> config){
        if(webinarComponent == null){
            webinarComponent = new WebinarComponent(config);
        }
        return webinarComponent;
    }

    public HttpResponse<String> list(Map<String,Object> pathMap, Map<String,String> paramMap ){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap,reqKeys))
            return getRequest(String.format("/users/%s/webinars",pathMap.get("userId")),paramMap,null);
        else
            return null;
    }

    public HttpResponse<String> create(Map<String,Object> pathMap,Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap,reqKeys)) {
            return postRequest(String.format("/users/%s/webinars",pathMap.get("userId")),null,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return patchRequest(String.format( "/webinars/%s",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> delete(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return deleteRequest(String.format( "/webinars/%s",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> end(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        paramMap.put("status","end");
        if(Utility.requireKeys(pathMap,reqKeys)){
            return putRequest(String.format( "/webinars/%s/status",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> get(Map<String,Object> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        if(Utility.requireKeys(pathMap,reqKeys))
            return getRequest(String.format("/webinars/%s",pathMap.get("webinarId")),paramMap,null);
        else
            return null;
    }

    public HttpResponse<String> register(Map<String,Object> pathMap,Map<String,String> paramMap,Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        List<String> dataKeys = Arrays.asList(new String[]{"email", "first_name", "last_name"});
        if(Utility.requireKeys(pathMap,reqKeys) && Utility.requireKeys(dataMap,dataKeys)) {
            return postRequest(String.format("/webinars/%s/registrants",pathMap.get("webinarId")),paramMap,null,dataMap,null);
        }
        else{
            return null;
        }
    }
}
