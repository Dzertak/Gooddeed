package com.kravchenko.apps.gooddeed.util;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;

@SuppressLint("RestrictedApi")
public class TextErrorRemover extends TextWatcherAdapter {
    private final TextInputLayout textInputLayout;

    public TextErrorRemover(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(@NonNull CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
        textInputLayout.setError(null);
    }
}
