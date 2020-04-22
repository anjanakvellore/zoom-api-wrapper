package com.app.zoomapi.clients;

import com.app.zoomapi.components.*;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple wrapper for Zoom.us REST API Java client
 */
public class ZoomClient extends ApiClient{
    private final String APIBASEURI= "https://api.zoom.us/v2";
    private Map<String,String> config;
    private UserComponent userComponent;
    private MeetingComponent meetingComponent;
    private ReportComponent reportComponent;
    private WebinarComponent webinarComponent;
    private RecordingComponent recordingComponent;

    /**
     * Create a new Zoom API client
     * @param apiKey The Zoom.us API key
     * @param apiSecret The Zoom.us API secret
     * @param dataType The expected return data type. Either 'json' or 'xml'
     * @param timeOut The time out to use for API requests
     */
    public ZoomClient(String apiKey, String apiSecret, String dataType, Integer timeOut){

        super("https://api.zoom.us/v2",timeOut!=null ? timeOut:15);

        /**
         * Set up the config details
         */
        config = new HashMap<>();
        config.put("apiKey",apiKey);
        config.put("apiSecret",apiSecret);
        config.put("dataType", dataType!=null ? dataType : "json");

    }

    //TODO: use as abstract? pass in python
    public String refreshToken() throws OAuthProblemException, OAuthSystemException, IOException{
        return null;
    }

    /**
     * get the Zoom.us API key
     */
    public String getApiKey(){
        return config.get("apiKey");
    }

    /**
     * Set the Zoom.us API Key
     * @param apiKey : The Zoom.us API key
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     * @throws IOException
     */
    private void setApiKey(String apiKey) throws OAuthSystemException, OAuthProblemException, IOException {
        config.put("apiKey",apiKey);
        refreshToken();
    }

    /**
     * get the Zoom.us API secret
     */
    public String getApiSecret(){
        return config.get("apiSecret");
    }

    /**
     * set the Zoom.us API secret
     * @param apiSecret : The Zoom.us API secret
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     * @throws IOException
     */
    private void setApiSecret(String apiSecret) throws OAuthSystemException, OAuthProblemException, IOException {
        config.put("apiSecret",apiSecret);
        refreshToken();
    }

    /**
     * get the meeting component
     */
    public MeetingComponent getMeetingComponent(){
        return meetingComponent;
    }

    /**
     * get the report component
     */
    public ReportComponent getReportComponent() {
        return reportComponent;
    }

    /**
     * get the user component
     */
    public UserComponent getUserComponent(){
        return userComponent;
    }

    /**
     * get the webinar component
     */
    public WebinarComponent getWebinarComponent(){
        return webinarComponent;
    }

    /**
     * get the recording component
     */
    public RecordingComponent getRecordingComponent() {
        return recordingComponent;
    }
}
