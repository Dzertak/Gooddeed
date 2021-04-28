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
import com.kravchenko.apps.gooddeed.databinding.FragmentConfirmAuthorizationBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;


public class ConfirmAuthorizationFragment extends BaseFragment {
    private FragmentConfirmAuthorizationBinding binding;
    private String args;
    private AuthViewModel authViewModel;
    private String passwordInput;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding = FragmentConfirmAuthorizationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        if (getArguments() != null) {
            args = ConfirmAuthorizationFragmentArgs.fromBundle(getArguments()).getChooseDestinations();
        }
        switch (args) {
            case SettingsFragment.NAV_ARG_EMAIL:
                binding.tvExplanation.setText(R.string.change_email_explanation);
                break;
            case SettingsFragment.NAV_ARG_PASSWORD:
                binding.tvExplanation.setText(R.string.change_password_explanation);
                break;
        }

        binding.btnConfirm.setOnClickListener(v ->
                onLoginClick()
        );

        authViewModel.getActionMarker().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case SUCCESS:
                    hideNetworkProgressDialog();
                    switch (args) {
                        case SettingsFragment.NAV_ARG_EMAIL:
                            getNavController().navigate(R.id.action_confirmAuthorizationFragment_to_emailSettingFragment);
                            break;
                        case SettingsFragment.NAV_ARG_PASSWORD:
                            getNavController().navigate(R.id.action_confirmAuthorizationFragment_to_passwordSettingsFragment);
                            break;
                    }
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

    public void onLoginClick() {
        if (validateInput()) {
            return;
        }
        authViewModel.loginWithPassword(passwordInput);
    }

    private boolean validateInput() {
        passwordInput = binding.tilPasswordHolder.getEditText().getText().toString().trim();
        String passwordError = InputValidator.validatePassword(passwordInput);
        binding.tilPasswordHolder.setError(passwordError);
        return passwordError != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}