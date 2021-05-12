package com.kravchenko.apps.gooddeed.screen.initiative;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.databinding.FragmentInitiativeEditBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.TimeUtil;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;
import com.kravchenko.apps.gooddeed.viewmodel.InitiativeViewModel;

import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditInitiativeFragment extends BaseFragment {

    public static final String EDIT_INITIATIVE_FRAGMENT_TAG = "EDIT_INITIATIVE_FRAGMENT_TAG";
    private FragmentInitiativeEditBinding binding;
    private Initiative initiativeCur;
    private InitiativeViewModel initiativeViewModel;
    private Calendar calendar;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        calendar = Calendar.getInstance();
        binding.cvCategoryChoice.setOnClickListener(v -> {
            NavDirections action
                    = EditInitiativeFragmentDirections.actionEditInitiativeFragmentToCategoryNavGraph(EDIT_INITIATIVE_FRAGMENT_TAG);
            getNavController().navigate(action);
        });
        initiativeViewModel = new ViewModelProvider(requireActivity()).get(InitiativeViewModel.class);
        initiativeViewModel.getInitiative().observe(getViewLifecycleOwner(), initiative -> {
            initiativeCur = initiative != null ? initiative : new Initiative();
            if (initiativeCur.getTitle() != null)
                binding.etInitiativeTitle.setText(initiativeCur.getTitle());
            if (initiativeCur.getDescription() != null)
                binding.etDescription.setText(initiativeCur.getDescription());
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
            if (initiativeCur.getCategory() != null)
                binding.tvCategory.setText(initiativeCur.getCategory());
            if (initiativeCur.getImgUri() != null)
                Glide.with(this)
                        .load(initiativeCur.getImgUri())
                        .fallback(R.drawable.no_photo)
                        .into(binding.ivInitiativeImage);
        });

        initiativeViewModel.getSavingInitiative().observe(getViewLifecycleOwner(), initiativeResource -> {
            if (initiativeResource.status.equals(Resource.Status.LOADING)) {
                Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show();
            } else if (initiativeResource.status.equals(Resource.Status.SUCCESS)) {
                Toast.makeText(requireContext(), "Initiative saved", Toast.LENGTH_SHORT).show();
//                getNavController().navigate(R.id.action_editInitiativeFragment_to_currentInitiativeFragment);
            } else if (initiativeResource.status.equals(Resource.Status.ERROR)) {
                Toast.makeText(requireContext(), "Error" + initiativeResource.message, Toast.LENGTH_SHORT).show();
            }
        });

        initiativeViewModel.getSelectedCategory().observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
            Category category = null;
            if (categoryTypesWithCategories != null) {
                for (CategoryTypeWithCategories categoryTypeWithCategories : categoryTypesWithCategories) {
                    List<Category> selectedCategories = categoryTypeWithCategories.getCategories();
                    if (selectedCategories != null && !selectedCategories.isEmpty()) {
                        category = selectedCategories.get(0);
                    }
                }
            }
            if (category != null) {
                String categoryTitle = Utils.getString(category.getTitle());
                if (initiativeCur == null) initiativeCur = new Initiative();
                initiativeCur.setCategory(categoryTitle);
                initiativeViewModel.updateInitiative(initiativeCur);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                if (initiativeCur == null) initiativeCur = new Initiative();
                initiativeCur.setImgUri(data.getData().toString());
                initiativeViewModel.updateInitiative(initiativeCur);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (isFormValid()) {
                if (!initiativeViewModel.getAuthUserId().isEmpty())
                    initiativeCur.setInitiativeUserId(initiativeViewModel.getAuthUserId());
                initiativeViewModel.saveInitiative(initiativeCur);
                initiativeViewModel.updateInitiative(initiativeCur);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFormValid() {
        hideKeyboard();
        if (binding.etInitiativeTitle.getText() == null
                || TextUtils.isEmpty(binding.etInitiativeTitle.getText())) {
            binding.tilInitiativeTitle.setError(getString(R.string.error_invalid_initiative_title));
            return false;
        }
        if (binding.tilInitiativeTitle.getError() != null)
            binding.tilInitiativeTitle.setError(null);
        if (binding.etDescription.getText() == null
                || TextUtils.isEmpty(binding.etDescription.getText())) {
            binding.tilInitiativeDescription.setError(getString(R.string.error_invalid_initiative_description));
            return false;
        }
        if (binding.tilInitiativeDescription.getError() != null)
            binding.tilInitiativeDescription.setError(null);
        if (binding.tvCategory.getText().equals(getString(R.string.choose_category))) {
            binding.cvCategoryChoice.setCardBackgroundColor(getResources().getColor(R.color.error));
            return false;
        }
        if (binding.tvType.getText().equals(getString(R.string.choose_type_initiative))) {
            binding.cvTypeChoice.setCardBackgroundColor(getResources().getColor(R.color.error));
            return false;
        }
        if (binding.tvTime.getText().equals(getString(R.string.choose_date_and_time))) {
            binding.cvTimeChoice.setCardBackgroundColor(getResources().getColor(R.color.error));
            return false;
        }
        if (binding.tvLocation.getText().equals(getString(R.string.choose_location))) {
            binding.cvLocationChoice.setCardBackgroundColor(getResources().getColor(R.color.error));
            return false;
        }
        initiativeCur.setTitle(binding.etInitiativeTitle.getText().toString());
        initiativeCur.setDescription(binding.etDescription.getText().toString());
        return true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    private void pickImage() {
        ImagePicker.Companion.with(this)
                .galleryMimeTypes(new String[]{"image/png", "image/jpg", "image/jpeg"})
                .compress(2024)
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
