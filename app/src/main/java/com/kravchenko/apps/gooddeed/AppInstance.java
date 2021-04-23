package com.kravchenko.apps.gooddeed;

import android.app.Application;
import android.content.Context;

import com.kravchenko.apps.gooddeed.util.SharedPreferencesManager;

import java.lang.ref.WeakReference;

public class AppInstance extends Application {

    private static WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextWeakReference = new WeakReference<>(getApplicationContext());
        SharedPreferencesManager.init(getApplicationContext());
    }

    public static Context getAppContext(){
        return contextWeakReference.get();
    }

}
