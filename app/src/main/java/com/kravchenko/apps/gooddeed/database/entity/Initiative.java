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
    private String location;
    private String lat;
    private String lng;
    private long timestamp;
    private String type;

    public Initiative(){

    }

    public Initiative(String title, String description, String imgUri) {
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
    }

    public void setInitiativeId(long initiativeId) {
        this.initiativeId = initiativeId;
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

    public void setInitiativeUserId(String initiativeUserId) {
        this.initiativeUserId = initiativeUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
