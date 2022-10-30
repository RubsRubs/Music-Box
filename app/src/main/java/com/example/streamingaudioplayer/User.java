package com.example.streamingaudioplayer;

import java.io.Serializable;

public class User implements Serializable {

    public String email;
    public String user;

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
