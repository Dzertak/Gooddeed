package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryTypeFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryTypeRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kravchenko.apps.gooddeed.screen.filterwindow.FilterFragmentMain.FILTER_FRAGMENT_MAIN_KEY;
import static com.kravchenko.apps.gooddeed.screen.initiative.EditInitiativeFragment.EDIT_INITIATIVE_FRAGMENT_TAG;
import static com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragment.EDIT_PROFILE_KEY;
import static com.kravchenko.apps.gooddeed.screen.settings.SubscriptionsSettingsFragment.SETTINGS_FRAGMENT_TAG;

public class CategoryTypeFilterFragment extends BaseFragment {
    private FragmentCategoryTypeFilterBinding binding;
    private CategoryTypeRecyclerViewAdapter adapter;
    private FilterViewModel filterViewModel;
    private String rootDirection;
    private OnBackPressedCallback callback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryTypeFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        //нужно подумать когда именно очищать
//        binding.toolbar.setNavigationOnClickListener(t -> {
//            clear();
//            getNavController().navigateUp();
//        });
        initRecyclerView();

        if (getArguments() != null) {
            rootDirection = CategoryTypeFilterFragmentArgs.fromBundle(getArguments()).getRootDirection();
            switch (rootDirection) {
                case FILTER_FRAGMENT_MAIN_KEY:
                    binding.btnSearch.setOnClickListener(v -> {
                        getNavController().popBackStack(R.id.categoryTypeFilterFragment, true);
                        filterViewModel.setIsBackPressed(true);
                    });
                    initFilterPreset();
                    break;
                case EDIT_INITIATIVE_FRAGMENT_TAG:
                    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            getNavController().popBackStack();
                        }
                    };
                    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
                    initInitiativePreset();
                    binding.btnSearch.setText(R.string.confirm);
                    binding.btnSearch.setOnClickListener(v -> {
                        getNavController().popBackStack();
                    });
                    break;
                case SETTINGS_FRAGMENT_TAG:
                    break;
                case EDIT_PROFILE_KEY:
                    binding.btnSearch.setText(R.string.confirm);
                    binding.btnSearch.setOnClickListener(v -> {
                        getNavController().popBackStack();
                    });
                    initProfilePreset();
                    break;
            }
        }
    }

    private void initProfilePreset() {
        filterViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypeWithCategories -> {
                    adapter.setCategorySizes(buildCategorySizesMap(categoryTypeWithCategories));
                });
        filterViewModel.getSubscriptionsSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    adapter.setSelectedCategories(categoryTypesWithCategories);
                });
    }

    private void initInitiativePreset() {
        filterViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    adapter.setCategorySizes(buildCategorySizesMap(categoryTypesWithCategories));
                });
        filterViewModel.getInitiativesSelectedCategoriesLiveData()
                .observe(getViewLifecycleOwner(), selectedCategoryTypesWithCategories -> {
                            if (selectedCategoryTypesWithCategories == null) {
                                filterViewModel.getCategoryTypesWithCategoriesLiveData()
                                        .observe(getViewLifecycleOwner(), filterViewModel::initInitiativesSelectedCategoriesLiveData);
                            } else {
                                adapter.setSelectedCategories(selectedCategoryTypesWithCategories);
                            }
                        }
                );
    }


    private void initFilterPreset() {
        filterViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                            adapter.setCategorySizes(buildCategorySizesMap(categoryTypesWithCategories));
                        }
                );
        filterViewModel.getMapSelectedCategoriesLiveData().observe(getViewLifecycleOwner(), selectedCategoryTypesWithCategories -> {
            if (selectedCategoryTypesWithCategories == null) {
                filterViewModel.getCategoryTypesWithCategoriesLiveData()
                        .observe(getViewLifecycleOwner(), filterViewModel::initMapSelectedCategoriesLiveData);
            } else {
                adapter.setSelectedCategories(selectedCategoryTypesWithCategories);
            }
        });
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

    private Map<Long, Integer> buildCategorySizesMap(List<CategoryTypeWithCategories> categoryTypesWithCategories) {
        Map<Long, Integer> categorySizes = new HashMap<>();
        for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
            long categoryTypeId = categoryTypeWithCategories.getCategoryType().getCategoryTypeId();
            int categoriesSize = categoryTypeWithCategories.getCategories().size();
            categorySizes.put(categoryTypeId, categoriesSize);
        }
        return categorySizes;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void clear() {
        //нужно добавить проверку. В случае если мы возвращаемся на mainFragment, то смело очищать viewmodel
        //requireActivity().getViewModelStore().clear();
    }
}