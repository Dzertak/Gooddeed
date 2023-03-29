package com.kravchenko.apps.gooddeed.screen.initiative;

import android.annotation.SuppressLint;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentInitiativeCurrentBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;
import com.kravchenko.apps.gooddeed.viewmodel.InitiativeViewModel;

import java.text.DateFormat;
import java.util.Date;


public class CurrentInitiativeFragment extends BaseFragment {

    public static final String ARG_INITIATIVE_ID = "initiative_id";
    private FragmentInitiativeCurrentBinding binding;
    private InitiativeViewModel initiativeViewModel;
    private String initiativeId;
    private MenuItem item;

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

        if (getArguments() != null) {
            initiativeId = CurrentInitiativeFragmentArgs.fromBundle(getArguments()).getInitiativeId();
        }
        Bundle bundle = new Bundle();
        bundle.putString("initiative_id", initiativeId);
        binding.btnSendProposeHelp.setOnClickListener(v -> getNavController()
                .navigate(R.id.action_currentInitiativeFragment_to_currentChatFragment, bundle));
        if (initiativeId != null)
            FirebaseFirestore.getInstance().collection("initiatives").document(initiativeId)
                    .get().addOnSuccessListener(initiativeSnapshot -> {
                if (initiativeSnapshot.get("initiativeUserId") == FirebaseAuth.getInstance().getCurrentUser().getUid())
                    binding.btnSendProposeHelp.setVisibility(View.GONE);
                else item.setVisible(false);
                if (initiativeSnapshot.getLong("categoryId") != null) {
                    initiativeViewModel.getCategoryById(initiativeSnapshot.getLong("categoryId")).observe(getViewLifecycleOwner(),
                            category -> binding.tvCategory.setText(Utils.getString(category.getTitle())));
                }
                if (initiativeSnapshot.get("description") != null)
                    binding.etDescription.setText(String.valueOf(initiativeSnapshot.get("description")));
                if (initiativeSnapshot.get("imgUri") != null)
                    Glide.with(view).load(Uri.parse(String.valueOf(initiativeSnapshot.get("imgUri")))).circleCrop().into(binding.imgInitiative);

                if (initiativeSnapshot.get("location") != null)
                    binding.tvLocation.setText(String.valueOf(initiativeSnapshot.get("location")));
                if (initiativeSnapshot.get("title") != null)
                    binding.etInitiativeTitle.setText(String.valueOf(initiativeSnapshot.get("title")));
                if (initiativeSnapshot.get("timestamp") != null) {
                    Date date = new Date((Long) initiativeSnapshot.get("timestamp"));
                    String dateTimeText = DateFormat.getDateInstance().format(date) + " " + DateFormat.getTimeInstance().format(date);
                    binding.tvDateTime.setText(dateTimeText);
                }
                if (initiativeSnapshot.get("type") != null) {
                    String type = String.valueOf(initiativeSnapshot.get("type"));
                    switch (type) {
                        case InitiativeType.SINGLE:
                            binding.tvInitiativeType.setText(getString(R.string.single));
                            break;
                        case InitiativeType.GROUP:
                            binding.tvInitiativeType.setText(getString(R.string.group));
                            break;
                        case InitiativeType.UNLIMITED:
                            binding.tvInitiativeType.setText(getString(R.string.unlimited));
                            break;
                    }
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.initiative_menu, menu);
        item = menu.findItem(R.id.edit_initiative);
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