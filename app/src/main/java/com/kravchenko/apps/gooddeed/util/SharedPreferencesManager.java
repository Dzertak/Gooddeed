package com.kravchenko.apps.gooddeed.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PACKAGE_NAME = "com.kravchenko.apps.gooddeed.util";
    private static final String PREF_KEY = PACKAGE_NAME + ".appSetting";

    private final SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

}
