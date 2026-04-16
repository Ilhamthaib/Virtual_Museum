package com.virtualmuseum.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
    tableName = "artworks",
    foreignKeys = @ForeignKey(
        entity        = CategoryEntity.class,
        parentColumns = "id",
        childColumns  = "categoryId",
        onDelete      = ForeignKey.CASCADE
    ),
    indices = @Index("categoryId")
)
public class ArtworkEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String  title;
    private String  artist;
    private int     year;
    private String  description;
    private int     categoryId;
    private String  imageUri;    // path to asset or file (nullable)
    private String  audioUri;    // nullable
    private String  model3DUri;  // nullable
    private boolean isFavorite;

    // ── Constructor ───────────────────────────────────────────────────────────
    public ArtworkEntity(String title, String artist, int year, String description,
                         int categoryId, String imageUri, String audioUri,
                         String model3DUri) {
        this.title       = title;
        this.artist      = artist;
        this.year        = year;
        this.description = description;
        this.categoryId  = categoryId;
        this.imageUri    = imageUri;
        this.audioUri    = audioUri;
        this.model3DUri  = model3DUri;
        this.isFavorite  = false;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    public String getYearLabel() {
        return year < 0 ? (-year) + " av. J.-C." : String.valueOf(year);
    }

    public boolean hasAudio()   { return audioUri   != null && !audioUri.isEmpty(); }
    public boolean has3DModel() { return model3DUri != null && !model3DUri.isEmpty(); }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int     getId()          { return id; }
    public String  getTitle()       { return title; }
    public String  getArtist()      { return artist; }
    public int     getYear()        { return year; }
    public String  getDescription() { return description; }
    public int     getCategoryId()  { return categoryId; }
    public String  getImageUri()    { return imageUri; }
    public String  getAudioUri()    { return audioUri; }
    public String  getModel3DUri()  { return model3DUri; }
    public boolean isFavorite()     { return isFavorite; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(int id)                { this.id = id; }
    public void setTitle(String v)           { this.title = v; }
    public void setArtist(String v)          { this.artist = v; }
    public void setYear(int v)               { this.year = v; }
    public void setDescription(String v)     { this.description = v; }
    public void setCategoryId(int v)         { this.categoryId = v; }
    public void setImageUri(String v)        { this.imageUri = v; }
    public void setAudioUri(String v)        { this.audioUri = v; }
    public void setModel3DUri(String v)      { this.model3DUri = v; }
    public void setFavorite(boolean v)       { this.isFavorite = v; }
}
