package com.example.streamingaudioplayer;

import java.io.Serializable;

public class AudioFileModel implements Serializable {
    public String id;
    public String album;
    public String artist;
    public String duration;
    public String imgURL;
    public String fileURL;
    public String songTitle;

    public AudioFileModel() {
    }

    public AudioFileModel(String id, String album, String artist, String imgURL, String fileURL, String songTitle, String duration) {
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.imgURL = imgURL;
        this.fileURL = fileURL;
        this.songTitle = songTitle;
        this.duration = duration;
    }

    public String getId() {
        return id;
}

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getDuration() {
        return duration;
    }
}