package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.databinding.FragmentRadiusFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;

public class RadiusFilterFragment extends BaseFragment {
    private FragmentRadiusFilterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadiusFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}