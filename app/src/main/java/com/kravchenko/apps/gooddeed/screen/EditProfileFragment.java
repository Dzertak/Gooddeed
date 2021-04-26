package com.kravchenko.apps.gooddeed.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentEditProfileBinding;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;
import com.kravchenko.apps.gooddeed.viewmodel.ProfileViewModel;

public class EditProfileFragment extends BaseFragment {

    private FragmentEditProfileBinding binding;
    private ProfileViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        mViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        mViewModel.getUser().observe(getViewLifecycleOwner(), firestoreUser -> {
            if (firestoreUser.data != null && firestoreUser.status.equals(Resource.Status.SUCCESS)) {
                if (firestoreUser.data.getFirstName() != null && firestoreUser.data.getLastName() != null) {
                    binding.tvProfileFirstName.setText(firestoreUser.data.getFirstName());
                    binding.tvProfileLastName.setText(firestoreUser.data.getLastName());
                }
                if (firestoreUser.data.getImageUrl() != null) {
                    Glide.with(this)
                            .load(firestoreUser.data.getImageUrl())
                            .into(binding.imageViewEditProfileAvatar);
                }
                binding.tvProfileEmail.setText(firestoreUser.data.getEmail());
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_settings) {
            //TODO
        }
        return super.onOptionsItemSelected(item);
    }
}