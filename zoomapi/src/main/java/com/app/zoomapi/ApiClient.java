package com.app.zoomapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class ApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private String baseUri;
    private int timeOut;

    public ApiClient(String baseUri,int timeOut){
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }

    public ApiClient(){
        this.baseUri="";
        this.timeOut = 15;
    }


    public int getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        if(baseUri!=null && !baseUri.isEmpty() && baseUri.endsWith("/")){
            baseUri = baseUri.substring(0,baseUri.length()-1);
        }
        this.baseUri = baseUri;
    }

    public String getUrlForEndPoint(String endpoint){
        if(!endpoint.startsWith("/")){
            endpoint = "/"+endpoint;
        }
        if(endpoint.endsWith("/")){
            endpoint = endpoint.substring(0,endpoint.length()-1);
        }
        return this.baseUri + endpoint;
    }

    public HttpResponse<String> getRequest(String endPoint, Map<String,Map<String,String>> variableArgs){
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();

            String url = getUrlForEndPoint(endPoint);

            if (variableArgs.containsKey("params")) {
                params = variableArgs.get("params");
            }

            if (variableArgs.containsKey("headers")) {
                headers = variableArgs.get("headers");
            }

            if (params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url+  key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }


            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().GET().uri(URI.create(url));

            if (headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            }
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
