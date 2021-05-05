package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryTypeFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryTypeRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;


public class CategoryTypeFilterFragment extends BaseFragment {
    private FragmentCategoryTypeFilterBinding binding;
    private CategoryTypeRecyclerViewAdapter adapter;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryTypeFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initRecyclerView();
        authViewModel.getCategoryTypes()
                .observe(getViewLifecycleOwner(), categoryTypes -> {
                    adapter.setCategoryTypes(categoryTypes);
                });
    }

    private void initRecyclerView() {
        adapter = new CategoryTypeRecyclerViewAdapter(getContext());
        adapter.setOnItemClickListener(categoryType -> {
            NavDirections action = CategoryTypeFilterFragmentDirections.actionCategoryTypeFilterFragmentToCategoryFilterFragment3(categoryType.getCategoryTypeId());
            getNavController().navigate(action);
        });

        binding.recyclerViewCategoryTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategoryTypes.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}