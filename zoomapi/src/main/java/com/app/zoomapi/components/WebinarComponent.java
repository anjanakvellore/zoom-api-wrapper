package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public HttpResponse<String> list(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/users/%s/webinars",pathMap.get("userId")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> create(Map<String,Object> pathMap,Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            return postRequest(String.format("/users/%s/webinars",pathMap.get("userId")),null,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return patchRequest(String.format( "/webinars/%s",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> delete(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return deleteRequest(String.format( "/webinars/%s",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> end(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        initialParamMap.put("status","end");
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = Utility.convertMap(initialParamMap);
            return putRequest(String.format( "/webinars/%s/status",pathMap.get("webinarId")),paramMap,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> get(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/webinars/%s",pathMap.get("webinarId")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> register(Map<String,Object> pathMap,Map<String,Object> initialParamMap,Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"webinarId"});
        List<String> dataKeys = Arrays.asList(new String[]{"email", "first_name", "last_name"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Utility.requireKeys(dataMap,dataKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return postRequest(String.format("/webinars/%s/registrants",pathMap.get("webinarId")),paramMap,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }
}
