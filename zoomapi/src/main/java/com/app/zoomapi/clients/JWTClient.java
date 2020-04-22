package com.app.zoomapi.clients;

import com.app.zoomapi.components.*;
import com.app.zoomapi.utilities.Utility;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple class to support JWT implementation
 */
public class JWTClient extends ZoomClient {
    private final String APIBASEURI= "https://api.zoom.us/v2";
    private final int version = 2;
    private Map<String,String> config;
    private UserComponent userComponent;
    private MeetingComponent meetingComponent;
    private ReportComponent reportComponent;
    private WebinarComponent webinarComponent;
    private RecordingComponent recordingComponent;

    public JWTClient(String apiKey, String apiSecret, String dataType, Integer timeOut) {
        super(apiKey, apiSecret, dataType!=null ? dataType:"json",timeOut!=null ? timeOut : 15);

        config = new HashMap<>();
        config.put("apiKey",apiKey);
        config.put("apiSecret",apiSecret);
        config.put("dataType", dataType);
        config.put("version",String.valueOf(version));
        config.put("token", Utility.generateJwt(config.get("apiKey"),config.get("apiSecret")));
    }

    @Override
    public String refreshToken() {
        return Utility.generateJwt(config.get("apiKey"),config.get("apiSecret"));
    }

    @Override
    public String getApiKey(){
        return config.get("apiKey");
    }

    private void setApiKey(String apiKey) {
        config.put("apiKey",apiKey);
        refreshToken();
    }

    @Override
    public String getApiSecret(){
        return config.get("apiSecret");
    }

    private void setApiSecret(String apiSecret) {
        config.put("apiSecret",apiSecret);
        refreshToken();
    }

    @Override
    public MeetingComponent getMeetingComponent(){
        return meetingComponent;
    }

    @Override
    public ReportComponent getReportComponent() {
        return reportComponent;
    }

    @Override
    public UserComponent getUserComponent(){
        return userComponent;
    }

    @Override
    public WebinarComponent getWebinarComponent(){
        return webinarComponent;
    }

    @Override
    public RecordingComponent getRecordingComponent() {
        return recordingComponent;
    }
}