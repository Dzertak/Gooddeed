package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.database.CategoryDatabase;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final String CATEGORY_TYPES_COLLECTION_PATH = "category-types";
    private final String CATEGORIES_COLLECTION_PATH = "categories";

    private MutableLiveData<List<CategoryTypeWithCategories>> mapSelectedCategoriesLiveData;
    private MutableLiveData<List<CategoryTypeWithCategories>> initiativesSelectedCategoriesLiveData;


    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // private

    public CategoryRepository() {
        categoryDao = CategoryDatabase.getInstance().categoryDao();
        mapSelectedCategoriesLiveData = findCategoryTypesWithCategoryList();
        initiativesSelectedCategoriesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<CategoryType>> getCategoryTypes() {
        return categoryDao.findCategoryTypes();
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryDao.findCategoryTypesWithCategory();
    }

    public LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(long ownerId) {
        return categoryDao.findCategoryTypesByCategoryOwnerId(ownerId);
    }

    private MutableLiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategories = new MutableLiveData<>();

    public MutableLiveData<List<CategoryTypeWithCategories>> findCategoryTypesWithCategoryList() {
        databaseWriteExecutor.execute(() ->
                categoryTypesWithCategories.postValue(categoryDao.findCategoryTypesWithCategoryList()));
        return categoryTypesWithCategories;
    }
}
