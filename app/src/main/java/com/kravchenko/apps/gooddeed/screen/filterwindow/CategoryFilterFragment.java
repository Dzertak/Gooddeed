package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.graphics.Color;
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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        authViewModel.getSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategories -> {
                    List<Category> categories = new ArrayList<>();
                    selectedCategories.forEach(categoryTypesWithCategories -> {
                        if (categoryTypesWithCategories.getCategoryType()
                                .getCategoryTypeId().equals(categoryTypeId)) {

                            categories.addAll(categoryTypesWithCategories.getCategories());
                           /* categories.forEach(category -> {
                                Log.i("dev", category.getCategoryId());
                            });
                            Log.i("dev", "****************");*/
                        }
                    });
                    if (categories.isEmpty() || categories.size() < categoriesSize) {
                        binding.cardViewSelectAll.setCardBackgroundColor(Color.WHITE);
                        binding.textViewSelectAllTitle.setText(R.string.select_all);
                        binding.imageViewCheckBox.setVisibility(View.GONE);
                    } else {
                        binding.cardViewSelectAll.setCardBackgroundColor(Color.LTGRAY);
                        binding.textViewSelectAllTitle.setText(R.string.clear);
                        binding.imageViewCheckBox.setVisibility(View.VISIBLE);
                    }
                });

        authViewModel.getSelectedCategoriesLiveData().observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
            List<Category> categories1 = new ArrayList<>();
            categoryTypesWithCategories.forEach(categoryTypeWithCategories -> {
                if (categoryTypeWithCategories.getCategoryType()
                        .getCategoryTypeId().equals(categoryTypeId)) {
                    categories1.addAll(categoryTypeWithCategories.getCategories());
                }
                adapter.setSelectedCategories(categories1);
            });
        });
        binding.cardViewSelectAll.setOnClickListener(v -> adapter.selectAll());
        authViewModel.getSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategories -> {
                    //Todo
                    // handle filtered categories
                    selectedCategories.forEach(selectedCategory -> {
                        selectedCategory.getCategories().forEach(category -> {
                            Log.i("dev", "Main: " + category.getCategoryId());
                        });
                        Log.i("dev", "********************************");
                    });
                    Log.i("dev", "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                    Log.i("dev", "\n");
                    Log.i("dev", "\n");
                    Log.i("dev", "\n");
                });

    }


    private void initRecyclerView() {
        adapter = new CategoryRecyclerViewAdapter(getContext(), authViewModel);
        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategories.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}