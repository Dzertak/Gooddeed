package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentFilterMainBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.Utils;


public class FilterFragmentMain extends BaseFragment {
    private FragmentFilterMainBinding binding;

    private final String DATA_PICKER_TAG = "DATA_PICKER";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //TODO
    // MACE BASE FRAGMENT
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarFilterFragment);
        NavigationUI.setupWithNavController(binding.toolbarFilterFragment, getNavController());


        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = Utils.createMaterialDatePicker();
        binding.textViewDataRange.setText(Utils.getDateRange());
        binding.cardViewDataPicker.setOnClickListener(v -> {
            materialDatePicker.show(requireActivity().getSupportFragmentManager(), DATA_PICKER_TAG);
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            binding.textViewDataRange.setText(materialDatePicker.getHeaderText());
            //TODO
            // Handle DataPicker result
        });
        binding.cardViewCategories.setOnClickListener(v ->
                getNavController().navigate(R.id.action_filterFragment_to_categoryTypeFilterFragment));
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
}