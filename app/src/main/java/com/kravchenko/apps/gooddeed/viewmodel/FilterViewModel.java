package com.kravchenko.apps.gooddeed.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.repository.CategoryRepository;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FilterViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;

    public FilterViewModel() {
        this.categoryRepository = new CategoryRepository();
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryRepository.getCategoryTypesWithCategoriesLiveData();
    }

    public void setInitiativesSelectedCategory(Category category, long categoryOwnerId) {
        categoryRepository.setInitiativesSelectedCategory(category, categoryOwnerId);
    }

    public MutableLiveData<List<CategoryTypeWithCategories>> getInitiativesSelectedCategoriesLiveData() {
        return categoryRepository.getInitiativesSelectedCategoriesLiveData();
    }
    public MutableLiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategory() {
      return categoryRepository.getCategoryTypesWithCategory();
    }
}
