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
import com.kravchenko.apps.gooddeed.databinding.FragmentEmailSettingBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.InputValidator;
import com.kravchenko.apps.gooddeed.util.TextErrorRemover;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;


public class EmailSettingFragment extends BaseFragment {

    private FragmentEmailSettingBinding binding;
    private AuthViewModel authViewModel;
    private String emailInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmailSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        binding.tilEmailHolder.getEditText().addTextChangedListener(new TextErrorRemover(binding.tilEmailHolder));
        binding.btnApplyChanges.setOnClickListener(v ->
                applyChanges()
        );

        authViewModel.getActionMarker().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case SUCCESS:
                    hideProgressDialog();
                    Toast.makeText(getContext(), R.string.email_changed_successfully, Toast.LENGTH_SHORT).show();
                    getNavController().navigate(R.id.action_emailSettingFragment_to_settingsFragment);
                    break;
                case ERROR:
                    hideProgressDialog();
                    Toast.makeText(getContext(), resource.getMessage(getContext()), Toast.LENGTH_LONG).show();
                    break;
                case LOADING:
                    showProgressDialog();
                    break;
            }
        });
    }

    private void applyChanges() {
        if (validateInput()) {
            return;
        }
        authViewModel.changeEmail(emailInput);
    }

    //TODO
    //To enable password validation, you need to uncomment the check in the validatePassword()
    // method in InputValidator
    private boolean validateInput() {
        emailInput = binding.tilEmailHolder.getEditText().getText().toString().trim();
        String emailError = InputValidator.validateEmail(emailInput);
        binding.tilEmailHolder.setError(emailError);
        return emailError != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void clear() {

    }
}