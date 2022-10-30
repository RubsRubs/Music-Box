package com.example.streamingaudioplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    public String title;
    public String description;
    public boolean publica;
    public ArrayList<String> songIds;

    public Playlist() {
    }

    public Playlist(String title, String description, boolean publica) {
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
