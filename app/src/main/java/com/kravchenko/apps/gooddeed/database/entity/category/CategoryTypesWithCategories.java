package com.kravchenko.apps.gooddeed.database.entity.category;



import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryTypesWithCategories {
    @Embedded
    private CategoryType categoryType;
    @Relation(
            parentColumn = "categoryTypeId",
            entityColumn = "categoryOwnerId"
    )
    private List<Category> categories;

    public CategoryTypesWithCategories(CategoryType categoryType, List<Category> categories) {
        this.categoryType = categoryType;
        this.categories = categories;
    }

    public CategoryTypesWithCategories() {
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
