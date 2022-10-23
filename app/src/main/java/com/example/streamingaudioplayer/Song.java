package com.example.streamingaudioplayer;

import java.io.Serializable;

public class Song implements Serializable {
    private String duration;
    private String fileURL;
    private String songTitle;
    private double idNumber; //si utilizo un String el objeto me devuelve un valor null y no entiendo bien porqué---

    public Song() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Song(String duration, String fileURL, String songTitle, double idNumber) {
        this.duration = duration;
        this.fileURL = fileURL;
        this.songTitle = songTitle;
        this.idNumber = idNumber;
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

    public double getIdNumber() {
        return idNumber;
    }
}
