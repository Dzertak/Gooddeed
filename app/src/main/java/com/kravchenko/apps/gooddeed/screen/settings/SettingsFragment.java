package com.kravchenko.apps.gooddeed.screen.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceFragmentCompat;

import com.kravchenko.apps.gooddeed.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String PREFERENCE_EMAIL_KEY = "email_pref";
    private static final String PREFERENCE_PASSWORD_KEY = "password_pref";
    private static final String PREFERENCE_SUBSCRIPTIONS_KEY = "subscriptions_pref";
    private static final String PREFERENCE_SYNCHRONIZATIONS_KEY = "synchronization_pref";
    private static final String PREFERENCE_NOTIFICATIONS_KEY = "notifications_pref";
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar_settings);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);

        findPreference(PREFERENCE_PASSWORD_KEY).setOnPreferenceClickListener(p -> {
            navController.navigate(R.id.action_settingsFragment_to_passwordSettingsFragment);
            return true;
        });

        findPreference(PREFERENCE_EMAIL_KEY).setOnPreferenceClickListener(p -> {
            navController.navigate(R.id.action_settingsFragment_to_emailSettingFragment);
            return true;
        });
        findPreference(PREFERENCE_SUBSCRIPTIONS_KEY).setOnPreferenceClickListener(p -> {
            navController.navigate(R.id.action_settingsFragment_to_subscriptionsSettingsFragment);
            return true;
        });
        findPreference(PREFERENCE_SYNCHRONIZATIONS_KEY).setOnPreferenceClickListener(p -> {
            navController.navigate(R.id.action_settingsFragment_to_synchronizationSettingsFragment);
            return true;
        });
        findPreference(PREFERENCE_NOTIFICATIONS_KEY).setOnPreferenceClickListener(p -> {
            //TODO handle button click
            return true;
        });
    }
}