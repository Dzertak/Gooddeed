package com.kravchenko.apps.gooddeed;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.kravchenko.apps.gooddeed.screen.dialog.DisconnectWarningFragment;
import com.kravchenko.apps.gooddeed.screen.dialog.ProgressDialogFragment;
import com.kravchenko.apps.gooddeed.util.ConnectionLiveData;

import static com.kravchenko.apps.gooddeed.screen.dialog.DisconnectWarningFragment.FRAGMENT_TEG;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DialogFragment dialogFragment = new DisconnectWarningFragment();

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.observe(this, isNetAvailable -> {
            if (!isNetAvailable) {
                dialogFragment.show(getSupportFragmentManager(), FRAGMENT_TEG);
            } else {
                if (dialogFragment.isAdded()) {
                    dialogFragment.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null){
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
//            if (fragment != null){
//                List<Fragment> fragments =  fragment.getChildFragmentManager().getFragments();
//                Log.i("", "");
//                if (fragments != null){
//
//                }
//            }
//
//        }
        super.onBackPressed();
    }
}