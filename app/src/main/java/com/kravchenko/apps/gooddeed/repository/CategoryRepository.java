package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.database.AppDatabase;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
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
    private final MutableLiveData<List<CategoryTypeWithCategories>> subscriptionsSelectedCategoriesLiveData;
    private final MutableLiveData<List<Category>> selectedSubscriptionsLiveData;
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

    public LiveData<List<Category>> getCategoriesByIds(List<Long> categoryIds) {
        return categoryDao.getCategoriesByIds(categoryIds);
    }

    private CategoryRepository() {
        this.categoryDao = AppDatabase.getInstance().categoryDao();
        this.isBackPressed = new MutableLiveData<>(false);
        this.isNavDrawerOpen = new MutableLiveData<>(false);
        this.categoryTypesWithCategories = categoryDao.getCategoryTypesWithCategory();
        this.mapSelectedCategoriesLiveData = new MutableLiveData<>(null);
        this.initiativesSelectedCategoriesLiveData = new MutableLiveData<>(null);
        this.subscriptionsSelectedCategoriesLiveData = new MutableLiveData<>(null);
        this.selectedSubscriptionsLiveData = new MutableLiveData<>(null);
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

    public void setSubscriptionsSelectedCategoriesLiveData(List<Category> selectedCategories, long categoryOwnerId) {
        for (CategoryTypeWithCategories categoryTypeWithCategories
                : subscriptionsSelectedCategoriesLiveData.getValue()) {
            if (categoryOwnerId
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypeWithCategories.setCategories(selectedCategories);
            }
        }

        subscriptionsSelectedCategoriesLiveData.setValue(subscriptionsSelectedCategoriesLiveData.getValue());
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

    public void initProfileSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypesWithCategories,
                                                      List<Category> selectedCategories) {

        for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
            categoryTypeWithCategories.setCategories(new ArrayList<>());
            List<Category> categories = new ArrayList<>();
            long categoryTypeId = categoryTypeWithCategories.getCategoryType().getCategoryTypeId();
            for (Category selectedCategory : selectedCategories) {
                if (categoryTypeId == selectedCategory.getCategoryOwnerId()) {
                    categories.add(selectedCategory);
                }
            }
            categoryTypeWithCategories.setCategories(categories);
        }

        subscriptionsSelectedCategoriesLiveData.setValue(categoryTypesWithCategories);

    }

    public LiveData<List<CategoryTypeWithCategories>> getSubscriptionsSelectedCategoriesLiveData() {
        return subscriptionsSelectedCategoriesLiveData;
    }

    public LiveData<Category> getCategoryById(long id) {
        return categoryDao.getCategoryById(id);
    }

    public void setSelectedSubscriptionsIds(List<Category> categoryIds) {
        selectedSubscriptionsLiveData.setValue(categoryIds);
    }

    public LiveData<List<Category>> getSelectedSubscriptionsIdsLiveData() {
        return selectedSubscriptionsLiveData;
    }


    public void removeCategory(Category category) {
        for (CategoryTypeWithCategories categoryTypeWithCategories
                : subscriptionsSelectedCategoriesLiveData.getValue()) {

            if (category.getCategoryOwnerId()
                    == (categoryTypeWithCategories.getCategoryType().getCategoryTypeId())) {
                List<Category> categories = categoryTypeWithCategories.getCategories();
                categories.remove(category);
                categoryTypeWithCategories.setCategories(categories);
            }
        }
        subscriptionsSelectedCategoriesLiveData.setValue(subscriptionsSelectedCategoriesLiveData.getValue());
    }

    public LiveData<CategoryType> getCategoryTypeById(long categoryTypeId){
        return categoryDao.findCategoryTypeById(categoryTypeId);
    }
}

