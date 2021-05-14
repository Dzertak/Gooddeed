package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.repository.AuthRepository;
import com.kravchenko.apps.gooddeed.repository.InitiativeRepository;
import com.kravchenko.apps.gooddeed.util.Resource;


import java.util.List;

public class AuthViewModel extends ViewModel {

    private final AuthRepository mAuthRepository;
    private final InitiativeRepository mInitiativeRepository;

    public AuthViewModel() {
        super();
        mAuthRepository = new AuthRepository();
        mInitiativeRepository = new InitiativeRepository();
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

    public LiveData<Resource<List<Initiative>>> getInitiativesFromFirestore() {
        return mInitiativeRepository.getInitiativesFromFirestoreLiveData();
    }
}
