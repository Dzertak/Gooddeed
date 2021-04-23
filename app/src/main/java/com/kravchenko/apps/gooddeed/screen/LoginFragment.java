package com.kravchenko.apps.gooddeed.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentLoginBinding;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {


    private static final int RC_SIGN_IN = 100;
    private final String TAG = "TAG_DEBUG_" + getClass().getSimpleName();
    private FragmentLoginBinding binding;
    private AuthViewModel mAuthViewModel;
    private NavController navController;
    private String emailInput;
    private String passwordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //for test

        //mBinding.buttonLoginFragmentLogIn.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_mainFragment));
        //navController.navigate(R.id.action_loginFragment_to_mainFragment);

        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mAuthViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                if (firebaseUser.status.equals(Resource.Status.SUCCESS)) {
                    // Login successful
                    Toast.makeText(getContext(), "Authentication successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login Successful!");
                } else if (firebaseUser.status.equals(Resource.Status.LOADING)) {
                    Toast.makeText(getContext(), firebaseUser.message, Toast.LENGTH_SHORT).show();
                } else if (firebaseUser.status.equals(Resource.Status.ERROR)) {
                    Toast.makeText(getContext(), firebaseUser.message, Toast.LENGTH_SHORT).show();
                }
            } else {
                // Invalid password or email
                Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onLoginClick() {
        if (validateInput()) {
            return;
        }
        mAuthViewModel.loginWithEmailAndPassword(binding.tilEmailHolder.getEditText().getText().toString().trim(),
               binding.tilPasswordHolder.getEditText().getText().toString().trim());
        mAuthViewModel.setIsAuth(true);
        navController.navigate(R.id.action_loginFragment_to_mainFragment);
    }

    private boolean validateInput() {
        emailInput = binding.tilEmailHolder.getEditText().getText().toString().trim();
        passwordInput = binding.tilPasswordHolder.getEditText().getText().toString().trim();

        String emailError = InputValidator.validateEmail(emailInput);
        String passwordError = InputValidator.validatePassword(passwordInput);

        binding.tilEmailHolder.setError(emailError);
        binding.tilPasswordHolder.setError(passwordError);
        return emailError != null || passwordError != null;
    }

    public void onLoginWithGoogleClick() {
        startActivityForResult(mAuthViewModel.loginWithGoogle().getSignInIntent(), RC_SIGN_IN);
    }

    public void onLoginWithFacebookClick() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "Firebase Auth with Google: " + account.getEmail());
                mAuthViewModel.setIdToken(account.getIdToken());
            } catch (ApiException e) {
                // Google sign-in failed
                Log.w(TAG, "Google sign-in failed: " + e);
            }
        }
    }
}