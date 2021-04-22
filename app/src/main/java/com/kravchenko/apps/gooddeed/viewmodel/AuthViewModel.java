package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private final AuthRepository mAuthRepository;

    public AuthViewModel(){
        mAuthRepository = new AuthRepository();
    }
}
