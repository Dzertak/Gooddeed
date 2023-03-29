package com.kravchenko.apps.gooddeed.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kravchenko.apps.gooddeed.AppInstance;

public class SharedPreferencesManager {
    private static final String PACKAGE_NAME = "com.kravchenko.apps.gooddeed.util";
    private static final String PREF_KEY = PACKAGE_NAME + ".appSetting";

    private final SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager() {
        sharedPreferences = AppInstance.getAppContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public static SharedPreferencesManager getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesManager();
        }
        return instance;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

}
