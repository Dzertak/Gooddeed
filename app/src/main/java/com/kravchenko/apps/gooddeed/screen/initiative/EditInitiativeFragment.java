package com.kravchenko.apps.gooddeed.screen.initiative;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.FragmentInitiativeEditBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.TimeUtil;
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;
import com.kravchenko.apps.gooddeed.viewmodel.InitiativeViewModel;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class EditInitiativeFragment extends BaseFragment {

    private FragmentInitiativeEditBinding binding;
    private final String[] categoryName = {"Help", "Work", "Volunteer", "Meeting"};
    private Initiative initiativeCur;
    private InitiativeViewModel initiativeViewModel;
    private Calendar calendar;
    public static final String TAG = EditInitiativeFragment.class.getName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInitiativeEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initDropDownCategory();

        calendar = Calendar.getInstance();
        binding.tvCategory.setOnClickListener(v -> {
            NavDirections action
                    = EditInitiativeFragmentDirections.actionEditInitiativeFragmentToCategoryNavGraph(TAG);
            getNavController().navigate(action);
        });
        initiativeViewModel = new ViewModelProvider(requireActivity()).get(InitiativeViewModel.class);
        initiativeViewModel.getInitiative().observe(getViewLifecycleOwner(), initiative -> {
            initiativeCur = initiative != null ? initiative : new Initiative();
            if (initiativeCur.getLocation() != null)
                binding.tvLocation.setText(initiativeCur.getLocation());
            if (initiativeCur.getTimestamp() > 0)
                binding.tvTime.setText(TimeUtil.convertToDisplayTime(initiativeCur.getTimestamp()));
            if (initiativeCur.getType() != null) {
                switch (initiativeCur.getType()) {
                    case InitiativeType.SINGLE:
                        binding.tvType.setText(getString(R.string.single));
                        break;
                    case InitiativeType.GROUP:
                        binding.tvType.setText(getString(R.string.group));
                        break;
                    case InitiativeType.UNLIMITED:
                        binding.tvType.setText(getString(R.string.unlimited));
                        break;
                }
            }
        });


        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                if (initiativeCur == null) initiativeCur = new Initiative();
                initiativeCur.setTimestamp(calendar.getTimeInMillis());
                initiativeViewModel.updateInitiative(initiativeCur);
            }
        };
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //+5 for making initiative only in future
                new TimePickerDialog(view.getContext(), timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), Calendar.MINUTE + 5, true).show();
            }
        };

        binding.cvLocationChoice.setOnClickListener(v -> getNavController().navigate(R.id.action_editInitiativeFragment_to_pickInitiativeLocationFragment));
        binding.cvTimeChoice.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        binding.cvImageChoice.setOnClickListener(v -> pickImage());
        binding.cvTypeChoice.setOnClickListener(t -> getNavController().navigate(R.id.action_editInitiativeFragment_to_initiativeTypeFragment));
    }

    private void initDropDownCategory() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryName);
        binding.cvCategoryChoice.setOnClickListener(t -> {
            Toast.makeText(requireContext(), "Choice category", Toast.LENGTH_SHORT).show();
        });
    }

    private void pickImage() {
        ImagePicker.Companion.with(this)
                .galleryMimeTypes(new String[]{"image/png", "image/jpg", "image/jpeg"})
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {

                Glide.with(this)
                        .load(data.getData())
                        .fallback(R.drawable.no_photo)
                        .into(binding.ivInitiativeImage);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Toast.makeText(requireContext(), "Сохранить инициативу", Toast.LENGTH_SHORT).show();
            getNavController().navigate(R.id.action_editInitiativeFragment_to_currentInitiativeFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
