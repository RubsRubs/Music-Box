package com.example.streamingaudioplayer;

import java.io.Serializable;

public class User implements Serializable {

    public String user;
    public String email;

    public User() {
    }

    public User(String user, String email) {
        this.user = user;
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

}
