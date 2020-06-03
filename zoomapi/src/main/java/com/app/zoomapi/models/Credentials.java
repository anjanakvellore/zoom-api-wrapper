package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

public class Credentials {
    @PrimaryKey
    private String zoomClientId;
    private String OAuthToken;
    private String loginTime;

    public Credentials(String zoomClientId, String OAuthToken,String loginTime) {
        this.zoomClientId = zoomClientId;
        this.OAuthToken = OAuthToken;
        this.loginTime = loginTime;
    }

    public Credentials(){
    }

    public String getZoomClientId() {
        return zoomClientId;
    }

    public String getOAuthToken() {
        return OAuthToken;
    }

    public String getLoginTime() {
        return loginTime;
    }

}
