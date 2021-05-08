package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.HashMap;
import java.util.Map;


public class CategoryTypeFilterFragment extends BaseFragment {
    private FragmentCategoryTypeFilterBinding binding;
    private CategoryTypeRecyclerViewAdapter adapter;
    private AuthViewModel authViewModel;
    private FilterViewModel filterViewModel;


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
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initRecyclerView();
        authViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypes -> {
                            Map<String, Integer> categorySizes = new HashMap<>();
                            categoryTypes.forEach(categoryTypesWithCategories -> {
                                String categoryTypeId = categoryTypesWithCategories.getCategoryType().getCategoryTypeId();
                                int categoriesSize = categoryTypesWithCategories.getCategories().size();
                                categorySizes.put(categoryTypeId, categoriesSize);
                            });
                            adapter.setCategorySizes(categorySizes);
                        }
                );

        authViewModel.getSelectedCategoriesLiveData().observe(getViewLifecycleOwner(), categoryTypesWithCategories ->
                adapter.setSelectedCategories(categoryTypesWithCategories)

        );

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