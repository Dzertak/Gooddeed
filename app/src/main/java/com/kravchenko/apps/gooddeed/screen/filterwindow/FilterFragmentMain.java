package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentFilterMainBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;


public class FilterFragmentMain extends BaseFragment {
    private FragmentFilterMainBinding binding;
    public static final String FILTER_FRAGMENT_MAIN_KEY = "FILTER_FRAGMENT_MAIN_KEY";
    private final String DATA_PICKER_TAG = "DATA_PICKER";
    private FilterViewModel filterViewModel;
    private OnBackPressedCallback callback;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterMainBinding.inflate(inflater, container, false);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        callback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                filterViewModel.setIsBackPressed(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarFilterFragment);
        NavigationUI.setupWithNavController(binding.toolbarFilterFragment, getNavController());
        filterViewModel
                .getIsDrawerOpen()
                .observe(getViewLifecycleOwner(), isDrawerOpen -> {
                    callback.setEnabled(isDrawerOpen);
                });

        MaterialDatePicker<Pair<Long, Long>> datePicker = Utils.createMaterialDatePicker();
        binding.textViewDataRange.setText(Utils.getDateRange());
        binding.cardViewDataPicker.setOnClickListener(v -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), DATA_PICKER_TAG);
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            binding.textViewDataRange.setText(datePicker.getHeaderText());
            //TODO
            // Handle DataPicker result
        });
        binding.cardViewCategories.setOnClickListener(v -> {
            NavDirections action
                    = FilterFragmentMainDirections.actionFilterFragmentToCategoryNavGraph(FILTER_FRAGMENT_MAIN_KEY);
            getNavController().navigate(action);
        });
        binding.btnSearch.setOnClickListener(v -> {
            filterViewModel.setIsBackPressed(true);
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        Log.i("dev", "onCreateOptionsMen");
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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