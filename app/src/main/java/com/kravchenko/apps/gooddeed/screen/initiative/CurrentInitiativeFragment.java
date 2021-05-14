package com.kravchenko.apps.gooddeed.screen.initiative;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kravchenko.apps.gooddeed.R;

import com.kravchenko.apps.gooddeed.databinding.FragmentInitiativeCurrentBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.viewmodel.InitiativeViewModel;


public class CurrentInitiativeFragment extends BaseFragment {

    private FragmentInitiativeCurrentBinding binding;
    private InitiativeViewModel initiativeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInitiativeCurrentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        
        initiativeViewModel = new ViewModelProvider(requireActivity()).get(InitiativeViewModel.class);
        //binding.cvLocationChoice.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_currentInitiativeFragment_to_mainFragment));

        //TODO here we need to get id of current initiative from database and put it into Bundle
        Bundle args = new Bundle();
        String initiativeId = "TODO"; //TODO
        args.putString("initiative_id",initiativeId);
        binding.btnSendProposeHelp.setOnClickListener(v -> getNavController().navigate(R.id.action_currentInitiativeFragment_to_currentChatFragment,args));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.initiative_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_initiative:
                getNavController().navigate(R.id.action_currentInitiativeFragment_to_editInitiativeFragment);
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
    public void clear() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}