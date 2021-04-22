package com.kravchenko.apps.gooddeed.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Initiative {
    @PrimaryKey(autoGenerate = true)
    private long initiativeId;
    @Ignore
    private String initiativeUserId;

    private String title;
    private String description;
    private String imgUri;

    public Initiative(String title, String description, String imgUri) {
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
    }

    public long getInitiativeId() {
        return initiativeId;
    }

    public String getInitiativeUserId() {
        return initiativeUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUri() {
        return imgUri;
    }
}
