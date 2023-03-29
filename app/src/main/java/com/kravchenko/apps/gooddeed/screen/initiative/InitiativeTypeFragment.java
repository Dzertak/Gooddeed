package com.kravchenko.apps.gooddeed.screen.initiative;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.FragmentInitiativeTypeBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;
import com.kravchenko.apps.gooddeed.viewmodel.InitiativeViewModel;

public class InitiativeTypeFragment extends BaseFragment {

    private FragmentInitiativeTypeBinding binding;
    private Initiative initiativeCur;
    private InitiativeViewModel initiativeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInitiativeTypeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());


        initiativeViewModel = new ViewModelProvider(requireActivity()).get(InitiativeViewModel.class);
        initiativeViewModel.getInitiative().observe(getViewLifecycleOwner(), initiative -> {
            initiativeCur = initiative != null ? initiative : new Initiative();
        });
        binding.cvSingle.setOnClickListener(t -> selectType(InitiativeType.SINGLE));
        binding.cvGroup.setOnClickListener(t -> selectType(InitiativeType.GROUP));
        binding.cvUnlimited.setOnClickListener(t -> selectType(InitiativeType.UNLIMITED));
    }

    private void selectType(@InitiativeType String type){
        if (initiativeCur == null) initiativeCur = new Initiative();
        initiativeCur.setType(type);
        initiativeViewModel.updateInitiative(initiativeCur);
        getNavController().navigateUp();
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
