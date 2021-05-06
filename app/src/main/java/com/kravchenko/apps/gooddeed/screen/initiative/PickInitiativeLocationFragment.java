package com.kravchenko.apps.gooddeed.screen.initiative;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentMainBinding;
import com.kravchenko.apps.gooddeed.databinding.FragmentPickInitiativeLocationBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.util.AppConstants;
import com.kravchenko.apps.gooddeed.util.LocationUtil;

import java.io.IOException;
import java.util.List;

public class PickInitiativeLocationFragment extends BaseFragment implements OnMapReadyCallback{

    public static final String REQUEST_LOCATION_RESULT = "request-location-result";
    private final static int REQUEST_CODE = 101;
    private static final int REQUEST_SWITCH_ON_GPS = 3331;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient fusedLocation;
    private FragmentPickInitiativeLocationBinding binding;
    private Geocoder geocoder;
    private String locationName;
    private LatLng latLngCurrent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPickInitiativeLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        geocoder = new Geocoder(requireContext());
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            //onMapClick();
        }

        binding.cvCurrentLocation.setOnClickListener(t -> {
            if (latLngCurrent == null || locationName == null){
                Toast.makeText(requireContext(), "Please, select location. We can use search field for input your city", Toast.LENGTH_SHORT).show();
            } else {
                Bundle result = new Bundle();
                result.putDouble("lat", latLngCurrent.latitude);
                result.putDouble("lng", latLngCurrent.longitude);
                result.putString("location", locationName);
                getParentFragmentManager().setFragmentResult(REQUEST_LOCATION_RESULT, result);
                getNavController().navigateUp();
            }

        });
        binding.icGps.setOnClickListener(v -> getCurrentLocation());
        //onMapClick();
        binding.inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = binding.inputSearch.getQuery().toString();
                if (location != null && !location.isEmpty()) {
                    try {
                        Geocoder geocoder = new Geocoder(requireActivity());
                        List<Address> addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        latLngCurrent= new LatLng(address.getLatitude(), address.getLongitude());
                        locationName = address.getAddressLine(0);
                        binding.tvChoiceCurrentLocation.setText(locationName);
                        if (mMarker != null) mMarker.remove();
                        mMarker = mMap.addMarker(new MarkerOptions().position(latLngCurrent).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 10));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext());
        getLastLocation();
    }

    @Override
    public void onDestroyView() {
        if (mMap!=null) mMap.clear();
        if (mapFragment != null) mapFragment.onDestroy();
        if (mMarker != null) mMarker.remove();
        mapFragment = null;
        mMarker = null;
        binding = null;
        super.onDestroyView();
    }

    private void geocodeCoordinates(){
        try {
            List<Address> addressList = geocoder.getFromLocation(latLngCurrent.latitude, latLngCurrent.longitude, 1);
            Address address = addressList.get(0);
            if (address != null){
                if (mMarker != null)
                    mMarker.remove();
                locationName = address.getAddressLine(0);
                mMarker = mMap.addMarker(new MarkerOptions().position(latLngCurrent).title(locationName));
                binding.tvChoiceCurrentLocation.setText(locationName);
                binding.inputSearch.setQuery(locationName, false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        latLngCurrent = AppConstants.ODESSA_COORDINATES;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 11));
        geocodeCoordinates();


        mMap.setOnMapClickListener(newLatLng -> {
            latLngCurrent = newLatLng;
            geocodeCoordinates();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SWITCH_ON_GPS){
            LocationManager manager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE );
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (statusOfGPS){
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE );
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!statusOfGPS){
                LocationUtil.buildAlertMessageNoGps(this, REQUEST_SWITCH_ON_GPS);
            } else {
                fusedLocation.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                        .addOnCompleteListener(task -> {
                            if (task != null && task.getResult()!=null){
//                                LocationUtil.moveToCurrentLocation(new LatLng(task.getResult().getLatitude(),
//                                        task.getResult().getLongitude()), mMap, 18);
                                latLngCurrent = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                                geocodeCoordinates();
                            }
                        });
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocation.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    //LocationUtil.moveToCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()), mMap, 11);
                    latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                    geocodeCoordinates();
                }
            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }
}
