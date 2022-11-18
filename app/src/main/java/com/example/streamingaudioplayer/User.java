package com.example.streamingaudioplayer;

import java.io.Serializable;

public class User implements Serializable {

    public String user;
    public String email;
    public String profileImgURL;

    public User() {
    }

    public User(String user, String email, String profileImgURL) {
        this.user = user;
        this.email = email;
        this.profileImgURL = profileImgURL;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImgURL() {
        return this.profileImgURL;
    }
}
