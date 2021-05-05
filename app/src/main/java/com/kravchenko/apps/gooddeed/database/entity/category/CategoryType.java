package com.kravchenko.apps.gooddeed.database.entity.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kravchenko.apps.gooddeed.util.Utils;

@Entity(tableName = "category_type")
public class CategoryType {
    @PrimaryKey
    @NonNull
    private String categoryTypeId;
    private long title;
    private long description;

    public CategoryType(long title, long description) {
        this.title = title;
        this.description = description;
    }

    public CategoryType() {
    }

    @NonNull
    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(@NonNull String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public long getTitle() {
        return title;
    }

    public void setTitle(long title) {
        this.title = title;
    }

    public long getDescription() {
        return description;
    }

    public void setDescription(long description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return categoryTypeId +" "+ Utils.getString(title) +" "+ Utils.getString(description);
    }
}
