package com.app.zoomapi.clients;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class ApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private Map<String,String> config = new HashMap<>();
    //ToDo: making it static so that same value will be shared across all clients
    private static String baseUri = null;
    private static Integer timeOut = null;


    public ApiClient(String baseUri,Integer timeOut,Map<String,String> config){
        this.config = config;
        this.baseUri = baseUri;
        this.timeOut = timeOut;
    }

    public ApiClient(Map<String,String> config){
        this.config = config;
       /* if(this.getBaseUri()==null)
            this.baseUri = baseUri;
        if(this.getTimeOut() == null)
            this.timeOut = timeOut;*/
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

   /* public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }*/

    public String getBaseUri() {
        return baseUri;
    }

   /* public void setBaseUri(String baseUri) {
        if(baseUri!=null && !baseUri.isEmpty() && baseUri.endsWith("/")){
            baseUri = baseUri.substring(0,baseUri.length()-1);
        }
        this.baseUri = baseUri;
    }*/

    public String getUrlForEndPoint(String endpoint){
        if(!endpoint.startsWith("/")){
            endpoint = "/"+endpoint;
        }
        if(endpoint.endsWith("/")){
            endpoint = endpoint.substring(0,endpoint.length()-1);
        }
        return this.baseUri + endpoint;
    }

   /* public HttpResponse<String> getRequest(String endPoint){
        try {
            //ToDo: call the second method to minimise the duplication
            String url = getUrlForEndPoint(endPoint);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().GET().uri(URI.create(url));
            String token = (String)getProperties("token");
            requestBuilder.setHeader("Authorization", String.format("Bearer %s", token));
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }*/

   /* public HttpResponse<String> getRequest(String endPoint, Map<String,String> ...variableArgs){
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();


            String url = getUrlForEndPoint(endPoint);

            for(Map<String,String> arg:variableArgs){

            }

//            if (variableArgs.containsKey("params")) {
//                params = (Map<String, String>)variableArgs.get("params");
//            }
//
//            if (variableArgs.containsKey("headers")) {
//                headers = (Map<String, String> )variableArgs.get("headers");
//            }

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
    }*/

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

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().GET().uri(URI.create(url));

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

    public HttpResponse<String> postRequest(String endPoint,Map<String,String> params,
                                            Map<String,String> headers, Map<String,String> data,
                                            Map<String,String> cookies){
        String dataStr = "";
        if(data!=null && data.size()>0){
            dataStr = new com.google.gson.Gson().toJson(data);
        }
        return postRequest(endPoint,params,headers, dataStr,cookies);

    }


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

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(data)).uri(URI.create(url));

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

    public HttpResponse<String> patchRequest(String endPoint,Map<String,Object> variableArgs){
        return null;
    }

    public HttpResponse<String> deleteRequest(String endPoint){
        return null;
    }
}
