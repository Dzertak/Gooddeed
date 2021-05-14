package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.repository.CategoryRepository;
import com.kravchenko.apps.gooddeed.repository.InitiativeRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

import java.util.List;

public class InitiativeViewModel extends ViewModel {

    private final MutableLiveData<Initiative> initiativeMutableLiveData;
    private final InitiativeRepository mInitiativeRepository;
    private final CategoryRepository mCategoryRepository;

    public InitiativeViewModel() {
        super();
        initiativeMutableLiveData = new MutableLiveData<>();
        mInitiativeRepository = new InitiativeRepository();
        mCategoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<Initiative> getInitiative() {
        return initiativeMutableLiveData;
    }

    public LiveData<Resource<Initiative>> getSavingInitiative() {
        return mInitiativeRepository.savingInitiative();
    }

    public void updateInitiative(Initiative initiative) {
        initiativeMutableLiveData.setValue(initiative);
    }

    public void saveInitiative(Initiative initiative) {
        mInitiativeRepository.saveInitiative(initiative);
    }

    public LiveData<List<CategoryTypeWithCategories>> getSelectedCategory() {
        return mCategoryRepository.getInitiativesSelectedCategoriesLiveData();
    }

    public LiveData<Category> getCategoryById(long id) {
        return mCategoryRepository.getCategoryById(id);
    }

    public String getAuthUserId() {
        FirebaseUser user = mInitiativeRepository.getAuthUser();
        if (user != null)
            return user.getUid();
        return "";
    }
}
