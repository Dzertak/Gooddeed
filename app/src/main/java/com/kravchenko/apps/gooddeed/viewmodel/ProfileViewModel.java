package com.kravchenko.apps.gooddeed.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.repository.CategoryRepository;
import com.kravchenko.apps.gooddeed.repository.ProfileRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository mRepository;
    private final CategoryRepository categoryRepository;

    public ProfileViewModel() {
        categoryRepository = CategoryRepository.getInstance();
        mRepository = new ProfileRepository();
    }

    public LiveData<Resource<FirestoreUser>> getUser() {
        return mRepository.getUser();
    }

    public LiveData<List<CategoryTypeWithCategories>> getSubscriptionsSelectedCategoriesLiveData() {
        return categoryRepository.getSubscriptionsSelectedCategoriesLiveData();
    }

    public void updateUser(String firstName,
                           String lastName,
                           Uri imageUri,
                           String description,
                           List<Long> categories) {
        mRepository.updateUser(firstName, lastName, imageUri, description, categories);
    }

    public LiveData<List<Category>> getSubscriptionsByIds(List<Long> categoryIds) {
        return categoryRepository.getCategoriesByIds(categoryIds);
    }

    public void setSelectedSubscriptionsIds(List<Category> categoryIds) {
        categoryRepository.setSelectedSubscriptionsIds(categoryIds);
    }


    public void setSubscriptionsSelectedCategoriesLiveData(List<Category> categories, long categoryOwnerId) {
        categoryRepository.setSubscriptionsSelectedCategoriesLiveData(categories, categoryOwnerId);
    }

    public LiveData<List<CategoryTypeWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return categoryRepository.getCategoryTypesWithCategoriesLiveData();
    }

    public void initProfileSelectedCategoriesLiveData(List<CategoryTypeWithCategories> categoryTypesWithCategories, List<Category> categories) {
        categoryRepository.initProfileSelectedCategoriesLiveData(categoryTypesWithCategories, categories);
    }

    public void removeSubscription(Category category) {
        categoryRepository.removeCategory(category);
    }
}
