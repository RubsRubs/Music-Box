package com.example.streamingaudioplayer;

public class Favourite {

    private String songId;

    public Favourite() {
        // Default constructor required for calls to DataSnapshot.getValue(Favourite.class)
    }

    public Favourite(String songId) {
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
