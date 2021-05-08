package com.kravchenko.apps.gooddeed.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypesWithCategories;
import com.kravchenko.apps.gooddeed.repository.AuthRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

import java.util.List;

public class AuthViewModel extends ViewModel {

    private final AuthRepository mAuthRepository;
    private MutableLiveData<List<CategoryTypesWithCategories>> selectedCategoriesLiveData;

    public AuthViewModel() {
        super();
        mAuthRepository = new AuthRepository();
        selectedCategoriesLiveData = mAuthRepository.findCategoryTypesWithCategoryList();
    }

    public void loginWithEmailAndPassword(String email, String password) {
        mAuthRepository.loginWithEmailAndPassword(email, password);
    }

    public GoogleSignInClient loginWithGoogle() {
        return mAuthRepository.loginWithGoogle();
    }

    public void loginWithFacebook() {
        //TODO
    }

    public void setIdToken(String tokenId) {
        mAuthRepository.firebaseAuthWithGoogle(tokenId);
    }

    public LiveData<Resource<FirebaseUser>> getUser() {
        return mAuthRepository.getUser();
    }

    public void changeEmail(String newEmail) {
        mAuthRepository.changeEmail(newEmail);
    }

    public void changePassword(String newPassword) {
        mAuthRepository.changePassword(newPassword);
    }

    public LiveData<Resource<Object>> getActionMarker() {
        return mAuthRepository.getActionMarker();
    }

    public void loginWithPassword(String password) {
        mAuthRepository.loginWithPassword(password);
    }

    //Categories
    public LiveData<List<CategoryType>> getCategoryTypes() {
        return mAuthRepository.getCategoryTypes();
    }

    public LiveData<List<CategoryTypesWithCategories>> getSelectedCategoriesLiveData() {
        return selectedCategoriesLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setSelectedCategories(List<Category> selectedCategories, String categoryOwnerId) {
        selectedCategoriesLiveData.getValue().forEach(categoryTypesWithCategories -> {
            if (categoryOwnerId
                    .equals(categoryTypesWithCategories.getCategoryType().getCategoryTypeId())) {
                categoryTypesWithCategories.setCategories(selectedCategories);
            }
        });
        selectedCategoriesLiveData.setValue(selectedCategoriesLiveData.getValue());
    }

    public LiveData<List<Category>> findCategoryTypesByCategoryOwnerId(String ownerId) {
        return mAuthRepository.findCategoryTypesByCategoryOwnerId(ownerId);
    }

    public LiveData<List<CategoryTypesWithCategories>> getCategoryTypesWithCategoriesLiveData() {
        return mAuthRepository.getCategoryTypesWithCategoriesLiveData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchCategoryTypeWithCategoriesFromFirestore() {
        mAuthRepository.fetchCategoryTypeWithCategoriesFromFirestore();
    }
}
