package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.repository.ProfileRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository mRepository;

    public ProfileViewModel() {
        mRepository = new ProfileRepository();
    }

    public LiveData<Resource<FirestoreUser>> getUser() {
        return mRepository.getUser();
    }

    public void updateUser(String firstName, String lastName, String imageUrl, String email, String description) {
        mRepository.updateUser(firstName, lastName, imageUrl, email, description);
    }

}
