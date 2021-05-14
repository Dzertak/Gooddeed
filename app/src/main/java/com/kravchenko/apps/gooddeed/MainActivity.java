package com.kravchenko.apps.gooddeed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.kravchenko.apps.gooddeed.screen.DisconnectWarningFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DialogFragment dialogFragment = new DisconnectWarningFragment();

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