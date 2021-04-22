package com.kravchenko.apps.gooddeed;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class AppInstance extends Application {

    private static WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextWeakReference = new WeakReference<>(getApplicationContext());

    }

    public static Context getAppContext(){
        return contextWeakReference.get();
    }

}
