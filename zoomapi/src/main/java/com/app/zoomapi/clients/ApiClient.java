package com.app.zoomapi.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class ApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private Map<String,String> config = new HashMap<>();
    // making it static so that same value will be shared across all instances
    private static String baseUri = null;
    private static Integer timeOut = null;


    public ApiClient(String baseUri,Integer timeOut,Map<String,String> config){
        this.config = config;
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }

    public ApiClient(Map<String,String> config){
        this.config = config;
    }
    public ApiClient(String baseUri,int timeOut){
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }


    public Map<String,String> getConfig(){
        return this.config;
    }

    public Integer getTimeOut() {
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

    public HttpResponse<String> getRequest(String endPoint, Map<String, String> params,Map<String,String> headers){
        try {

            String url = getUrlForEndPoint(endPoint);

            if (params!=null && params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url+  key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().GET().timeout(Duration.ofSeconds(timeOut)).uri(URI.create(url));

            if (headers!=null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            }
            else{
                String token = this.config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
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

    //This request supports data as map and converts it into string; calls the post method that takes data as string
    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, Map<String,Object> data,
                                            Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            JSONObject json = new JSONObject(data);
            dataStr = json.toString();
        }
        return postRequest(endPoint,params,headers, dataStr,cookies);

    }


    //This post request supports data as string
    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, String data,
                                            Map<String,String> cookies){
        try {
            String url = getUrlForEndPoint(endPoint);

            if(data == null){
                data = "";
            }

            if (params!=null && params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url + key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(data)).timeout(Duration.ofSeconds(timeOut)).uri(URI.create(url));

            if (headers!=null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            } else {
                String token = this.config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
            }
            requestBuilder.setHeader("Content-type", "application/json");

            if (cookies!=null && cookies.size() > 0) {
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

    public HttpResponse<String> deleteRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return postRequest(endPoint,params,headers, dataStr,cookies);


    }

    public HttpResponse<String> deleteRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, String data,
                                              Map<String,String> cookies){

        try{
            String url = getUrlForEndPoint(endPoint);

            if(data == null){
                data = "";
            }

            if (params!=null && params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url + key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().method("DELETE",HttpRequest.BodyPublishers.ofString(data)).timeout(Duration.ofSeconds(timeOut)).uri(URI.create(url));

            if (headers!=null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            } else {
                String token = this.config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
            }
            requestBuilder.setHeader("Content-type", "application/json");

            if (cookies!=null && cookies.size() > 0) {
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

    public HttpResponse<String> putRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return putRequest(endPoint,params,headers, dataStr,cookies);


    }

    public HttpResponse<String> putRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, String data,
                                              Map<String,String> cookies){

        try{
            String url = getUrlForEndPoint(endPoint);

            if(data == null){
                data = "";
            }

            if (params!=null && params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url + key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().method("PUT",HttpRequest.BodyPublishers.ofString(data)).timeout(Duration.ofSeconds(timeOut)).uri(URI.create(url));

            if (headers!=null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            } else {
                String token = this.config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
            }
            requestBuilder.setHeader("Content-type", "application/json");

            if (cookies!=null && cookies.size() > 0) {
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

    public HttpResponse<String> patchRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return patchRequest(endPoint,params,headers, dataStr,cookies);


    }

    public HttpResponse<String> patchRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, String data,
                                              Map<String,String> cookies){

        try{
            String url = getUrlForEndPoint(endPoint);

            if(data == null){
                data = "";
            }

            if (params!=null && params.size() > 0) {
                url = url + "?";
                for (String key : params.keySet()) {
                    url = url + key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length() - 1);
            }

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().method("PATCH",HttpRequest.BodyPublishers.ofString(data)).timeout(Duration.ofSeconds(timeOut)).uri(URI.create(url));

            if (headers!=null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    requestBuilder.setHeader(key, headers.get(key));
                }
            } else {
                String token = this.config.get("token");
                requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
            }
            requestBuilder.setHeader("Content-type", "application/json");

            if (cookies!=null && cookies.size() > 0) {
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


}
