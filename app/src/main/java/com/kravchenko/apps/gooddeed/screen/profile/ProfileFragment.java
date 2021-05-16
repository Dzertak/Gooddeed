package com.kravchenko.apps.gooddeed.screen.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Review;
import com.kravchenko.apps.gooddeed.databinding.FragmentProfileBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.profile.ReviewAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.subscription.SubscriptionAdapter;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void clear() {
        requireActivity().getViewModelStore().clear();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        binding.toolbar.setNavigationOnClickListener(t -> {
            clear();
            getNavController().navigateUp();
        });


        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(requireContext(), false);

        binding.recyclerViewSubscriptions.setAdapter(subscriptionAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.recyclerViewSubscriptions.setLayoutManager(layoutManager);

        mViewModel.getUser().observe(getViewLifecycleOwner(), fireStoreUser -> {
            if (fireStoreUser.status.equals(Resource.Status.SUCCESS)) {
                if (fireStoreUser.data.getFirstName() != null
                        && fireStoreUser.data.getLastName() != null
                        && !TextUtils.isEmpty(fireStoreUser.data.getFirstName())
                        && !TextUtils.isEmpty(fireStoreUser.data.getLastName())) {
                    String nameAndSurname = fireStoreUser.data.getFirstName() + " " + fireStoreUser.data.getLastName();
                    binding.toolbar.setTitle(nameAndSurname);

                } else if (fireStoreUser.data.getFirstName() != null
                        && !TextUtils.isEmpty(fireStoreUser.data.getFirstName())) {
                    binding.toolbar.setTitle(fireStoreUser.data.getFirstName());

                } else if (fireStoreUser.data.getLastName() != null
                        && !TextUtils.isEmpty(fireStoreUser.data.getLastName())) {
                    binding.toolbar.setTitle(fireStoreUser.data.getLastName());
                }
                if (fireStoreUser.data.getDescription() != null) {
                    binding.tvAbout.setText(fireStoreUser.data.getDescription());
                }
                if (fireStoreUser.data.getSubscriptions() != null) {
                    //TODO ADD categories
                    List<Long> categoryIds = fireStoreUser.data.getSubscriptions();
                    mViewModel.getSubscriptionsByIds(categoryIds)
                            .observe(getViewLifecycleOwner(), categories -> {
                                subscriptionAdapter.setCategories(categories);
                                mViewModel.getCategoryTypesWithCategoriesLiveData()
                                        .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                                            mViewModel.initProfileSelectedCategoriesLiveData(categoryTypesWithCategories, categories);
                                        });
                            });
                }
                Glide.with(this)
                        .load(fireStoreUser.data.getImageUrl())
                        .fallback(R.drawable.no_photo)
                        .into(binding.imageViewProfileAvatar);
                binding.reviewRatingBar.setRating(Float.parseFloat(fireStoreUser.data.getRate()));
                binding.tvProfileRating.setText(getString(R.string.title_rating, Float.parseFloat(fireStoreUser.data.getRate())));
            } else if (fireStoreUser.status.equals(Resource.Status.LOADING)) {
                Toast.makeText(requireContext(), fireStoreUser.message, Toast.LENGTH_SHORT).show();
            } else if (fireStoreUser.status.equals(Resource.Status.ERROR)) {
                Toast.makeText(requireContext(), fireStoreUser.message, Toast.LENGTH_SHORT).show();
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .collection("ratings").get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<Review> reviewArrayList = new ArrayList<>();
                for (DocumentSnapshot reviewDoc : queryDocumentSnapshots.getDocuments()) {
                    Review review = reviewDoc.toObject(Review.class);
                    reviewArrayList.add(review);
                }
                binding.recyclerReviews.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerReviews.setAdapter(new ReviewAdapter(reviewArrayList));
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                getNavController().navigate(R.id.action_accountFragment_to_editAccountFragment);
                break;
            case (R.id.suggest):
                //TODO
                break;
            case R.id.share:
                //TODO
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}