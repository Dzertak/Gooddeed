package com.kravchenko.apps.gooddeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.repository.AuthRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository mAuthRepository;


    public AuthViewModel(@NonNull Application application) {
        super(application);
        mAuthRepository = new AuthRepository(application);
    }

    public void loginWithEmailAndPassword(String email, String password) {
        mAuthRepository.loginWithEmailAndPassword(email, password);
    }

    public GoogleSignInClient loginWithGoogle() {
        return mAuthRepository.loginWithGoogle();
    }

    public void loginWithFacebook() {

    }

    public void setIdToken(String tokenId) {
        mAuthRepository.firebaseAuthWithGoogle(tokenId);
    }

    public LiveData<Resource<FirebaseUser>> getUser() {
        return mAuthRepository.getUser();
    }

    public void signOutUser() {
        mAuthRepository.signOutUser();
    }
}
