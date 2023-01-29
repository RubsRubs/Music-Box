package com.example.streamingaudioplayer;

import java.io.Serializable;

public class Song implements Serializable {
    private String duration;
    private String fileURL;
    private String songTitle;
    private double songId; //si utilizo un String el objeto me devuelve un valor null y no entiendo bien porqué---

    public Song() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Song(String duration, String fileURL, String songTitle, double songId) {
        this.duration = duration;
        this.fileURL = fileURL;
        this.songTitle = songTitle;
        this.songId = songId;
    }

    public String getDuration() {
        return duration;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public double getSongId() {
        return songId;
    }
}
