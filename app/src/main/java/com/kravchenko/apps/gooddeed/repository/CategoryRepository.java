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

    private MutableLiveData<List<CategoryTypeWithCategories>> mapSelectedCategoriesLiveData;
    private MutableLiveData<List<CategoryTypeWithCategories>> initiativesSelectedCategoriesLiveData;
    private LiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategories;


    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public CategoryRepository() {
        this.categoryDao = AppDatabase.getInstance().categoryDao();

        this.categoryTypesWithCategories = categoryDao.getCategoryTypesWithCategory();
        this.mapSelectedCategoriesLiveData = new MutableLiveData<>(null);
        this.initiativesSelectedCategoriesLiveData = new MutableLiveData<>(null);
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

        for (CategoryTypeWithCategories categoryTypeWithCategories : mapSelectedCategoriesLiveData.getValue()) {
            if (categoryOwnerId
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypeWithCategories.setCategories(selectedCategories);
            }
        }

//        mapSelectedCategoriesLiveData.getValue().forEach(categoryTypesWithCategories -> {
//            if (categoryOwnerId
//                    == (categoryTypesWithCategories.getCategoryType().getCategoryTypeId())) {
//                categoryTypesWithCategories.setCategories(selectedCategories);
//            }
//        });
        mapSelectedCategoriesLiveData.setValue(mapSelectedCategoriesLiveData.getValue());
    }

    public void setInitiativesSelectedCategory(List<Category> selectedCategories, long categoryOwnerId) {

        for (CategoryTypeWithCategories categoryTypeWithCategories : initiativesSelectedCategoriesLiveData.getValue()) {
            if (categoryOwnerId
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypeWithCategories.setCategories(selectedCategories);
            }
        }


//        initiativesSelectedCategoriesLiveData.getValue().forEach(categoryTypesWithCategories -> {
//            categoryTypesWithCategories.setCategories(new ArrayList<>());
//            if (categoryOwnerId
//                    == (categoryTypesWithCategories.getCategoryType().getCategoryTypeId())) {
//                categoryTypesWithCategories.setCategories(selectedCategories);
//            }
//        });
        initiativesSelectedCategoriesLiveData.setValue(initiativesSelectedCategoriesLiveData.getValue());
    }

    public void initMapSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypeWithCategories) {
        mapSelectedCategoriesLiveData.setValue(categoryTypeWithCategories);
    }

    public void initInitiativesSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypesWithCategories) {

        for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
            categoryTypeWithCategories.setCategories(new ArrayList<>());
        }


//        categoryTypesWithCategories.forEach(categoryTypeWithCategories -> {
//            categoryTypeWithCategories.setCategories(new ArrayList<>());
//        });
        this.initiativesSelectedCategoriesLiveData.setValue(categoryTypesWithCategories);
    }
}


//
//
//
//
//    private MutableLiveData<List<CategoryTypeWithCategories>> initInitiativesSelectedCategoriesLiveData() {
//        databaseWriteExecutor.execute(() -> {
//            List<CategoryTypeWithCategories> initiativesSelectedCategories = new ArrayList<>();
//            List<CategoryType> categoryTypes = categoryDao.findCategoryTypes();
//            categoryTypes.forEach(categoryType -> {
//                CategoryTypeWithCategories categoryTypeWithCategories = new CategoryTypeWithCategories();
//                categoryTypeWithCategories.setCategoryType(categoryType);
//                categoryTypeWithCategories.setCategories(new ArrayList<>());
//                initiativesSelectedCategories.add(categoryTypeWithCategories);
//            });
//            this.initiativesSelectedCategoriesLiveData.postValue(initiativesSelectedCategories);
//        });
//
//        return initiativesSelectedCategoriesLiveData;
//    }
//
//
//
//
//    public MutableLiveData<List<CategoryTypeWithCategories>> initCategoryTypesWithCategory() {
//        MutableLiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategories = new MutableLiveData<>();
//        databaseWriteExecutor.execute(() ->
//                categoryTypesWithCategories.postValue(categoryDao.findCategoryTypesWithCategoryList()));
//        return categoryTypesWithCategories;
//    }
//
//
//
//
//
//
//
