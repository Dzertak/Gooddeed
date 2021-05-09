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
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.kravchenko.apps.gooddeed.screen.filterwindow.FilterFragmentMain.FILTER_FRAGMENT_MAIN_KEY;
import static com.kravchenko.apps.gooddeed.screen.initiative.EditInitiativeFragment.EDIT_INITIATIVE_FRAGMENT_TAG;
import static com.kravchenko.apps.gooddeed.screen.settings.SubscriptionsSettingsFragment.SUBSCRIPTIONS_SETTINGS_FRAGMENT_TAG;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CategoryTypeFilterFragment extends BaseFragment {
    private FragmentCategoryTypeFilterBinding binding;
    private CategoryTypeRecyclerViewAdapter adapter;
    private AuthViewModel authViewModel;
    private FilterViewModel filterViewModel;
    private String rootDirection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryTypeFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initRecyclerView();

        if (getArguments() != null) {
            rootDirection = CategoryTypeFilterFragmentArgs.fromBundle(getArguments()).getRootDirection();
            switch (rootDirection) {
                case FILTER_FRAGMENT_MAIN_KEY:
                    initFilterPreset();
                    break;
                case EDIT_INITIATIVE_FRAGMENT_TAG:
                    initInitiativePreset();
                    break;
                case SUBSCRIPTIONS_SETTINGS_FRAGMENT_TAG:
                    break;
            }
        }
        filterViewModel.getInitiativesSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypeWithCategories -> {
                    categoryTypeWithCategories.forEach(selectedCategory -> {
                        selectedCategory.getCategories().forEach(category -> {
                            Log.i("dev", Utils.getString(selectedCategory.getCategoryType().getTitle()) + ": " + Utils.getString(category.getTitle()));
                        });
                        Log.i("dev", "********************************");
                    });
                    Log.i("dev", "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                });
    }

    private void initInitiativePreset() {
        authViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypeWithCategories -> {
                    Map<Long, Integer> categorySizes = new HashMap<>();
                    categoryTypeWithCategories.forEach(categoryTypesWithCategories -> {
                        long categoryTypeId = categoryTypesWithCategories.getCategoryType().getCategoryTypeId();
                        int categoriesSize = categoryTypesWithCategories.getCategories().size();
                        categorySizes.put(categoryTypeId, categoriesSize);
                    });
                    adapter.setCategorySizes(categorySizes);
                });
        filterViewModel.getInitiativesSelectedCategoriesLiveData().observe(getViewLifecycleOwner(), categoryTypesWithCategories ->
                adapter.setSelectedCategories(categoryTypesWithCategories));
    }

    private void initFilterPreset() {
        authViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypes -> {
                            Map<Long, Integer> categorySizes = new HashMap<>();
                            categoryTypes.forEach(categoryTypesWithCategories -> {
                                long categoryTypeId = categoryTypesWithCategories.getCategoryType().getCategoryTypeId();
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
            NavDirections action
                    = CategoryTypeFilterFragmentDirections
                    .actionCategoryTypeFilterFragment2ToCategoryFilterFragment(categoryType.getCategoryTypeId(), rootDirection);
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