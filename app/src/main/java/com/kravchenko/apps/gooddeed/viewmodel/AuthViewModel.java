package com.kravchenko.apps.gooddeed.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.kravchenko.apps.gooddeed.repository.AuthRepository;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.SharedPreferencesManager;

import static com.kravchenko.apps.gooddeed.util.SharedPreferencesManager.IS_AUTH_KEY;

public class AuthViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final AuthRepository mAuthRepository;
    private final SharedPreferencesManager sharedPreferencesManager;
    private final MutableLiveData<Boolean> isAuth;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        mAuthRepository = new AuthRepository(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance();
        sharedPreferencesManager.registerOnSharedPreferenceChangeListener(this);
        isAuth = new MutableLiveData<>(sharedPreferencesManager.getIsAuth());
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

    public void signOutUser() {
        mAuthRepository.signOutUser();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case IS_AUTH_KEY:
                isAuth.setValue(sharedPreferencesManager.getIsAuth());
                break;
        }
    }
    public LiveData<Boolean> getIsAuth() {
        return isAuth;
    }
    public void setIsAuth(boolean isAuth) {
        sharedPreferencesManager.putIsAuth(isAuth);
    }
}
