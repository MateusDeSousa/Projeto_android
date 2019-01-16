package com.example.vincius.myapplication;

public class User {

    private final String uid;
    private final String username;
    private final String profileUrl;


    public User(String uid, String username, String profileUrl) {
        this.uid = uid;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
