package com.kravchenko.apps.gooddeed.database.entity.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "category")
public class Category {
    @NonNull
    @PrimaryKey
    private String categoryId;

    private String categoryOwnerId;
    private long title;
    private long description;

    public Category(long title, long description) {
        this.title = title;
        this.description = description;
    }

    public Category() {
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryOwnerId() {
        return categoryOwnerId;
    }

    public void setCategoryOwnerId(String categoryOwnerId) {
        this.categoryOwnerId = categoryOwnerId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return title == category.title &&
                description == category.description &&
                categoryId.equals(category.categoryId) &&
                categoryOwnerId.equals(category.categoryOwnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryOwnerId, title, description);
    }
}
