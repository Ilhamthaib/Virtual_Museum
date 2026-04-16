package com.virtualmuseum.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "categories")
public class CategoryEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private String iconEmoji;
    private String colorHex;

    public CategoryEntity(String name, String description,
                          String iconEmoji, String colorHex) {
        this.name        = name;
        this.description = description;
        this.iconEmoji   = iconEmoji;
        this.colorHex    = colorHex;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public int    getId()            { return id; }
    public void   setId(int id)      { this.id = id; }
    public String getName()          { return name; }
    public void   setName(String v)  { this.name = v; }
    public String getDescription()   { return description; }
    public void   setDescription(String v) { this.description = v; }
    public String getIconEmoji()     { return iconEmoji; }
    public void   setIconEmoji(String v)   { this.iconEmoji = v; }
    public String getColorHex()      { return colorHex; }
    public void   setColorHex(String v)    { this.colorHex = v; }
}
