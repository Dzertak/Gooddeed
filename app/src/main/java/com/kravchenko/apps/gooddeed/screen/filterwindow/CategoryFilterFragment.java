package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.graphics.Color;
import android.os.Bundle;
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
import com.kravchenko.apps.gooddeed.screen.adapter.filter.BaseCategoryRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.MultiChoiceFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.SingleChoseFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.kravchenko.apps.gooddeed.screen.filterwindow.FilterFragmentMain.FILTER_FRAGMENT_MAIN_KEY;
import static com.kravchenko.apps.gooddeed.screen.initiative.EditInitiativeFragment.EDIT_INITIATIVE_FRAGMENT_TAG;
import static com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragment.EDIT_PROFILE_KEY;
import static com.kravchenko.apps.gooddeed.screen.settings.SubscriptionsSettingsFragment.SETTINGS_FRAGMENT_TAG;

public class CategoryFilterFragment extends BaseFragment implements FilterCallBack {
    private FragmentCategoryFilterBinding binding;
    private long categoryTypeId;
    private FilterViewModel filterViewModel;
    private BaseCategoryRecyclerViewAdapter adapter;
    private int categoriesSize;
    private String rootDirection;
    private OnBackPressedCallback callback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getNavController().popBackStack();
            }
        };
        binding = FragmentCategoryFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryTypeId = CategoryFilterFragmentArgs.fromBundle(getArguments()).getCategoryTypeId();
        rootDirection = CategoryFilterFragmentArgs.fromBundle(getArguments()).getRootDirection();

        switch (rootDirection) {
            case FILTER_FRAGMENT_MAIN_KEY:
                initFilterPreset();
                break;
            case EDIT_INITIATIVE_FRAGMENT_TAG:
                initInitiativePreset();
                break;
            case SETTINGS_FRAGMENT_TAG:
                //TODO
                break;
            case EDIT_PROFILE_KEY:
                initProfilePreset();
                break;
        }

        binding.recyclerViewCategories.setAdapter(adapter);

        filterViewModel.findCategoryTypeById(categoryTypeId)
                .observe(getViewLifecycleOwner(), categoryType ->
                        binding.toolbar.setTitle(Utils.getString(categoryType.getTitle())));

        filterViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categories -> {
                    categoriesSize = categories.size();
                    adapter.setCategories(categories);
                });
    }

    private void initProfilePreset() {
        binding.btnSearch.setVisibility(View.GONE);
        adapter = new MultiChoiceFilterRecyclerViewAdapter(getContext(), this);
        binding.cardViewSelectAll.setOnClickListener(v -> adapter.selectAll());

        filterViewModel.getSubscriptionsSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    manageSelectAll(getSelectedCategories(categoryTypesWithCategories));
                    adapter.setSelectedCategories(getSelectedCategories(categoryTypesWithCategories));
                });
    }

    private void initFilterPreset() {
        adapter = new MultiChoiceFilterRecyclerViewAdapter(getContext(), this);
        binding.btnSearch.setOnClickListener(v -> {
            getNavController().popBackStack(R.id.categoryTypeFilterFragment, true);
            filterViewModel.setIsBackPressed(true);
        });
        binding.cardViewSelectAll.setOnClickListener(v -> adapter.selectAll());

        filterViewModel.getMapSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    adapter.setSelectedCategories(getSelectedCategories(categoryTypesWithCategories));
                    manageSelectAll(getSelectedCategories(categoryTypesWithCategories));
                });
    }

    private void initInitiativePreset() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        binding.cardViewSelectAll.setVisibility(View.GONE);
        adapter = new SingleChoseFilterRecyclerViewAdapter(getContext(), this);
        binding.btnSearch.setText(R.string.confirm);
        binding.btnSearch.setOnClickListener(v -> {
            getNavController().popBackStack(R.id.categoryTypeFilterFragment, true);
        });

        filterViewModel.getInitiativesSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    adapter.setSelectedCategories(getSelectedCategories(categoryTypesWithCategories));
                });
    }

    private List<Category> getSelectedCategories(List<CategoryTypeWithCategories> categoryTypesWithCategories) {
        List<Category> categories = new ArrayList<>();
        for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
            if (categoryTypeWithCategories.getCategoryType()
                    .getCategoryTypeId() == categoryTypeId) {
                categories.addAll(categoryTypeWithCategories.getCategories());
            }
        }
        return categories;
    }

    private void manageSelectAll(List<Category> categories) {
        if (categories.isEmpty() || categories.size() < categoriesSize) {
            binding.cardViewSelectAll.setCardBackgroundColor(Color.WHITE);
            binding.textViewSelectAllTitle.setText(R.string.select_all);
            binding.imageViewCheckBox.setVisibility(View.GONE);
        } else {
            binding.cardViewSelectAll.setCardBackgroundColor(Color.LTGRAY);
            binding.textViewSelectAllTitle.setText(R.string.clear);
            binding.imageViewCheckBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void clear() {
        //TODO
    }

    @Override
    public void setSelectedCategories(List<Category> categories, long categoryOwnerId) {
        switch (rootDirection) {
            case FILTER_FRAGMENT_MAIN_KEY:
                filterViewModel.setMapSelectedCategoriesLiveData(categories, categoryOwnerId);
                break;
            case EDIT_INITIATIVE_FRAGMENT_TAG:
                filterViewModel.setInitiativesSelectedCategory(categories, categoryOwnerId);
                break;
            case SETTINGS_FRAGMENT_TAG:

                break;
            case EDIT_PROFILE_KEY:
                filterViewModel.setSubscriptionsSelectedCategoriesLiveData(categories, categoryOwnerId);
                break;
        }
    }
}
