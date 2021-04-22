package com.kravchenko.apps.gooddeed.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.kravchenko.apps.gooddeed.R;

// This is a wrapper class for Mutable Live Data
public class Resource<T> {


    public enum Status {
        SUCCESS,
        ERROR,
        LOADING,
        INACTIVE
    }

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public String message;

    @Nullable
    @StringRes
    public Integer messageRes;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable Object message) {
        this.status = status;
        this.data = data;
        if(message!=null) {
            if (message instanceof String) this.message = (String) message;
            else if (message instanceof Integer) this.messageRes = (Integer) message;
        }
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> error(int msgres, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msgres);
    }

    public static <T> Resource<T> loading(String msg, @Nullable T data) {
        return new Resource<>(Status.LOADING, data, msg);
    }

    public static <T> Resource<T> loading(int msgres, @Nullable T data) {
        return new Resource<>(Status.LOADING, data, msgres);
    }

    public static <T> Resource<T> inactive() {
        return new Resource<>(Status.INACTIVE, null, null);
    }

    @Nullable
    public String getMessage(Context context) {
        return message!=null && !message.isEmpty() ? message : (messageRes!=null ? context.getString(messageRes) : context.getString(R.string.default_error_msg));
    }
}
