package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.database.AppDatabase;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CategoryRepository {
    private final CategoryDao categoryDao;
    private static CategoryRepository instance;

    private final MutableLiveData<List<CategoryTypeWithCategories>> mapSelectedCategoriesLiveData;
    private final MutableLiveData<List<CategoryTypeWithCategories>> initiativesSelectedCategoriesLiveData;
    private final LiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategories;
    private final MutableLiveData<Boolean> isBackPressed;
    private final MutableLiveData<Boolean> isNavDrawerOpen;

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CategoryRepository getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }

    private CategoryRepository() {
        this.categoryDao = AppDatabase.getInstance().categoryDao();
        this.isBackPressed = new MutableLiveData<>(false);
        this.isNavDrawerOpen = new MutableLiveData<>(false);
        this.categoryTypesWithCategories = categoryDao.getCategoryTypesWithCategory();
        this.mapSelectedCategoriesLiveData = new MutableLiveData<>(null);
        this.initiativesSelectedCategoriesLiveData = new MutableLiveData<>(null);
    }

    public LiveData<Boolean> getIsBackPressed() {
        return isBackPressed;
    }

    public MutableLiveData<Boolean> getIsNavDrawerOpen() {
        return isNavDrawerOpen;
    }

    public void setIsNavDrawerOpen(boolean isNavDrawerOpen) {
        this.isNavDrawerOpen.setValue(isNavDrawerOpen);
    }

    public void setIsBackPressed(boolean isBackPressed) {
        this.isBackPressed.setValue(isBackPressed);
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryDao.getCategoryTypesWithCategory();
    }

    public LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(long ownerId) {
        return categoryDao.getCategoryTypesByCategoryOwnerId(ownerId);
    }

    public LiveData<List<CategoryTypeWithCategories>> getMapSelectedCategoriesLiveData() {
        return mapSelectedCategoriesLiveData;
    }

    public LiveData<List<CategoryTypeWithCategories>> getInitiativesSelectedCategoriesLiveData() {
        return initiativesSelectedCategoriesLiveData;
    }

    public void setMapSelectedCategoriesLiveData(List<Category> selectedCategories, long categoryOwnerId) {
        for (CategoryTypeWithCategories categoryTypeWithCategories
                : mapSelectedCategoriesLiveData.getValue()) {
            if (categoryOwnerId
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypeWithCategories.setCategories(selectedCategories);
            }
        }
        mapSelectedCategoriesLiveData.setValue(mapSelectedCategoriesLiveData.getValue());
    }

    public void setInitiativesSelectedCategory(List<Category> selectedCategories, long categoryOwnerId) {
        for (CategoryTypeWithCategories categoryTypeWithCategories
                : initiativesSelectedCategoriesLiveData.getValue()) {
            if (categoryOwnerId
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypeWithCategories.setCategories(selectedCategories);
            } else {
                categoryTypeWithCategories.setCategories(new ArrayList<>());
            }
        }
        initiativesSelectedCategoriesLiveData.setValue(initiativesSelectedCategoriesLiveData.getValue());
    }

    public void initMapSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypeWithCategories) {
        mapSelectedCategoriesLiveData.setValue(categoryTypeWithCategories);
    }

    public void initInitiativesSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypesWithCategories) {

        for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
            categoryTypeWithCategories.setCategories(new ArrayList<>());
        }
        this.initiativesSelectedCategoriesLiveData.setValue(categoryTypesWithCategories);
    }

    public LiveData<Category> getCategoryById(long id) {
        return categoryDao.getCategoryById(id);
    }

    ;
}

