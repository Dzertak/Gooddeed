package com.kravchenko.apps.gooddeed.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kravchenko.apps.gooddeed.R;

public class LocationUtil {

    public static void buildAlertMessageNoGps(Fragment fragment, int requestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setMessage(fragment.getString(R.string.msg_dialog_enable_gps))
                .setCancelable(false)
                .setPositiveButton(fragment.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        fragment.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), requestCode);
                    }
                })
                .setNegativeButton(fragment.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void moveToCurrentLocation(LatLng latLng, GoogleMap googleMap, float zoomValue){
        MarkerOptions markerCurrentLocation = new MarkerOptions().position(latLng).title("Im here!");
        markerCurrentLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomValue));
        googleMap.addMarker(markerCurrentLocation);
    }
}
