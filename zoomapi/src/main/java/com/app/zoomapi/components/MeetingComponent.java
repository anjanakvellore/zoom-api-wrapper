package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //check with Kaj - expecting the client to parse date as "string" before passing it here;
    //not doing the conversion here; provided a date to string converter in utils package
    public HttpResponse<String> create(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return postRequest(String.format("/users/%s/meetings",pathMap.get("user_id")),paramMap,null,(String)null,null);
        }
        else{
            return null;
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

    //check with Kaj - expecting the client to parse date as "string" before passing it here;
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
