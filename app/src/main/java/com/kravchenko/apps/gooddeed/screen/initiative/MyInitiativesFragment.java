package com.kravchenko.apps.gooddeed.screen.initiative;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.tabs.TabLayoutMediator;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.FragmentMyInitiativesBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.myinitiatives.InitiativeRecyclerAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.myinitiatives.ViewPagerAdapter;

import java.util.ArrayList;

public class MyInitiativesFragment extends BaseFragment {

    private FragmentMyInitiativesBinding binding;
    private InitiativeRecyclerAdapter mCreatorAdapter;
    private InitiativeRecyclerAdapter mDoerAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyInitiativesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        // For test
        ArrayList<Initiative> creatorInitiatives = new ArrayList<>();
        creatorInitiatives.add(new Initiative("Creator title 1", "Creator description 1", null));
        creatorInitiatives.add(new Initiative("Creator title 2", "Creator description 2", null));
        creatorInitiatives.add(new Initiative("Creator title 3", "Creator description 3", null));
        creatorInitiatives.add(new Initiative("Creator title 4", "Creator description 4", null));
        creatorInitiatives.add(new Initiative("Creator title 5", "Creator description 5", null));
        creatorInitiatives.add(new Initiative("Creator title 6", "Creator description 6", null));
        mCreatorAdapter = new InitiativeRecyclerAdapter(creatorInitiatives, requireContext());

        ArrayList<Initiative> doerInitiatives = new ArrayList<>();
        doerInitiatives.add(new Initiative("Doer title 1", "Doer description 1", null));
        doerInitiatives.add(new Initiative("Doer title 2", "Doer description 2", null));
        doerInitiatives.add(new Initiative("Doer title 3", "Doer description 3", null));
        doerInitiatives.add(new Initiative("Doer title 4", "Doer description 4", null));
        doerInitiatives.add(new Initiative("Doer title 5", "Doer description 5", null));
        mDoerAdapter = new InitiativeRecyclerAdapter(doerInitiatives, requireContext());

        binding.viewPager.setAdapter(new ViewPagerAdapter(
                new InitiativeRecyclerAdapter[]{mCreatorAdapter, mDoerAdapter}, requireContext()));

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.im_creator));
                    break;
                case 1:
                    tab.setText(getString(R.string.im_doer));
            }
        }).attach();

        mCreatorAdapter.setListener((v, position) ->
                new AlertDialog.Builder(requireContext())
                        .setTitle(mCreatorAdapter.getInitiatives().get(position).getTitle())
                        .setMessage(mCreatorAdapter.getInitiatives().get(position).getDescription())
                        .setCancelable(true)
                        .create().show());

        mDoerAdapter.setListener((v, position) ->
                new AlertDialog.Builder(requireContext())
                        .setTitle(mDoerAdapter.getInitiatives().get(position).getTitle())
                        .setMessage(mDoerAdapter.getInitiatives().get(position).getDescription())
                        .setCancelable(true)
                        .create().show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
