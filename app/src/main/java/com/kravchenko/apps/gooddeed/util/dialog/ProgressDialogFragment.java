package com.kravchenko.apps.gooddeed.util.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.kravchenko.apps.gooddeed.R;


public class ProgressDialogFragment extends DialogFragment {
    public static final String TAG = "ProgressDialogFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false);
    }

    public ProgressDialogFragment(){}

    public static ProgressDialogFragment getInstance(){
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setCancelable(true);
        return progressDialogFragment;
    }

}