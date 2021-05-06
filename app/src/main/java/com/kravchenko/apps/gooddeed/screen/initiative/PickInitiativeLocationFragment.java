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
    Geocoder geocoder;


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
        geocoder = new Geocoder(requireActivity());
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            //onMapClick();
        }

        binding.cvCurrentLocation.setOnClickListener(t -> {
            Bundle result = new Bundle();
            if (mMarker != null){
                result.putDouble("lat", mMarker.getPosition().latitude);
                result.putDouble("lng", mMarker.getPosition().longitude);
            }
            getParentFragmentManager().setFragmentResult(REQUEST_LOCATION_RESULT, result);
            getNavController().navigateUp();
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
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        if (mMarker != null) mMarker.remove();
                        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
        super.onDestroyView();
        mapFragment.onDestroyView();
        mMap.clear();
        binding = null;
    }


    //    private void onMapClick() {
//        mapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {
//
//            String snippet = "Some info here";
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Some title");
//            markerOptions.snippet(snippet);
//            mMarker = googleMap.addMarker(markerOptions);
//            //googleMap.clear();
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//            googleMap.addMarker(markerOptions);
//            getNavController().navigate(R.id.action_pickInitiativeLocationFragment_to_editInitiativeFragment);
//
//        }));
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(46.482952, 30.712481);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        mMap.setOnMapClickListener(newLatLng -> {
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(newLatLng.latitude, newLatLng.longitude, 1);
                Address address = addressList.get(0);
                if (address != null){
                    if (mMarker != null)
                        mMarker.remove();

                    String cityName = address.getAddressLine(0);
                    String stateName = address.getAddressLine(1);
                    String countryName = address.getAddressLine(2);
                    StringBuilder adr = new StringBuilder();
                    String snippet = address.getAdminArea();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(newLatLng);
                    markerOptions.title(cityName);
                    markerOptions.snippet(snippet);
                    mMarker = mMap.addMarker(markerOptions);
//                    adr.append(countryName);
//                    adr.append(",");
//                    adr.append(stateName);
//                    adr.append(",");
                    adr.append(cityName);
                    binding.tvChoiceCurrentLocation.setText(adr.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

//            String snippet = "Some info here";
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(newLatLng);
//            markerOptions.title("Some title");
//            markerOptions.snippet(snippet);
//            mMarker = mMap.addMarker(markerOptions);
////            //googleMap.clear();
////            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
////            mMap.addMarker(markerOptions);
//            Bundle result = new Bundle();
//            result.putDouble("lat", newLatLng.latitude);
//            result.putDouble("lng", newLatLng.longitude);
//            getParentFragmentManager().setFragmentResult(REQUEST_LOCATION_RESULT, result);
//            getNavController().navigateUp();
            //getNavController().navigate(R.id.action_pickInitiativeLocationFragment_to_editInitiativeFragment);
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
                                List<Address> addressList = null;
                                try {
                                    addressList = geocoder.getFromLocation(task.getResult().getLatitude(), task.getResult().getLongitude(), 1);
                                    Address address = addressList.get(0);
                                    if (address != null){
                                        if (mMarker != null)
                                            mMarker.remove();

                                        String cityName = address.getAddressLine(0);
                                        String stateName = address.getAddressLine(1);
                                        String countryName = address.getAddressLine(2);
                                        StringBuilder adr = new StringBuilder();
                                        String snippet = address.getAdminArea();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude()));
                                        markerOptions.title(cityName);
                                        markerOptions.snippet(snippet);
                                        mMarker = mMap.addMarker(markerOptions);
//                    adr.append(countryName);
//                    adr.append(",");
//                    adr.append(stateName);
//                    adr.append(",");
                                        adr.append(cityName);
                                        binding.tvChoiceCurrentLocation.setText(adr.toString());
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Address address = addressList.get(0);
                        if (address != null){
                            if (mMarker != null)
                                mMarker.remove();

                            String cityName = address.getAddressLine(0);
                            String stateName = address.getAddressLine(1);
                            String countryName = address.getAddressLine(2);
                            StringBuilder adr = new StringBuilder();
                            String snippet = address.getAdminArea();
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                            markerOptions.title(cityName);
                            markerOptions.snippet(snippet);
                            mMarker = mMap.addMarker(markerOptions);
//                    adr.append(countryName);
//                    adr.append(",");
//                    adr.append(stateName);
//                    adr.append(",");
                            adr.append(cityName);
                            binding.tvChoiceCurrentLocation.setText(adr.toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }
}
