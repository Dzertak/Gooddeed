package com.kravchenko.apps.gooddeed.database.entity.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "category")
public class Category {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long categoryId;

    private long categoryOwnerId;
    private String title;
    private String description;
    @Ignore
    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Category() {
    }

    @NonNull
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryOwnerId() {
        return categoryOwnerId;
    }

    public void setCategoryOwnerId(long categoryOwnerId) {
        this.categoryOwnerId = categoryOwnerId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return categoryId == category.categoryId &&
                categoryOwnerId == category.categoryOwnerId &&
                title.equals(category.title) &&
                description.equals(category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryOwnerId, title, description);
    }
}
