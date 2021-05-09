package com.kravchenko.apps.gooddeed.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.database.CategoryDatabase;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final String CATEGORY_TYPES_COLLECTION_PATH = "category-types";
    private final String CATEGORIES_COLLECTION_PATH = "categories";

    private MutableLiveData<List<CategoryTypeWithCategories>> mapSelectedCategoriesLiveData;
    private MutableLiveData<List<CategoryTypeWithCategories>> initiativesSelectedCategoriesLiveData;
    private MutableLiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategories = new MutableLiveData<>();


    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // private

    public CategoryRepository() {
        categoryDao = CategoryDatabase.getInstance().categoryDao();
        mapSelectedCategoriesLiveData = getCategoryTypesWithCategory();
        initiativesSelectedCategoriesLiveData = new MutableLiveData<>();
        initInitiativesSelectedCategoriesLiveData();
    }

    private MutableLiveData<List<CategoryTypeWithCategories>> initInitiativesSelectedCategoriesLiveData() {
        databaseWriteExecutor.execute(() -> {
            List<CategoryTypeWithCategories> initiativesSelectedCategories = new ArrayList<>();
            List<CategoryType> categoryTypes = categoryDao.findCategoryTypes();
            categoryTypes.forEach(categoryType -> {
                CategoryTypeWithCategories categoryTypeWithCategories = new CategoryTypeWithCategories();
                categoryTypeWithCategories.setCategoryType(categoryType);
                categoryTypeWithCategories.setCategories(new ArrayList<>());
                initiativesSelectedCategories.add(categoryTypeWithCategories);
            });
            this.initiativesSelectedCategoriesLiveData.postValue(initiativesSelectedCategories);
        });

        return initiativesSelectedCategoriesLiveData;
    }

    public LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(long ownerId) {
        return categoryDao.findCategoryTypesByCategoryOwnerId(ownerId);
    }


    public MutableLiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategory() {
        if (categoryTypesWithCategories == null) {
            databaseWriteExecutor.execute(() ->
                    categoryTypesWithCategories.postValue(categoryDao.findCategoryTypesWithCategoryList()));
        }
        return categoryTypesWithCategories;
    }


    public MutableLiveData<List<CategoryTypeWithCategories>> getInitiativesSelectedCategoriesLiveData() {
        return initiativesSelectedCategoriesLiveData;
    }

    public void setInitiativesSelectedCategory(Category category, long categoryOwnerId) {
        initiativesSelectedCategoriesLiveData.getValue().forEach(categoryTypesWithCategories -> {
            if (categoryOwnerId
                    == (categoryTypesWithCategories.getCategoryType().getCategoryTypeId())) {
                List<Category> selectedCategories = new ArrayList<>();
                selectedCategories.add(category);

                categoryTypesWithCategories.setCategories(selectedCategories);
            }
        });
        initiativesSelectedCategoriesLiveData.setValue(initiativesSelectedCategoriesLiveData.getValue());
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryDao.findCategoryTypesWithCategory();
    }
}
