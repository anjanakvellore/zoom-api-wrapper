package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Zoom.us REST API Java client - User Component
 * Component dealing with all user related matters
 */
public class UserComponent extends BaseComponent {
    private static UserComponent userComponent = null;

    private UserComponent(Map<String,String> config){
        super(config);
    }

    public static UserComponent getUserComponent(Map<String,String> config){
        if(userComponent == null){
            userComponent = new UserComponent(config);
        }
        return userComponent;
    }

    public HttpResponse<String> list(Map<String,String> paramMap){
        return getRequest("/users",paramMap,null);
    }

    public HttpResponse<String> create(Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"action","user_info"});
        if(Utility.requireKeys(dataMap,reqKeys)) {
            return postRequest("/users", null,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap,Map<String,String> paramMap,Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap, pathKeys)){
            return patchRequest(String.format("/users/%s",pathMap.get("userId")),paramMap,null,dataMap,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> delete(Map<String,Object> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return deleteRequest(String.format( "/users/%s",pathMap.get("userId")),paramMap,null, (String) null,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> get(Map<String,Object> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        if(Utility.requireKeys(pathMap,reqKeys))
            return getRequest(String.format("/users/%s",pathMap.get("userId")),paramMap,null);
        else
            return null;
    }
}
