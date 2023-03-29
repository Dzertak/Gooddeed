package com.kravchenko.apps.gooddeed.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String convertToDisplayTime(long time){
        Date d = new Date(time);
        DateFormat f = new SimpleDateFormat("HH:mm  dd-MMM-yyyy", Locale.getDefault());
        return f.format(d);
    }
}
