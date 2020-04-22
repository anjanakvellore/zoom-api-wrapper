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

/*
Simple wrapper for REST API components
 */
public class ApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private Map<String,String> config = new HashMap<>();
    private static String baseUri = null;
    private static Integer timeOut = null;

    /**
     * Set up a new API client
     * @param baseUri The baseUri to the api
     * @param timeOut The timeOut to use for requests
     * @param config The config details
     */
    public ApiClient(String baseUri,Integer timeOut,Map<String,String> config){
        this.config = config;
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }

    /**
     * Set up a new API client
     * @param config The config details
     */
    public ApiClient(Map<String,String> config){
        this.config = config;
    }

    /**
     * Set up a new API client
     * @param baseUri The baseUri to api
     * @param timeOut The timeOut to use for requests
     */
    public ApiClient(String baseUri,int timeOut){
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }

    /**
     * Get the config details
     * @return config details
     */
    public Map<String,String> getConfig(){
        return this.config;
    }

    /**
     * Get the timeOut value
     * @return timeOut value
     */
    public Integer getTimeOut() {
        return this.timeOut;
    }

    /**
     * Sets the timeOut value
     * @param timeOut The timeOut to use for requests
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Get the baseUri to api
     * @return baseUri
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * Set the default baseUri
     * @param baseUri
     */
    public void setBaseUri(String baseUri) {
        if(baseUri!=null && !baseUri.isEmpty() && baseUri.endsWith("/")){
            baseUri = baseUri.substring(0,baseUri.length()-1);
        }
        this.baseUri = baseUri;
    }


    /**
     * Get the URL for the given end point
     * @param endpoint The endpoint
     * @return The full URL for the endpoint
     */
    public String getUrlForEndPoint(String endpoint){
        if(!endpoint.startsWith("/")){
            endpoint = "/"+endpoint;
        }
        if(endpoint.endsWith("/")){
            endpoint = endpoint.substring(0,endpoint.length()-1);
        }
        return this.baseUri + endpoint;
    }


    /**
     * Helper function for GET requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @return The Response object for this request
     */
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

    /**
     * Helper function for POST requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as hashmap to include with the POST
     * @param cookies request cookies
     * @return The Response object for this request
     */
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

    /**
     * Helper function for POST requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as JSON string to include with the POST
     * @param cookies request cookies
     * @return The Response object for this request
     */
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

    /**
     * Helper function for DELETE requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as hashmap to include with the DELETE
     * @param cookies request cookies
     * @return The Response object for this request
     */
    public HttpResponse<String> deleteRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return postRequest(endPoint,params,headers, dataStr,cookies);


    }

    /**
     * Helper function for DELETE requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as JSON string to include with the DELETE
     * @param cookies request cookies
     * @return The Response object for this request
     */
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

    /**
     * Helper function for PUT requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as hashmap to include with the PUT
     * @param cookies request cookies
     * @return The Response object for this request
     */
    public HttpResponse<String> putRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return putRequest(endPoint,params,headers, dataStr,cookies);


    }


    /**
     * Helper function for PUT requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as JSON string to include with the PUT
     * @param cookies request cookies
     * @return The Response object for this request
     */
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
    /**
     * Helper function for PATCH requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as hashmap to include with the PATCH
     * @param cookies request cookies
     * @return The Response object for this request
     */
    public HttpResponse<String> patchRequest(String endPoint,Map<String,String> params,
                                              Map<String,String> headers, Map<String,Object> data,
                                              Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return patchRequest(endPoint,params,headers, dataStr,cookies);


    }

    /**
     * Helper function for PATCH requests
     * @param endPoint The endpoint
     * @param params The URL parameters
     * @param headers request headers
     * @param data The data as JSON string to include with the PATCH
     * @param cookies request cookies
     * @return The Response object for this request
     */
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
