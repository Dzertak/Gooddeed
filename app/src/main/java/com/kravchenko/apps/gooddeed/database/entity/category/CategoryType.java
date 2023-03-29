package com.kravchenko.apps.gooddeed.database.entity.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.kravchenko.apps.gooddeed.util.Utils;

@Entity(tableName = "category_type")
public class CategoryType {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long categoryTypeId;
    private String title;
    private String description;
    @Ignore
    public CategoryType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CategoryType() {
    }

    @NonNull
    public long getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(@NonNull long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
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

}
