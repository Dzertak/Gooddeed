package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.InitiativeFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.MapFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.SubscriptionsFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.kravchenko.apps.gooddeed.screen.filterwindow.FilterFragmentMain.FILTER_FRAGMENT_MAIN_KEY;
import static com.kravchenko.apps.gooddeed.screen.initiative.EditInitiativeFragment.EDIT_INITIATIVE_FRAGMENT_TAG;
import static com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragment.EDIT_PROFILE_KEY;
import static com.kravchenko.apps.gooddeed.screen.settings.SubscriptionsSettingsFragment.SUBSCRIPTIONS_SETTINGS_FRAGMENT_TAG;


public class CategoryFilterFragment extends BaseFragment {
    private FragmentCategoryFilterBinding binding;
    private long categoryTypeId;
    private MapFilterRecyclerViewAdapter mapFilterAdapter;
    private InitiativeFilterRecyclerViewAdapter initiativeFilterAdapter;
    private FilterViewModel filterViewModel;
    private SubscriptionsFilterRecyclerViewAdapter subscriptionsFilterAdapter;
    private int categoriesSize;
    private String rootDirection;
    private OnBackPressedCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            categoryTypeId = CategoryFilterFragmentArgs.fromBundle(getArguments()).getCategoryTypeId();
            rootDirection = CategoryFilterFragmentArgs.fromBundle(getArguments()).getRootDirection();
            switch (rootDirection) {
                case FILTER_FRAGMENT_MAIN_KEY:
                    mapFilterAdapter = new MapFilterRecyclerViewAdapter(getContext(), filterViewModel);
                    binding.recyclerViewCategories.setAdapter(mapFilterAdapter);
                    binding.btnSearch.setOnClickListener(v -> {
                        getNavController().popBackStack(R.id.categoryTypeFilterFragment2, true);
                        filterViewModel.setIsBackPressed(true);
                    });
                    initFilterPreset();
                    break;
                case EDIT_INITIATIVE_FRAGMENT_TAG:
                    callback = new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            getNavController().popBackStack();
                        }
                    };
                    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

                    binding.cardViewSelectAll.setVisibility(View.GONE);
                    initiativeFilterAdapter = new InitiativeFilterRecyclerViewAdapter(getContext(), filterViewModel);
                    binding.recyclerViewCategories.setAdapter(initiativeFilterAdapter);
                    binding.btnSearch.setText(R.string.confirm);
                    binding.btnSearch.setOnClickListener(v -> {
                        getNavController().popBackStack(R.id.categoryTypeFilterFragment2, true);
                    });
                    initInitiativePreset();
                    break;
                case SUBSCRIPTIONS_SETTINGS_FRAGMENT_TAG:

                    break;
                case EDIT_PROFILE_KEY:
                    binding.btnSearch.setVisibility(View.GONE);
                    subscriptionsFilterAdapter = new SubscriptionsFilterRecyclerViewAdapter(getContext(), filterViewModel);
                    binding.recyclerViewCategories.setAdapter(subscriptionsFilterAdapter);
                    initProfilePreset();
                    break;
            }
        }
    }

    private void initProfilePreset() {
        binding.cardViewSelectAll.setOnClickListener(v -> subscriptionsFilterAdapter.selectAll());

        filterViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categories -> {
                    categoriesSize = categories.size();
                    subscriptionsFilterAdapter.setCategories(categories);
                });

        filterViewModel.getSubscriptionsSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategories -> {
                    List<Category> categories
                            = getCategoriesFromCategoryTypesWithCategories(selectedCategories);
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

        filterViewModel.getSubscriptionsSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    List<Category> categories = new ArrayList<>();
                    for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
                        if (categoryTypeWithCategories.getCategoryType()
                                .getCategoryTypeId() == categoryTypeId) {
                            categories.addAll(categoryTypeWithCategories.getCategories());
                        }
                        subscriptionsFilterAdapter.setSelectedCategories(categories);
                    }
                });
    }

    private void initInitiativePreset() {
        filterViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categories -> {
                    categoriesSize = categories.size();
                    initiativeFilterAdapter.setCategories(categories);
                });
        filterViewModel.getInitiativesSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    List<Category> categories = new ArrayList<>();
                    for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
                        if (categoryTypeWithCategories.getCategoryType()
                                .getCategoryTypeId() == categoryTypeId) {
                            categories.addAll(categoryTypeWithCategories.getCategories());
                        }
                        initiativeFilterAdapter.setSelectedCategories(categories);
                    }
                });
    }

    private void initFilterPreset() {
        binding.cardViewSelectAll.setOnClickListener(v -> mapFilterAdapter.selectAll());

        filterViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categories -> {
                    categoriesSize = categories.size();
                    mapFilterAdapter.setCategories(categories);
                });

        filterViewModel.getMapSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategories -> {
                    List<Category> categories
                            = getCategoriesFromCategoryTypesWithCategories(selectedCategories);
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

        filterViewModel.getMapSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    List<Category> categories = new ArrayList<>();
                    for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
                        if (categoryTypeWithCategories.getCategoryType()
                                .getCategoryTypeId() == categoryTypeId) {
                            categories.addAll(categoryTypeWithCategories.getCategories());
                        }
                        mapFilterAdapter.setSelectedCategories(categories);
                    }
                });
    }

    private List<Category> getCategoriesFromCategoryTypesWithCategories(List<CategoryTypeWithCategories> selectedCategories) {
        List<Category> categories = new ArrayList<>();
        for (CategoryTypeWithCategories categoryTypeWithCategories : selectedCategories) {
            if (categoryTypeWithCategories.getCategoryType()
                    .getCategoryTypeId() == categoryTypeId) {
                categories.addAll(categoryTypeWithCategories.getCategories());
            }
        }
        return categories;
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
