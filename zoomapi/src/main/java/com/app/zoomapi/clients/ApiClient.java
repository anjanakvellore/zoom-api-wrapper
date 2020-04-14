package com.app.zoomapi.clients;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mashape.unirest.http.ObjectMapper;
import org.json.JSONObject;

import javax.net.ssl.SSLSession;

public class ApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private String baseUri = "https://api.zoom.us/v2"; //ONLY FOR TESTING PURPOSE - REMOVE THIS
    private int timeOut;
    private Map<String,Object> properties = new HashMap<>();

    public ApiClient(Map<String,Object> variableArgs){
        this.properties = variableArgs;
        this.baseUri = this.properties.containsKey("baseuri")?this.properties.get("baseuri").toString() : "";
        this.timeOut = this.properties.containsKey("timeout")?Integer.parseInt(this.properties.get("timeout").toString()):15;

    }

   /* public ApiClient(String baseUri,int timeOut){
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }
    */

    public ApiClient(){
        this.baseUri="";
        this.timeOut = 15;
    }

    public Map<String,Object> getProperties(){
        return this.properties;
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

    public HttpResponse<String> getRequest(String endPoint){
        try {
            String url = getUrlForEndPoint(endPoint);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().GET().uri(URI.create(url));
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public HttpResponse<String> getRequest(String endPoint, Map<String, Object> variableArgs){
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();


            String url = getUrlForEndPoint(endPoint);

            if (variableArgs.containsKey("params")) {
                params = (Map<String, String>)variableArgs.get("params");
            }

            if (variableArgs.containsKey("headers")) {
                headers = (Map<String, String> )variableArgs.get("headers");
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

    public HttpResponse<String> postRequest(String endPoint,Map<String,Object> variableArgs){
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Map<String, String> cookies = new HashMap<>();
            String url = getUrlForEndPoint(endPoint);

            if (variableArgs.containsKey("params")) {
                params = (Map<String, String>) variableArgs.get("params");
            }

            if (variableArgs.containsKey("headers")) {
                headers = (Map<String, String>) variableArgs.get("headers");
            }

            if (variableArgs.containsKey("cookies")) {
                cookies = (Map<String, String>) variableArgs.get("cookies");
            }

            if (params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url + key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            String data = "";
            if (variableArgs.containsKey("data") && !(variableArgs.get("data") instanceof String)) {
                data = new com.google.gson.Gson().toJson(variableArgs.get("data"));
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(data)).uri(URI.create(url));

            if (headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            } else {
                Map<String, String> config = (Map<String, String>) variableArgs.get("config");
                String token = config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer {0}", token));
            }
            requestBuilder.setHeader("Content-type", "application/json");

            if (cookies.size() > 0) {
                //do something
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public HttpResponse<String> patchRequest(String endPoint,Map<String,Object> variableArgs){
        return null;
    }

    public HttpResponse<String> deleteRequest(String endPoint){
        return null;
    }
}
