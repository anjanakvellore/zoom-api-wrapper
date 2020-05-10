package com.app.zoomapi.models;

public class Member {
    private String memberId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public Member(String memberId,String email,String firstName,String lastName, String role){
        this.memberId = memberId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getMemberId(){
        return memberId;
    }

    public String getEmail(){
        return email;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getRole() {return role;}
}
