package com.kravchenko.apps.gooddeed.database.entity.category;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private long categoryId;
    @Ignore
    private String firebaseCategoryId;

    private String description;
    private String title;
    private String imgUrl;

    public Category(String description, String title, String imgUrl) {
        this.description = description;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getFirebaseCategoryId() {
        return firebaseCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
