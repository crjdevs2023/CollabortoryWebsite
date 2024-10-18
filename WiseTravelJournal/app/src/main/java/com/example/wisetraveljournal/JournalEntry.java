package com.example.wisetraveljournal;

import java.io.Serializable;

public class JournalEntry implements Serializable {
    private String title;
    private String date;
    private String geoTag;
    private String description;

    // Constructor
    public JournalEntry(String title, String date, String geoTag, String description) {
        this.title = title;
        this.date = date;
        this.geoTag = geoTag;
        this.description = description;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getGeoTag() { return geoTag; }
    public String getDescription() { return description; }
}
