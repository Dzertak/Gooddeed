package com.kravchenko.apps.gooddeed.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.DialogManager;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentLoginBinding;
import com.kravchenko.apps.gooddeed.util.dialog.ProgressDialogFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import static android.app.Activity.RESULT_OK;
import static com.kravchenko.apps.gooddeed.repository.AuthRepository.FLAG_NEW_LOGIN;

public class LoginFragment extends BaseFragment {

    public static final String SIGNED_OUT_FLAG = "com.kravchenko.apps.gooddeed.IS_SIGNED_OUT";
    private static final int RC_SIGN_IN = 100;
    private boolean isSignedOut = false;
    private FragmentLoginBinding binding;
    private AuthViewModel mAuthViewModel;


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
        binding.tilEmailHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilEmailHolder));
        binding.tilPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilPasswordHolder));

        if (getArguments() != null) {
            isSignedOut = getArguments().getBoolean(SIGNED_OUT_FLAG);
        }
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mAuthViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                if (firebaseUser.status.equals(Resource.Status.SUCCESS)) {
                    DialogManager.hideDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                    if (!isSignedOut) {
                        if (firebaseUser.message != null && firebaseUser.message.equals(FLAG_NEW_LOGIN)) {
                            mAuthViewModel.getInitiativesFromFirestore().observe(getViewLifecycleOwner(), listResource -> {
                                if (listResource.status.equals(Resource.Status.SUCCESS)) {
                                    getNavController().navigate(R.id.action_loginFragment_to_mainFragment);
                                } else if (listResource.status.equals(Resource.Status.LOADING)) {
                                    // loading data from Firestore to local database
                                }
                            });
                        } else {
                            getNavController().navigate(R.id.action_loginFragment_to_mainFragment);
                        }
                    }
                } else if (firebaseUser.status.equals(Resource.Status.LOADING)) {
                    DialogManager.showDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                } else if (firebaseUser.status.equals(Resource.Status.ERROR)) {
                    DialogManager.hideDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
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

    private void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void onLoginClick() {
        if (validateInput()) {
            return;
        }
        mAuthViewModel.loginWithEmailAndPassword(binding.tilEmailHolder.getEditText().getText().toString().trim(),
                binding.tilPasswordHolder.getEditText().getText().toString().trim());
        isSignedOut = false;
    }

    //TODO
    //To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
    private boolean validateInput() {
        String emailInput = binding.tilEmailHolder.getEditText().getText().toString().trim();
        String passwordInput = binding.tilPasswordHolder.getEditText().getText().toString().trim();

        String emailError = InputValidator.validateEmail(emailInput);
        String passwordError = InputValidator.validatePassword(passwordInput);

        binding.tilEmailHolder.setError(emailError);
        binding.tilPasswordHolder.setError(passwordError);
        return emailError != null || passwordError != null;
    }

    public void onLoginWithGoogleClick() {
        startActivityForResult(mAuthViewModel.loginWithGoogle().getSignInIntent(), RC_SIGN_IN);
        isSignedOut = false;
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
                mAuthViewModel.setIdToken(account.getIdToken());
            } catch (ApiException e) {
                // Google sign-in failed
            }
        }
    }

    @Override
    public void clear() {

    }
}