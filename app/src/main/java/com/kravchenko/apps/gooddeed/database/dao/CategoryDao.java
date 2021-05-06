package com.kravchenko.apps.gooddeed.database.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypesWithCategories;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class CategoryDao {
    @Insert(onConflict = REPLACE)
    public abstract long insertCategoryType(CategoryType categoryType);

    @Insert(onConflict = REPLACE)
    public abstract long insertCategory(Category category);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void insertCategoryTypeWithCategories(CategoryTypesWithCategories categoryTypesWithCategories) {
        CategoryType categoryType = categoryTypesWithCategories.getCategoryType();
        List<Category> categories = categoryTypesWithCategories.getCategories();
        insertCategoryType(categoryType);
        categories.forEach(category -> {
            category.setCategoryOwnerId(categoryType.getCategoryTypeId());
            insertCategory(category);
        });
    }

    @Query("SELECT * FROM category")
    public abstract LiveData<List<Category>> findCategories();

    @Query("SELECT * FROM category_type")
    public abstract LiveData<List<CategoryType>> findCategoryTypes();

    @Query("SELECT * FROM category WHERE categoryOwnerId = :ownerId")
    public abstract LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(String ownerId);

    @Query("SELECT * FROM category_type WHERE categoryTypeId = :id")
    public abstract LiveData<CategoryType> findCategoryTypeById(String id);

    @Transaction
    @Query("SELECT * FROM category_type ")
    public abstract LiveData<List<CategoryTypesWithCategories>> findCategoryTypesWithCategory();

    @Transaction
    @Query("SELECT * FROM category_type WHERE categoryTypeId = :id")
    public abstract LiveData<List<CategoryTypesWithCategories>> findCategoryTypesWithCategoryByCategoryTypeId(String id);
}
