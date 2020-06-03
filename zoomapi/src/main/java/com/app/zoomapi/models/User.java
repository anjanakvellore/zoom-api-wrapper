package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

public class User {
    @PrimaryKey
    private String zoomClientId;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    public User(String zoomClientId, String userId, String firstName, String lastName, String email) {
        this.zoomClientId = zoomClientId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(){

    }

    public String getZoomClientId(){
        return zoomClientId;
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
