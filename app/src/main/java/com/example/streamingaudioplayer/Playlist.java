package com.example.streamingaudioplayer;

import java.io.Serializable;

public class Playlist implements Serializable {

    private String title;
    private String description;
    // private ArrayList<String> songIds;

    public Playlist() {
    }

    public Playlist(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
