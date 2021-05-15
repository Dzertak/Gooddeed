package com.kravchenko.apps.gooddeed.util.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kravchenko.apps.gooddeed.R;


public class DisconnectWarningFragment extends DialogFragment {
    public static final String TAG = "DisconnectWarningFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disconnect_warning, container, false);
    }

    public DisconnectWarningFragment(){}

    public static DisconnectWarningFragment getInstance(){
        return new DisconnectWarningFragment();
    }

}