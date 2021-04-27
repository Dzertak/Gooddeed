package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.databinding.FragmentEmailSettingBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;


public class EmailSettingFragment extends BaseFragment {

    private FragmentEmailSettingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmailSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        binding.tilEmailHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilEmailHolder));
        binding.btnApplyChanges.setOnClickListener(v ->
                applyChanges()
        );
    }

    private void applyChanges() {
        if (validateInput()) {
            return;
        }
        // TODO implements e-mail changes
    }

    //TODO
    //To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
    private boolean validateInput() {
        String passwordInput = binding.tilEmailHolder.getEditText().getText().toString().trim();
        String emailError = InputValidator.validateEmail(passwordInput);
        binding.tilEmailHolder.setError(emailError);
        return emailError != null;
    }
}