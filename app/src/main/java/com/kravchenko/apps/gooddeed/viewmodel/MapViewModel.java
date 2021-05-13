package com.kravchenko.apps.gooddeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.kravchenko.apps.gooddeed.repository.CategoryRepository;
import com.kravchenko.apps.gooddeed.repository.MapRepository;

import java.util.ArrayList;

public class MapViewModel extends AndroidViewModel {

    private final MapRepository mMapRepository;
    private final CategoryRepository categoryRepository;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mMapRepository = new MapRepository(application);
        categoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<Boolean> getIsBackPressed() {
        return categoryRepository.getIsBackPressed();
    }

    public void setIsBackPressed(boolean isBackPressed) {
        this.categoryRepository.setIsBackPressed(isBackPressed);
    }
    public MutableLiveData<Boolean> getIsDrawerOpen() {
        return categoryRepository.getIsDrawerOpen();
    }

    public void setIsDrawerOpen(boolean isDrawerOpen) {
        categoryRepository.setIsDrawerOpen(isDrawerOpen);
    }
    public void searchFunction(String searchRequest) {
        mMapRepository.search(searchRequest);
    }

    public void newCoordinates(LatLng latLngs) {
        mMapRepository.addNewCoordinates(latLngs);
    }

    public void newTitle(String newTitle) {
        mMapRepository.addTitle(newTitle);
    }

    public void signOutUser() {
        mMapRepository.signOutUser();
    }

    public LiveData<ArrayList<LatLng>> getLatLng() {
        return mMapRepository.getLatLng();
    }

    public LiveData<ArrayList<String>> getTitle() {
        return mMapRepository.getTitle();
    }

}
