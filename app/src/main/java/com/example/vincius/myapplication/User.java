package com.example.vincius.myapplication;

public class User {

    private final String uid;
    private final String username;
    private final String profileUrl;


    public User(String uuid, String username, String profileUrl) {
        this.uid = uuid;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public String getUuid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
