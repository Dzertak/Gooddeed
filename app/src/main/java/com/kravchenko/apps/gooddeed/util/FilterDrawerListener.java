package com.kravchenko.apps.gooddeed.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.viewmodel.MapViewModel;

public class FilterDrawerListener extends DrawerLayout.SimpleDrawerListener {

    private final MapViewModel mapViewModel;

    public FilterDrawerListener(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        if (R.id.filter_nav_host == drawerView.getId()) {
            mapViewModel.setIsNavDrawerOpen(true);
        }
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        mapViewModel.setIsNavDrawerOpen(false);
    }

}
