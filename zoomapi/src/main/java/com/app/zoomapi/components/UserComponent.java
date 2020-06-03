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

    public HttpResponse<String> list(Map<String,Object> initialParamMap){
        try {
            Map<String, String> paramMap = null;
            if (initialParamMap != null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest("/users", paramMap, null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> create(Map<String,Object> dataMap){
        List<String> reqKeys = Arrays.asList(new String[]{"action","user_info"});
        try {
            Utility.requireKeys(dataMap,reqKeys);
            return postRequest("/users", null,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> update(Map<String,Object> pathMap,Map<String,Object> initialParamMap,Map<String,Object> dataMap){
        List<String> pathKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap, pathKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return patchRequest(String.format("/users/%s",pathMap.get("userId")),paramMap,null,dataMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }

    public HttpResponse<String> delete(Map<String,Object> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return deleteRequest(String.format( "/users/%s",pathMap.get("userId")),paramMap,null, (String) null,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }


    public HttpResponse<String> get(Map<String,Object> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"userId"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/users/%s",pathMap.get("userId")),paramMap,null);
        }catch (Exception ex){
            return Utility.getStringHttpResponse(0,ex.getMessage());
        }
    }
}
