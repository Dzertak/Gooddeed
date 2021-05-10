package com.kravchenko.apps.gooddeed.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.repository.AuthRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AuthViewModel extends ViewModel {

    private final AuthRepository mAuthRepository;

    public AuthViewModel() {
        super();
        mAuthRepository = new AuthRepository();

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

}
