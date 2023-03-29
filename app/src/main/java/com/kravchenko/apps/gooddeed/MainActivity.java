package com.kravchenko.apps.gooddeed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.kravchenko.apps.gooddeed.util.dialog.DisconnectWarningFragment;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;

import java.util.List;
import com.kravchenko.apps.gooddeed.util.ConnectionLiveData;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.observe(this, isNetAvailable -> {
            if (!isNetAvailable) {
                DialogManager.showDialog(getSupportFragmentManager(), DisconnectWarningFragment.TAG);
            } else {
                DialogManager.hideDialog(getSupportFragmentManager(), DisconnectWarningFragment.TAG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (fragment != null){
                List<Fragment> fragments =  fragment.getChildFragmentManager().getFragments();
                if (fragments != null && !fragments.isEmpty() && fragments.get(0) instanceof  BaseFragment){
                    ((BaseFragment) fragments.get(0)).clear();
//                    if (fragments.get(0) instanceof EditInitiativeFragment){
//                        ((BaseFragment) fragments.get(0)).clear();
//                    }
                }
            }
        }
        super.onBackPressed();
    }
}