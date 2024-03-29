package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceFragmentCompat;

import com.kravchenko.apps.gooddeed.R;

public class SubscriptionsSettingsFragment extends PreferenceFragmentCompat {
    private static final String PREFERENCE_RADIUS_KEY = "radius_pref";
    private static final String PREFERENCE_CATEGORY_KEY = "category_pref";
    private NavController navController;
    public final static String SETTINGS_FRAGMENT_TAG = "SUBSCRIPTIONS_SETTINGS_FRAGMENT_TAG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.subscriptions_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar_settings);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);

        findPreference(PREFERENCE_RADIUS_KEY).setOnPreferenceClickListener(preference -> {
            //TODO
            return true;
        });

        findPreference(PREFERENCE_CATEGORY_KEY).setOnPreferenceClickListener(preference -> {

            NavDirections action
                    = SubscriptionsSettingsFragmentDirections.actionSubscriptionsSettingsFragmentToCategoryNavGraph(SETTINGS_FRAGMENT_TAG);
            navController.navigate(action);
            return true;
        });
    }
}