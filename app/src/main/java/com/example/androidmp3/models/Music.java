package com.example.androidmp3.models;

import android.media.Image;

public class Music {
    public Music(String album, String artiste, String titre, String path, int duration, Image cover) {
        this.album = album;
        this.artiste = artiste;
        this.titre = titre;
        this.path = path;
        this.duration = duration;
        this.cover = cover;
    }

    private String album;
    private String artiste;
    private String titre;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
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

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    private String path;
    private int duration;
    private Image cover;
}
