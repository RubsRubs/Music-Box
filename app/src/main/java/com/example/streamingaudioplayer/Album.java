package com.example.streamingaudioplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String albumTitle;
    private String genre;
    private String imgURL;
    private ArrayList<Song> songs;
    private String year;

    public Album() {
        // Default constructor required for calls to DataSnapshot.getValue(Album.class)
    }

    public Album(String albumTitle, String genre, String imgURL, ArrayList<Song> songs, String year) {
        this.albumTitle = albumTitle;
        this.genre = genre;
        this.imgURL = imgURL;
        this.songs = songs;
        this.year = year;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getGenre() {
        return genre;
    }

    public String getImgURL() {
        return imgURL;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getYear() {
        return year;
    }
}