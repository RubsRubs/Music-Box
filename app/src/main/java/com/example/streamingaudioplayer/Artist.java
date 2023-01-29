package com.example.streamingaudioplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {

    private String name;
    private ArrayList<Album> albums;
    private String artistImageURL;
    private String artistInfo;

    public Artist() {
        // Default constructor required for calls to DataSnapshot.getValue(Artist.class)
    }

    public Artist(String artist, ArrayList<Album> albums, String artistImageURL, String artistInfo) {
        this.name = artist;
        this.albums = albums;
        this.artistImageURL = artistImageURL;
        this.artistInfo = artistInfo;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public String getArtistImageURL() {
        return artistImageURL;
    }

    public String getArtistInfo() {
        return artistInfo;
    }
}
