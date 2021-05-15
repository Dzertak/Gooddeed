package com.kravchenko.apps.gooddeed;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.kravchenko.apps.gooddeed.util.dialog.DisconnectWarningFragment;
import com.kravchenko.apps.gooddeed.util.dialog.ProgressDialogFragment;

public abstract class DialogManager {

    public static void showDialog(FragmentManager fragmentManager, String tag){
        hideDialog(fragmentManager, tag);
        if (ProgressDialogFragment.TAG.equals(tag)){
            ProgressDialogFragment.getInstance().show(fragmentManager, tag);
        } else if (DisconnectWarningFragment.TAG.equals(tag)){
            DisconnectWarningFragment.getInstance().show(fragmentManager, tag);
        }
    }

    public static void hideDialog(FragmentManager fragmentManager, String tag){
        DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(tag);
        if (dialogFragment != null) dialogFragment.dismiss();
    }
}
