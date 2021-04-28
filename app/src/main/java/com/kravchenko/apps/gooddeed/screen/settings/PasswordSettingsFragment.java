package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentPasswordSettingsBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;


public class PasswordSettingsFragment extends BaseFragment {
    private FragmentPasswordSettingsBinding binding;
    private String newPasswordInput;
    private AuthViewModel mAuthViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        binding.tilPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilPasswordHolder));
        binding.tilConfirmPasswordHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilConfirmPasswordHolder));

        binding.btnApplyChanges.setOnClickListener(v ->
                applyChanges()
        );

        mAuthViewModel.getActionMarker().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case SUCCESS:
                    hideNetworkProgressDialog();
                    Toast.makeText(getContext(), R.string.password_changed_successfully, Toast.LENGTH_SHORT).show();
                    getNavController().navigate(R.id.action_passwordSettingsFragment_to_settingsFragment);
                    break;
                case ERROR:
                    hideNetworkProgressDialog();
                    Toast.makeText(getContext(), resource.getMessage(getContext()), Toast.LENGTH_LONG).show();
                    break;
                case LOADING:
                    showNetworkProgressDialog();
                    break;
            }
        });
    }

    private void applyChanges() {
        if (validateInput()) {
            return;
        }
        mAuthViewModel.changePassword(newPasswordInput);
    }

    //TODO
    // To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
    private boolean validateInput() {
        newPasswordInput = binding.tilPasswordHolder.getEditText().getText().toString().trim();
        String confirmPasswordInput = binding.tilConfirmPasswordHolder.getEditText().getText().toString().trim();

        String passwordError = InputValidator.validatePassword(newPasswordInput);
        String confirmPasswordError = InputValidator.validateConfirmPassword(confirmPasswordInput, newPasswordInput);

        binding.tilPasswordHolder.setError(passwordError);
        binding.tilConfirmPasswordHolder.setError(confirmPasswordError);

        return passwordError != null || confirmPasswordError != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}