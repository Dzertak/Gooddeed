package com.kravchenko.apps.gooddeed.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kravchenko.apps.gooddeed.viewmodel.MapViewModel;

public class FilterDrawerListener implements DrawerLayout.DrawerListener {

    private final MapViewModel mapViewModel;

    public FilterDrawerListener(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        mapViewModel.setIsDrawerOpen(true);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        mapViewModel.setIsDrawerOpen(false);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
