package com.kravchenko.apps.gooddeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;

public class PickInitiativeLocationFragment extends BaseFragment {

    private SupportMapFragment mapFragment;
    private Marker mMarker;
    private OnMapReadyCallback callback = googleMap -> {
        LatLng latLng = new LatLng(46.482952, 30.712481);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_initiative_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            onMapClick();
        }
    }

    private void onMapClick() {
        mapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {

            String snippet = "Some info here";
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Some title");
            markerOptions.snippet(snippet);
            mMarker = googleMap.addMarker(markerOptions);
            //googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            googleMap.addMarker(markerOptions);
            getNavController().navigate(R.id.action_pickInitiativeLocationFragment_to_editInitiativeFragment);

        }));
    }
}
