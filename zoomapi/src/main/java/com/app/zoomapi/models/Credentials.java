package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

public class Credentials {
    @PrimaryKey
    private String zoomClientId;
    private String OAuthToken;
    private String refreshToken;
    private String loginTime;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    public Credentials(String zoomClientId, String OAuthToken,String refreshToken, String loginTime,
                    String userId, String firstName, String lastName, String email) {
        this.zoomClientId = zoomClientId;
        this.OAuthToken = OAuthToken;
        this.refreshToken = refreshToken;
        this.loginTime = loginTime;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public Credentials(){
    }

    public String getZoomClientId() {
        return zoomClientId;
    }

    public String getOAuthToken() {
        return OAuthToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }


    public String getLoginTime() {
        return loginTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){ return email;}
}
