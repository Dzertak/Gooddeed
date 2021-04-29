package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.databinding.FragmentSynchronizationSettingsBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;

public class SynchronizationSettingsFragment extends BaseFragment {
    private FragmentSynchronizationSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSynchronizationSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}