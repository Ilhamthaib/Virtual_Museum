package com.virtualmuseum.models;

import java.io.Serializable;

public class Category implements Serializable {

    private int    id;
    private String name;
    private String description;
    private String iconEmoji;
    private String colorHex;

    public Category(int id, String name, String description,
                    String iconEmoji, String colorHex) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.iconEmoji   = iconEmoji;
        this.colorHex    = colorHex;
    }

    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public String getIconEmoji()   { return iconEmoji; }
    public String getColorHex()    { return colorHex; }
}
