package com.example.streamingaudioplayer;

public class User {

    private String email;
    private String user;

    public User() {
    }

    public User(String email, String user) {
        this.email = email;
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public String getUser() {
        return user;
    }
}
