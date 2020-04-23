package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.net.ssl.SSLSession;
import javax.print.DocFlavor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Zoom.us REST API Java client - Report Component
 * Component dealing with all report related matters
 */
public class ReportComponent extends BaseComponent{
    private static ReportComponent reportComponent = null;

    private ReportComponent(Map<String,String> config){
        super(config);
    }

    public static ReportComponent getReportComponent(Map<String,String> config){
        if(reportComponent== null){
            reportComponent = new ReportComponent(config);
        }
        return reportComponent;
    }

    public HttpResponse<String> getUserReport(Map<String,String> pathMap, Map<String,Object> initialParamMap){
        List<String> pathKeys = Arrays.asList(new String[]{"user_id"});
        List<String> paramKeys = Arrays.asList(new String[]{"start_time","end_time"});
        try{
            Utility.requireKeys(pathMap,pathKeys);
            Utility.requireKeys(initialParamMap,paramKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null){
                if (initialParamMap.containsKey("start_time") && initialParamMap.containsKey("end_time")) {
                    initialParamMap.put("from", Utility.dateToString((Date) initialParamMap.get("start_time")));
                    initialParamMap.remove("start_time");
                    initialParamMap.put("to", Utility.dateToString((Date) initialParamMap.get("end_time")));
                    initialParamMap.remove("end_time");
                }
                paramMap = Utility.convertMap(initialParamMap);
            }
            return getRequest(String.format("/report/users/%s/meetings",pathMap.get("user_id")),paramMap,null);
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

    public HttpResponse<String> getAccountReport(Map<String,Object> initialParamMap){
        List<String> reqKeys = Arrays.asList(new String[]{"start_time","end_time"});
        try{
            Utility.requireKeys(initialParamMap,reqKeys);
            Map<String, String> paramMap = null;
            if(initialParamMap!=null){
                if (initialParamMap.containsKey("start_time") && initialParamMap.containsKey("end_time")) {
                    initialParamMap.put("from", Utility.dateToString((Date) initialParamMap.get("start_time")));
                    initialParamMap.remove("start_time");
                    initialParamMap.put("to", Utility.dateToString((Date) initialParamMap.get("end_time")));
                    initialParamMap.remove("end_time");
                }
                paramMap = Utility.convertMap(initialParamMap);
            }
            return getRequest("/report/users",paramMap,null);
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