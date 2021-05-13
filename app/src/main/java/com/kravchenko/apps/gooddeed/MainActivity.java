package com.kravchenko.apps.gooddeed;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kravchenko.apps.gooddeed.screen.BaseFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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