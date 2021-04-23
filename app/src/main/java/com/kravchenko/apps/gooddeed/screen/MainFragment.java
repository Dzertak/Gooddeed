package com.kravchenko.apps.gooddeed.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarMainFragment);
        NavigationUI.setupWithNavController(binding.toolbarMainFragment, navController);
        buildDrawerToggle();

        //TODO
        // set values
        View headerLayout = binding.navView.getHeaderView(0);
        TextView username = headerLayout.findViewById(R.id.tv_username);
        ImageView userAvatar = headerLayout.findViewById(R.id.imv_ava);

    }

    @SuppressLint("NonConstantResourceId")
    private void buildDrawerToggle() {
        DrawerLayout drawerLayout = binding.getRoot();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                binding.toolbarMainFragment,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_item:
                    //TODO
                    break;
                case R.id.settings_item:
                    //TODO
                    break;
                case R.id.initiative_item:
                    //TODO
                    break;
                case R.id.sign_out_item:
                    //TODO
                    break;
            }
            return true;
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_button) {
            binding.drawerLayoutMainFragment.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }
}