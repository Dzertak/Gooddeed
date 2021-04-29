package com.kravchenko.apps.gooddeed.screen.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.content.ContextCompat;
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
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends BaseFragment {

    private static final int REQUEST_PICK_PHOTO = 100;
    private FragmentProfileEditBinding binding;
    private ProfileViewModel mViewModel;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
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
            if (firestoreUser.status.equals(Resource.Status.SUCCESS) && firestoreUser.data != null) {
                if (firestoreUser.data.getFirstName() != null && firestoreUser.data.getLastName() != null) {
                    binding.etProfileFirstName.setText(firestoreUser.data.getFirstName());
                    binding.etProfileLastName.setText(firestoreUser.data.getLastName());
                }
                if (firestoreUser.data.getDescription() != null) {
                    binding.etDescription.setText(firestoreUser.data.getDescription());
                }

                imageUri = Uri.parse(firestoreUser.data.getImageUrl());
                Glide.with(this)
                        .load(firestoreUser.data.getImageUrl())
                        .fallback(R.drawable.no_photo)
                        .into(binding.ivProfileAvatar);
                binding.etProfileEmail.setText(firestoreUser.data.getEmail());
            } else if (firestoreUser.status.equals(Resource.Status.LOADING)) {
                Toast.makeText(requireContext(), firestoreUser.message, Toast.LENGTH_SHORT).show();
            } else if (firestoreUser.status.equals(Resource.Status.ERROR)) {
                Toast.makeText(requireContext(), firestoreUser.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeProfileImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK)
                .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg"});
        startActivityForResult(pickIntent, REQUEST_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_PICK_PHOTO) {
                startCrop(data.getData());
            } else if (requestCode == UCrop.REQUEST_CROP) {
                imageUri = UCrop.getOutput(data);
                Glide.with(this)
                        .load(UCrop.getOutput(data))
                        .fallback(R.drawable.no_photo)
                        .into(binding.ivProfileAvatar);
            } else if (requestCode == UCrop.RESULT_ERROR) {
                Toast.makeText(requireContext(), UCrop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrop(Uri sourceUri) {
        Uri destinationUri;
        String destinationFileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date()) + ".jpg";
        destinationUri = Uri.fromFile(new File(requireActivity().getCacheDir(), destinationFileName));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setAspectRatioOptions(0, new AspectRatio("1:1", 1, 1));
        options.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.green));
        options.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.darkergreen));
        uCrop.withOptions(options);
        uCrop.start(requireContext(), this);
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
            if (binding.etProfileFirstName.getText() != null && binding.etProfileLastName.getText() != null
                    && binding.etProfileEmail.getText() != null && binding.etDescription.getText() != null) {
                mViewModel.updateUser(
                        binding.etProfileFirstName.getText().toString().trim(),
                        binding.etProfileLastName.getText().toString().trim(),
                        imageUri,
                        binding.etProfileEmail.getText().toString().trim(),
                        binding.etDescription.getText().toString().trim());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}