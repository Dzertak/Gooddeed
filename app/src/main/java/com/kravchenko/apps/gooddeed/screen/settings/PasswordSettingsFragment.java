package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;


import com.kravchenko.apps.gooddeed.databinding.FragmentPasswordSettingsBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;


public class PasswordSettingsFragment extends BaseFragment {
    private FragmentPasswordSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        binding.tilPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilPasswordHolder));
        binding.tilConfirmPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilConfirmPasswordHolder));

        binding.btnApplyChanges.setOnClickListener(v ->
                applyChanges()
        );
    }

    private void applyChanges() {
        if (validateInput()) {
            return;
        }
        // TODO implements password changes
    }
    //TODO
    //To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
    private boolean validateInput() {
        String passwordInput = binding.tilPasswordHolder.getEditText().getText().toString().trim();
        String confirmPasswordInput = binding.tilConfirmPasswordHolder.getEditText().getText().toString().trim();

        String passwordError = InputValidator.validatePassword(passwordInput);
        String confirmPasswordError = InputValidator.validateConfirmPassword(confirmPasswordInput, passwordInput);

        binding.tilPasswordHolder.setError(passwordError);
        binding.tilConfirmPasswordHolder.setError(confirmPasswordError);

        return passwordError != null || confirmPasswordError != null;
    }
}