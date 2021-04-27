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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentProfileBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.subscription.SubscriptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        //for test
        List<String> subscriptions = new ArrayList<>();
        subscriptions.add("Уборка територий");
        subscriptions.add("Массаж");
        subscriptions.add("Ремонт техники");
        subscriptions.add("Иностранные языки");
        subscriptions.add("Услуги психолога или психотерапевта");
        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(requireContext(), subscriptions);
        //

        binding.recyclerViewSubscriptions.setAdapter(subscriptionAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.recyclerViewSubscriptions.setLayoutManager(layoutManager);


//        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
//        ratingBar.setRating(4.3f);
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