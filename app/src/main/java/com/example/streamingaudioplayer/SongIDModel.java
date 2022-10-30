package com.example.streamingaudioplayer;

import java.io.Serializable;

public class SongIDModel implements Serializable {

    public String songId;

    public SongIDModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Favourite.class)
    }

    public SongIDModel(String songId) {
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
