package com.kravchenko.apps.gooddeed.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentEditInitiativeBinding;

public class EditInitiativeFragment extends BaseFragment {

    private FragmentEditInitiativeBinding binding;
    private final String[] categoryName = {"Help", "Work", "Volunteer", "Meeting"};
    private final String[] statusName = {"New", "Viewed", "Not active", "Mine"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditInitiativeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initDropDownCategory();
        initDropDownStatus();

        binding.chosePlaceOnMapButton.setOnClickListener(v -> getNavController().navigate(R.id.action_editInitiativeFragment_to_mainFragment));
        binding.confirmButton.setOnClickListener(v -> getNavController().navigate(R.id.action_editInitiativeFragment_to_currentInitiativeFragment));
    }

    private void initDropDownCategory() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryName);
        binding.editCategory.setAdapter(arrayAdapter);
        binding.editCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String category = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initDropDownStatus() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statusName);
        binding.editStatus.setAdapter(arrayAdapter);
        binding.editStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String status = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}