package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;

import java.util.List;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> selectedCategoriesLiveData;

    public FilterViewModel() {
        this.selectedCategoriesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Category>> getSelectedCategoriesLiveData() {
        return selectedCategoriesLiveData;
    }

    public void setSelectedCategories(List<Category> categories) {
        selectedCategoriesLiveData.setValue(categories);
    }
}
