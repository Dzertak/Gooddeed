package com.kravchenko.apps.gooddeed.util;

import com.kravchenko.apps.gooddeed.AppInstance;

public class Utils {
    public static String getString(int code) {
        return AppInstance.getAppContext().getString(code);
    }
}
