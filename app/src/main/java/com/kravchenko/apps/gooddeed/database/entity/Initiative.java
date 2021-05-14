package com.kravchenko.apps.gooddeed.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "initiative")
public class Initiative {
    @PrimaryKey
    @NonNull
    private String initiativeId;
    private String initiativeUserId;
    private String title;
    private String description;
    private String imgUri;
    private String location;
    private String lat;
    private String lng;
    private long timestamp;
    private String type;
    private long categoryId;
    private long executors;

    public Initiative() {
        initiativeId = "";
    }

    @Ignore
    public Initiative(String title, String description, String imgUri) {
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        initiativeId = "";
    }

    @Ignore
    public Initiative(String initiativeUserId, String title, String description, String imgUri,
                      String location, String lat, String lng, long timestamp, String type, long categoryId) {
        this.initiativeUserId = initiativeUserId;
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.type = type;
        this.categoryId = categoryId;
        initiativeId = "";
    }

    @Ignore
    public Initiative(String initiativeUserId, String title, String description, String imgUri,
                      String location, String lat, String lng, long timestamp, String type, long categoryId,
                      long executors) {
        this.initiativeUserId = initiativeUserId;
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.type = type;
        this.categoryId = categoryId;
        this.executors = executors;
        initiativeId = "";
    }

    @NonNull
    public String getInitiativeId() {
        return initiativeId;
    }

    public void setInitiativeId(@NonNull String initiativeId) {
        this.initiativeId = initiativeId;
    }

    public String getInitiativeUserId() {
        return initiativeUserId;
    }

    public void setInitiativeUserId(String initiativeUserId) {
        this.initiativeUserId = initiativeUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUri() {
        return imgUri;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getExecutors() {
        return executors;
    }

    public void setExecutors(long executors) {
        this.executors = executors;
    }

    @NonNull
    @Override
    public String toString() {
        return "Initiative{" +
                "initiativeId='" + initiativeId + '\'' +
                ", initiativeUserId='" + initiativeUserId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imgUri='" + imgUri + '\'' +
                ", location='" + location + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", category='" + categoryId + '\'' +
                ", executors=" + executors +
                '}';
    }
}
