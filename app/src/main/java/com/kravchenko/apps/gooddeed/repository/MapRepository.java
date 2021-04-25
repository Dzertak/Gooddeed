package com.kravchenko.apps.gooddeed.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapRepository {

    private ArrayList<LatLng> markersLatLng;
    private ArrayList<String> markersTitle;
    private GoogleMap googleMap;
    private final MutableLiveData<ArrayList<LatLng>> mLatLng;
    private final MutableLiveData<ArrayList<String>> mTitle;

    public MapRepository(Context context) {
        mLatLng = new MutableLiveData<>();
        mTitle = new MutableLiveData<>();
        readInitiatives();
        mLatLng.setValue(markersLatLng);
        mTitle.setValue(markersTitle);
    }

    public void search (String searchRequest) {
        String request = searchRequest;

        if (request != null || !request.equals("")) {
            if (request.equals("Odessa")){
                LatLng latLng = new LatLng(46.482952, 30.712481);
                markersLatLng.add(latLng);
                markersTitle.add("Odessa");
                mLatLng.setValue(markersLatLng);
                mTitle.setValue(markersTitle);

            }
        }
    }

    private void readInitiatives () {
        markersLatLng = new ArrayList<>();
        markersTitle = new ArrayList<>();
        LatLng odessa = new LatLng(46.482952, 30.712481);
        LatLng nikolaev = new LatLng(46.96591, 31.9974);
        markersLatLng.add(odessa);
        markersLatLng.add(nikolaev);
        markersTitle.add("Odessa");
        markersTitle.add("Nikolaev");
    }

    public MutableLiveData<ArrayList<LatLng>> getLatLng() {
        return mLatLng;
    }

    public MutableLiveData<ArrayList<String>> getTitle() {
        return mTitle;
    }

    public void addNewCoordinates (LatLng coordinates) {
        markersLatLng.add(coordinates);
        mLatLng.setValue(markersLatLng);
    }

    public void addTitle (String title) {
        markersTitle.add(title);
        mTitle.setValue(markersTitle);
    }

}
