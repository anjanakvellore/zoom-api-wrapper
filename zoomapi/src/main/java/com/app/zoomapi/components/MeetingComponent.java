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

    public HttpResponse<String> list(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return getRequest(String.format("/users/%s/meetings",pathMap.get("user_id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    //TODO Santhiya: Check this for reference
    public HttpResponse<String> create(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        try {
            //if (Utility.requireKeys(pathMap, reqKeys)) {
            Utility.requireKeys(pathMap, reqKeys);
            if (initialParamMap.containsKey("start_time")) {
                initialParamMap.put("start_time", Utility.dateToString((Date) initialParamMap.get("start_time")));
            }
            Map<String, String> paramMap = Utility.convertMap(initialParamMap);

            return postRequest(String.format("/users/%s/meetings", pathMap.get("user_id")), paramMap, null, (String) null, null);
        }
        catch (Exception ex){
            return new HttpResponse<String>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return ex.getMessage();
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };
        }

    }

    public HttpResponse<String> get(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return getRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    //TODO check with Kaj - expecting the client to parse date as "string" before passing it here;
    //not doing the conversion here; provided a date to string converter in utils package
    public HttpResponse<String> update(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return patchRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null,(String)null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> delete(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"id"});
        if(Utility.requireKeys(paramMap,reqKeys)){
            return deleteRequest(String.format("/meetings/%s",pathMap.get("id")),paramMap,null,(String)null,null);
        }
        else{
            return null;
        }
    }


}
