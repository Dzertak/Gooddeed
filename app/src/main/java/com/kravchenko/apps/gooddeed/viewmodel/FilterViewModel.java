package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> selectedCategoriesLiveData;
    private final MutableLiveData<Set<Category>> allSelectedCategoriesLiveData;
    private final MutableLiveData<List<CategoryTypeWithCategories>> categoryTypesWithCategoriesLiveData;

    public FilterViewModel() {
        this.selectedCategoriesLiveData = new MutableLiveData<>();
        this.categoryTypesWithCategoriesLiveData = new MutableLiveData<>();
        this.allSelectedCategoriesLiveData = new MutableLiveData<>(new HashSet<>());
    }

    public LiveData<List<Category>> getSelectedCategoriesLiveData() {
        return selectedCategoriesLiveData;
    }

    public void setSelectedCategories(List<Category> categories) {
        selectedCategoriesLiveData.setValue(categories);
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryTypesWithCategoriesLiveData;
    }

    public void setCategoryTypesWithCategories(List<CategoryTypeWithCategories> categoryTypesWithCategories) {
        categoryTypesWithCategoriesLiveData.setValue(categoryTypesWithCategories);
    }

    public LiveData<Set<Category>> getAllSelectedCategoriesLiveData() {
        return allSelectedCategoriesLiveData;
    }

    public void addSelectedCategory(Category category) {
        Set<Category> value = allSelectedCategoriesLiveData.getValue();
        value.add(category);
        allSelectedCategoriesLiveData.setValue(value);
    }
    public void removeSelectedCategory(Category category) {
        Set<Category> value = allSelectedCategoriesLiveData.getValue();
        value.remove(category);
        allSelectedCategoriesLiveData.setValue(value);
    }
}
