package com.virtualmuseum.models;

import java.io.Serializable;

public class Artwork implements Serializable {

    private int    id;
    private String title;
    private String artist;
    private int    year;
    private String description;
    private int    categoryId;
    private int    imageResId;
    private int    audioResId;   // 0 = no audio
    private boolean has3DModel;
    private boolean isFavorite;

    public Artwork(int id, String title, String artist, int year,
                   String description, int categoryId, int imageResId,
                   int audioResId, boolean has3DModel) {
        this.id          = id;
        this.title       = title;
        this.artist      = artist;
        this.year        = year;
        this.description = description;
        this.categoryId  = categoryId;
        this.imageResId  = imageResId;
        this.audioResId  = audioResId;
        this.has3DModel  = has3DModel;
        this.isFavorite  = false;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public int     getId()          { return id; }
    public String  getTitle()       { return title; }
    public String  getArtist()      { return artist; }
    public int     getYear()        { return year; }
    public String  getDescription() { return description; }
    public int     getCategoryId()  { return categoryId; }
    public int     getImageResId()  { return imageResId; }
    public int     getAudioResId()  { return audioResId; }
    public boolean has3DModel()     { return has3DModel; }
    public boolean isFavorite()     { return isFavorite; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

    /** Human-readable year string (handles B.C. years stored as negatives) */
    public String getYearLabel() {
        return year < 0 ? (-year) + " av. J.-C." : String.valueOf(year);
    }
}
