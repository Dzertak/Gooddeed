package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.repository.CategoryRepository;

import java.util.List;


public class FilterViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;

    public FilterViewModel() {
        this.categoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<Boolean> getIsBackPressed() {
        return categoryRepository.getIsBackPressed();
    }

    public MutableLiveData<Boolean> getIsDrawerOpen() {
        return categoryRepository.getIsDrawerOpen();
    }

    public void setIsDrawerOpen(boolean isDrawerOpen) {
        categoryRepository.setIsDrawerOpen(isDrawerOpen);
    }

    public void setIsBackPressed(boolean isBackPressed) {
        this.categoryRepository.setIsBackPressed(isBackPressed);
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryRepository.getCategoryTypesWithCategoriesLiveData();
    }

    public void setMapSelectedCategoriesLiveData(List<Category> selectedCategories, long categoryOwnerId) {
        categoryRepository.setMapSelectedCategoriesLiveData(selectedCategories, categoryOwnerId);
    }

    public void setInitiativesSelectedCategory(List<Category> selectedCategories, long categoryOwnerId) {
        categoryRepository.setInitiativesSelectedCategory(selectedCategories, categoryOwnerId);
    }

    public LiveData<List<CategoryTypeWithCategories>> getMapSelectedCategoriesLiveData() {
        return categoryRepository.getMapSelectedCategoriesLiveData();
    }

    public LiveData<List<CategoryTypeWithCategories>> getInitiativesSelectedCategoriesLiveData() {
        return categoryRepository.getInitiativesSelectedCategoriesLiveData();
    }

    public LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(long ownerId) {
        return categoryRepository.findCategoryTypesByCategoryOwnerId(ownerId);
    }

    public void initMapSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypeWithCategories) {
        categoryRepository.initMapSelectedCategoriesLiveData(categoryTypeWithCategories);
    }

    public void initInitiativesSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypesWithCategories) {
        categoryRepository.initInitiativesSelectedCategoriesLiveData(categoryTypesWithCategories);
    }
}
