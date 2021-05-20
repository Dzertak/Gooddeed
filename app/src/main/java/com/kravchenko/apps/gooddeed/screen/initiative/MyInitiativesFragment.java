package com.kravchenko.apps.gooddeed.screen.initiative;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

        ArrayList<Initiative> creatorInitiatives = new ArrayList<>();
        mCreatorAdapter = new InitiativeRecyclerAdapter(creatorInitiatives, requireContext());
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(userSnapshot -> {
            ArrayList<String> initiativesCreated = (ArrayList<String>) userSnapshot.get("initiativesCreated");
            if (initiativesCreated != null) {
                for (String initiativeId : initiativesCreated) {
                    FirebaseFirestore.getInstance().collection("initiatives").document(initiativeId)
                            .get().addOnSuccessListener(initiativeSnapshot -> {
                        creatorInitiatives.add(initiativeSnapshot.toObject(Initiative.class));
                        binding.viewPager.setAdapter(new ViewPagerAdapter(
                                new InitiativeRecyclerAdapter[]{mCreatorAdapter, mDoerAdapter}, requireContext()));
                    });
                }
            }
        });

        ArrayList<Initiative> doerInitiatives = new ArrayList<>();
        //TODO
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

//        mDoerAdapter.setListener((v, position) ->
//                new AlertDialog.Builder(requireContext())
//                        .setTitle(mDoerAdapter.getInitiatives().get(position).getTitle())
//                        .setMessage(mDoerAdapter.getInitiatives().get(position).getDescription())
//                        .setCancelable(true)
//                        .create().show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void clear() {

    }
}
