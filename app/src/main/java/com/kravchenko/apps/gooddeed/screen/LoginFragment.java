package com.kravchenko.apps.gooddeed.screen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentLoginBinding;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends BaseFragment {


    private static final int RC_SIGN_IN = 100;
    private final String TAG = "TAG_DEBUG_" + getClass().getSimpleName();
    private FragmentLoginBinding binding;
    private AuthViewModel mAuthViewModel;
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tilEmailHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilEmailHolder));
        binding.tilPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilPasswordHolder));

        //for test
        //mBinding.buttonLoginFragmentLogIn.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_mainFragment));
        //navController.navigate(R.id.action_loginFragment_to_mainFragment);

        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mAuthViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                if (firebaseUser.status.equals(Resource.Status.SUCCESS)) {
                    // Login successful
                    //Toast.makeText(getContext(), "Authentication successful", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Login Successful!");
                    // adjustBackStack();
                    getNavController().navigate(R.id.action_loginFragment_to_mainFragment);
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
        getNavController().navigate(R.id.action_loginFragment_to_mainFragment);
    }

    //TODO
    //To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
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

    private void adjustBackStack() {
        NavGraph navGraph = getNavController().getGraph();
        navGraph.setStartDestination(R.id.mainFragment);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        if (!(getNavController().getCurrentDestination().getId() == R.id.mainFragment)) {
            getNavController().navigate(R.id.action_loginFragment_to_mainFragment, null, navOptions);
        }
    }
}