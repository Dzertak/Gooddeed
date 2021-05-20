package com.kravchenko.apps.gooddeed.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.kravchenko.apps.gooddeed.AppInstance;
import com.kravchenko.apps.gooddeed.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Map;

public class Utils {
    public final static long SEVEN_DAYS_IN_MILLISECONDS = 604_800_000L;
    public final static long MONTH_IN_MILLISECONDS = 2592000000L;

    public static String getString(String code) {
        String result = "";
        Map<String, Integer> stringKeys = FillHelper.getStringKeys();
        for (String key : stringKeys.keySet()) {
            if (code.equals(key)) {
                result = getString(stringKeys.get(key));
            }
        }
        return result;
    }

    public static String getString(int code) {
        return AppInstance.getAppContext().getString(code);
    }

    public static String getString(long code) {
        return AppInstance.getAppContext().getString((int) code);
    }

    public static MaterialDatePicker<Pair<Long, Long>> createMaterialDatePicker() {

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText(R.string.data_picker_title);
        builder.setCalendarConstraints(limitRange().build());
        builder.setSelection(new Pair<>(today, today + SEVEN_DAYS_IN_MILLISECONDS));
        return builder.build();
    }

    public static String getDateRange() {
        String[] months = new DateFormatSymbols().getMonths();
        long currentDateTime = System.currentTimeMillis();
        long futureDataTime = currentDateTime + SEVEN_DAYS_IN_MILLISECONDS;


        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(currentDateTime);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        String currentMonth = months[currentCalendar.get(Calendar.MONTH)];

        Calendar futureCalendar = Calendar.getInstance();
        futureCalendar.setTimeInMillis(futureDataTime);
        int futureDay = futureCalendar.get(Calendar.DAY_OF_MONTH);
        String futureMonth = months[futureCalendar.get(Calendar.MONTH)];

        return currentDay + " " + currentMonth + " - " + futureDay + " " + futureMonth;
    }

    @DrawableRes
    public static int getIconForCategory(long id) {
        if (id > 0 && id < 5)
            return R.drawable.ic_category_beauty_and_health;
        if (id > 4 && id < 11)
            return R.drawable.ic_category_repair_and_construction;
        if (id == 11 || id == 12)
            return R.drawable.ic_category_cleaning;
        if (id > 12 && id < 18)
            return R.drawable.ic_category_tutoring_and_training;
        if (id > 17 && id < 20)
            return R.drawable.ic_category_charity;
        if (id > 19 && id < 23)
            return R.drawable.ic_category_delivery_and_transportation;
        if (id > 22 && id < 25)
            return R.drawable.ic_category_photo_and_video;
        // default icon
        return R.drawable.ic_check_black;
    }

    private static CalendarConstraints.Builder limitRange() {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        long currentDateTime = System.currentTimeMillis();

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(currentDateTime);

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(currentDateTime + (MONTH_IN_MILLISECONDS * 6));


        int year = Calendar.getInstance().get(Calendar.YEAR);
        int startMonth = Calendar.getInstance().get(Calendar.MONTH);
        int startDate = Calendar.getInstance().get(Calendar.DATE);

        calendarStart.set(year, startMonth, startDate - 1);


        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();


        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new RangeValidator(minDate, maxDate));

        return constraintsBuilderRange;
    }

    static class RangeValidator implements CalendarConstraints.DateValidator {

        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Parcelable.Creator<RangeValidator> CREATOR = new Parcelable.Creator<RangeValidator>() {

            @Override
            public RangeValidator createFromParcel(Parcel parcel) {
                return new RangeValidator(parcel);
            }

            @Override
            public RangeValidator[] newArray(int size) {
                return new RangeValidator[size];
            }
        };


    }
}
