package com.kravchenko.apps.gooddeed.screen.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kravchenko.apps.gooddeed.R;

import com.kravchenko.apps.gooddeed.databinding.FragmentProfileEditBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.subscription.SubscriptionAdapter;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends BaseFragment {

    private FragmentProfileEditBinding binding;
    private ProfileViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        //for test
        List<String> subscriptions = new ArrayList<>();
        subscriptions.add("Уборка територий");
        subscriptions.add("Массаж");
        subscriptions.add("Ремонт техники");
        subscriptions.add("Иностранные языки");
        subscriptions.add("Услуги психолога или психотерапевта");
        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(requireContext(), subscriptions, true);
        //
        binding.recyclerViewSubscriptions.setAdapter(subscriptionAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.recyclerViewSubscriptions.setLayoutManager(layoutManager);

        mViewModel.getUser().observe(getViewLifecycleOwner(), firestoreUser -> {
            if (firestoreUser.status.equals(Resource.Status.SUCCESS)) {
                if (firestoreUser.data.getFirstName() != null && firestoreUser.data.getLastName() != null) {
                    binding.etProfileFirstName.setText(firestoreUser.data.getFirstName());
                    binding.etProfileLastName.setText(firestoreUser.data.getLastName());
                }

                Glide.with(this)
                        .load(firestoreUser.data.getImageUrl())
                        .fallback(R.drawable.no_photo)
                        .into(binding.ivProfileAvatar);

                if (firestoreUser.data.getDescription() != null) {
                    binding.etDescription.setText(firestoreUser.data.getDescription());
                }
                binding.etProfileEmail.setText(firestoreUser.data.getEmail());
            } else if (firestoreUser.status.equals(Resource.Status.LOADING)) {
                Toast.makeText(requireContext(), firestoreUser.message, Toast.LENGTH_SHORT).show();
            } else if (firestoreUser.status.equals(Resource.Status.ERROR)) {
                Toast.makeText(requireContext(), firestoreUser.message, Toast.LENGTH_SHORT).show();
            }
        });
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
            mViewModel.updateUser(
                    binding.etProfileFirstName.getText().toString().trim(),
                    binding.etProfileLastName.getText().toString().trim(),
                    null,
                    binding.etProfileEmail.getText().toString().trim(),
                    binding.etDescription.getText().toString().trim());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}