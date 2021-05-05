package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilterFragment extends BaseFragment {
    private CategoryRecyclerViewAdapter adapter;
    private FragmentCategoryFilterBinding binding;
    private String categoryTypeId;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        if (getArguments() != null) {
            categoryTypeId = CategoryFilterFragmentArgs.fromBundle(getArguments()).getCategoryTypeId();
            Toast.makeText(getContext(), categoryTypeId, Toast.LENGTH_SHORT).show();
        }
        initRecyclerView();
        authViewModel.findCategoryTypesByCategoryOwnerId(categoryTypeId)
                .observe(getViewLifecycleOwner(), categoryTypes -> {
                    adapter.setCategories(categoryTypes);
                });
    }

    private void initRecyclerView() {
        adapter = new CategoryRecyclerViewAdapter(getContext());
//        adapter.setOnItemClickListener(categoryType -> {
//            Toast.makeText(getContext(), Utils.getString((int) categoryType.getTitle()), Toast.LENGTH_SHORT).show();
//            //TODO change args
//            NavDirections action = CategoryTypeFilterFragmentDirections.actionCategoryTypeFilterFragmentToCategoryFilterFragment(Utils.getString(categoryType.getTitle()));
//            getNavController().navigate(action);
//        });
        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategories.setAdapter(adapter);
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(R.string.building_title, R.id.tv_about_title));
        categories.add(new Category(R.string.building_title, R.id.tv_about_title));
        categories.add(new Category(R.string.building_title, R.id.tv_about_title));
        categories.add(new Category(R.string.building_title, R.id.tv_about_title));
        adapter.setCategories(categories);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}