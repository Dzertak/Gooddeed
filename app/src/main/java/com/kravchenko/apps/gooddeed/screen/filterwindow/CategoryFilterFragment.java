package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

public class CategoryFilterFragment extends BaseFragment {
    private CategoryRecyclerViewAdapter adapter;
    private FragmentCategoryFilterBinding binding;
    private String categoryTypeId;
    private AuthViewModel authViewModel;
    private FilterViewModel filterViewModel;
    private int categoriesSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        if (getArguments() != null) {
            categoryTypeId = CategoryFilterFragmentArgs.fromBundle(getArguments()).getCategoryTypeId();
        }
        initRecyclerView();
        authViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categories -> {
                    categoriesSize = categories.size();
                    adapter.setCategories(categories);
                });

        filterViewModel.getSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategories -> {

                    if (selectedCategories.isEmpty() || selectedCategories.size() < categoriesSize) {
                        binding.cardViewSelectAll.setCardBackgroundColor(Color.WHITE);
                        binding.imageViewCheckBox.setVisibility(View.GONE);
                    } else {
                        binding.cardViewSelectAll.setCardBackgroundColor(Color.LTGRAY);
                        binding.imageViewCheckBox.setVisibility(View.VISIBLE);
                    }

                });
        binding.cardViewSelectAll.setOnClickListener(v -> adapter.selectAll());
    }

    private void initRecyclerView() {
        adapter = new CategoryRecyclerViewAdapter(getContext(), filterViewModel);
        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategories.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}