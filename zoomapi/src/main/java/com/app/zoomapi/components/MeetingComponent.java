package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Zoom.us REST API Java client - Meeting Component
 * Component dealing with all meeting related matters
 */
public class MeetingComponent extends BaseComponent {
    private static MeetingComponent meetingComponent = null;

    private MeetingComponent(Map<String,String> config){
        super(config);
    }

    public static MeetingComponent getMeetingComponent(Map<String,String> config){
        if(meetingComponent == null){
            meetingComponent = new MeetingComponent(config);
        }
        return meetingComponent;
    }

    public HttpResponse<String> list(Map<String,String> pathMap,Map<String,Object> initialParamMap) {
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/users/%s/meetings",pathMap.get("user_id")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> create(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        try {
            Utility.requireKeys(pathMap, reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null){
                if (initialParamMap.containsKey("start_time")) {
                    initialParamMap.put("start_time", Utility.dateToString((Date) initialParamMap.get("start_time")));
                }
                paramMap = Utility.convertMap(initialParamMap);
            }
            return postRequest(String.format("/users/%s/meetings", pathMap.get("user_id")), paramMap, null, (String) null, null);
        }
        catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }

    }

    public HttpResponse<String> get(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return patchRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null,(String)null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> delete(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        try{
            Utility.requireKeys(initialParamMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return deleteRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null,(String)null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }
}
