package com.example.androidmp3.models;

import android.media.Image;

public class Music {
    public Music(long id, String titre, String path, String artist, long artistId, String album, long albumId, int duration, long date) {
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.artistId = artistId;
        this.albumId = albumId;
        this.titre = titre;
        this.path = path;
        this.duration = duration;
        this.date = date;
    }

    private long id;
    private long artistId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    private long albumId;
    private String album;
    private String artist;
    private String titre;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    private String path;
    private int duration;
    private long date;
}
