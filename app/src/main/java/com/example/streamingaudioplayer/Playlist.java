package com.example.streamingaudioplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    public String title;
    public String description;
    public boolean publica;

    public Playlist() {
    }

    public Playlist(String title, String description, boolean publica) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
