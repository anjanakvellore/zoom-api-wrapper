package com.app.zoomapi.models;

public class Member {
    private String memberId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String channel;

    public Member(String memberId,String email,String firstName,String lastName, String role, String channel){
        this.memberId = memberId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.channel = channel;
    }

    /**
     * gets Id of member
     * @return member Id
     */
    public String getMemberId(){
        return memberId;
    }

    /**
     * gets email of member
     * @return email of member
     */
    public String getEmail(){
        return email;
    }

    /**
     * gets the first name of member
     * @return first name of member
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * gets the last name of member
     * @return last name of member
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * gets the role of member (owner, admin, member)
     * @return role of member
     */
    public String getRole() {return role;}

    /**
     * gets the channel of member in picture is associated with
     * @return channel of member
     */
    public String getChannel() {
        return channel;
    }
}
