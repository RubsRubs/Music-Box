package com.example.streamingaudioplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    public String title;
    public String description;
    public boolean publica;
    public String userId;

    public Playlist() {
    }

    public Playlist(String title, String description, boolean publica, String userId) {
        this.title = title;
        this.description = description;
        this.publica = publica;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPublica() {
        return publica;
    }
}
