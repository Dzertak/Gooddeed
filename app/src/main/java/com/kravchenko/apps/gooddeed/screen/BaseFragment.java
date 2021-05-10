package com.kravchenko.apps.gooddeed.screen;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kravchenko.apps.gooddeed.R;

public abstract class BaseFragment extends Fragment {
    private NavController navController;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.updating_data));
        progressDialog.setCancelable(false);
    }

    public NavController getNavController() {
        return navController;
    }

    public void showNetworkProgressDialog() {
        progressDialog.show();
    }

    public void hideNetworkProgressDialog() {
        progressDialog.dismiss();
    }
}
