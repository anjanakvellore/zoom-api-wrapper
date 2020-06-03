package com.app.zoomapi.models;

import com.app.zoomapi.repo.annotations.PrimaryKey;

/**
 * Models the MemberMaster table in the database.
 */
public class MemberMaster {
    @PrimaryKey
    private int channelId;
    @PrimaryKey
    private String zoomMemberId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String timeStamp;

    public MemberMaster(int channelId, String zoomMemberId,String firstName, String lastName, String email, String role,String timeStamp) {
        this.channelId = channelId;
        this.zoomMemberId = zoomMemberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.timeStamp = timeStamp;
    }

    public MemberMaster(String zoomMemberId,String firstName, String lastName, String email, String role,String timeStamp){
        this.zoomMemberId = zoomMemberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.timeStamp = timeStamp;
    }

    public MemberMaster(){

    }

    public int getChannelId() {
        return channelId;
    }

    public String getZoomMemberId() {
        return zoomMemberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getTimeStamp(){
        return timeStamp;
    }
}
