package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Zoom.us REST API Java client - Recording Component
 * Component dealing with all recording related matters
 */
public class RecordingComponent extends BaseComponent {
    private static RecordingComponent recordingComponent = null;

    private RecordingComponent(Map<String,String> config){
        super(config);
    }

    public static RecordingComponent getRecordingComponent(Map<String,String> config){
        if(recordingComponent== null){
            recordingComponent = new RecordingComponent(config);
        }
        return recordingComponent;
    }

    public HttpResponse<String> list(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/users/%s/recordings",pathMap.get("user_id")),paramMap,null);
        }catch (Exception ex){
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

    public HttpResponse<String> get(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"meeting_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return getRequest(String.format("/meetings/%s/recordings",pathMap.get("meeting_id")),paramMap,null);
        }catch (Exception ex){
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

    public HttpResponse<String> delete(Map<String,String> pathMap,Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"meeting_id"});
        try{
            Utility.requireKeys(pathMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null)
                paramMap = Utility.convertMap(initialParamMap);
            return deleteRequest(String.format("/meetings/%s/recordings",pathMap.get("meeting_id")),paramMap,null,(String)null,null);
        }catch (Exception ex){
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
}
