package com.kravchenko.apps.gooddeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.kravchenko.apps.gooddeed.repository.MapRepository;

import java.util.ArrayList;

public class MapViewModel extends AndroidViewModel {

    private final MapRepository mMapRepository;
    private MutableLiveData<Boolean> isBackPressed;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mMapRepository = new MapRepository(application);
        this.isBackPressed = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getIsBackPressed() {
        return isBackPressed;
    }

    public void setIsBackPressed(boolean isBackPressed) {
        this.isBackPressed.setValue(isBackPressed);
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
