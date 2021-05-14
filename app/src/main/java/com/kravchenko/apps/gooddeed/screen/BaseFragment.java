package com.kravchenko.apps.gooddeed.screen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kravchenko.apps.gooddeed.screen.dialog.ProgressDialogFragment;

public abstract class BaseFragment extends Fragment {
    private NavController navController;
    private ProgressDialogFragment progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        progressDialog = new ProgressDialogFragment();
        progressDialog.setCancelable(false);
    }

    public NavController getNavController() {
        return navController;
    }

    public void showProgressDialog() {
        progressDialog.show(getParentFragmentManager(), ProgressDialogFragment.TAG);
    }

    public void hideProgressDialog() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
    }
}
