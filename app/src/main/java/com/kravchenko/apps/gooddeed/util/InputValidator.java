package com.kravchenko.apps.gooddeed.util;

import android.content.Context;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.kravchenko.apps.gooddeed.AppInstance;
import com.kravchenko.apps.gooddeed.R;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=\\S+$)" +
                    ".{6,20}" +
                    "$");

    private final static Context context;

    static {
        context = AppInstance.getAppContext();
    }

    public static String validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            return context.getString(R.string.empty_field_explanation);
        } else {
            return null;
        }
    }

    public static String validateConfirmPassword(String confirmPassword, String password) {
        if (confirmPassword.isEmpty()) {
            return context.getString(R.string.empty_field_explanation);
        } else if (!confirmPassword.equals(password)) {
            return context.getString(R.string.password_mismatch);
        } else {
            return null;
        }
    }

    public static String validatePassword(String password) {
        if (password.isEmpty()) {
            return context.getString(R.string.empty_field_explanation);
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return context.getString(R.string.weak_password_explanation);
        } else {
            return null;
        }
    }

    public static String validateName(String name) {
        if (name.isEmpty()) {
            return context.getString(R.string.empty_field_explanation);
        } else {
            return null;
        }
    }

    public static String validateEmail(String email) {
        if (email.isEmpty()) {
            return context.getString(R.string.empty_field_explanation);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return context.getString(R.string.invalid_email_explanation);
        } else {
            return null;
        }
    }

}
