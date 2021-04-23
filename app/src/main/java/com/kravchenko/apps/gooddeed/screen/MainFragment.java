package com.kravchenko.apps.gooddeed.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentMainBinding;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "TAG";
    private FragmentMainBinding binding;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocation;
    private final static int REQUEST_CODE = 101;
    private ArrayList<LatLng> markersLatLng;
    private ArrayList<String> markersTitle;
    private final static String titleName = "MARKER";
    private NavController navController;
    private Marker mMarker;
    private GoogleMap googleMap;
    private AuthViewModel authViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        readInitiatives();
        searchFunction();
        initMapWithInitiatives();
        getCurrentLocation();
        onMapClick();

        return binding.getRoot();
    }

    private void readInitiatives() {
        markersLatLng = new ArrayList<>();
        markersTitle = new ArrayList<>();
        LatLng odessa = new LatLng(46.482952, 30.712481);
        LatLng nikolaev = new LatLng(46.96591, 31.9974);
        markersLatLng.add(odessa);
        markersLatLng.add(nikolaev);
        markersTitle.add("Odessa");
        markersTitle.add("Nikolaev");
    }

    private void getCurrentLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocation.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    supportMapFragment.getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerCurrentLocation = new MarkerOptions().position(latLng).title("Im here!");
                        markerCurrentLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        googleMap.addMarker(markerCurrentLocation);

                    });
                }
            });
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void searchFunction() {
        binding.inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = binding.inputSearch.getQuery().toString();
                List<Address> addressList = new ArrayList<>();

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void buildDrawerToggle() {
        DrawerLayout drawerLayout = binding.getRoot();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                binding.toolbarMainFragment,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_item:
                    navController.navigate(R.id.action_mainFragment_to_accountFragment);
                    break;
                case R.id.settings_item:
                    navController.navigate(R.id.action_mainFragment_to_settingsFragment);
                    break;
                case R.id.initiative_item:
                    navController.navigate(R.id.action_mainFragment_to_myInitiativesFragment);
                    break;
                case R.id.sign_out_item:
                    authViewModel.signOutUser();
                    authViewModel.setIsAuth(false);
                    navController.navigate(R.id.action_mainFragment_to_loginFragment);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarMainFragment);
        NavigationUI.setupWithNavController(binding.toolbarMainFragment, navController);
        buildDrawerToggle();


        //TODO
        // set values
        View headerLayout = binding.navView.getHeaderView(0);
        TextView username = headerLayout.findViewById(R.id.tv_username);
        ImageView userAvatar = headerLayout.findViewById(R.id.imv_ava);
        binding.icGps.setOnClickListener(v -> getCurrentLocation());

        binding.placeInfo.setOnClickListener(v -> {
            try {
                if (mMarker.isInfoWindowShown()) {
                    mMarker.hideInfoWindow();
                } else {
                    mMarker.showInfoWindow();
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
            }
        });

    }

    private void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void onMapClick() {
        supportMapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {

            String snippet = "Some info here";

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(titleName);
            markerOptions.snippet(snippet);
            mMarker = googleMap.addMarker(markerOptions);
            markersLatLng.add(latLng);
            markersTitle.add(titleName);
            //googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            googleMap.addMarker(markerOptions);

        }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initMapWithInitiatives() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(googleMap -> {
            for (int i = 0; i < markersLatLng.size(); i++) {
                for (int j = 0; j < markersTitle.size(); j++) {
                    googleMap.addMarker(new MarkerOptions().position(markersLatLng.get(i)).title(String.valueOf(markersTitle.get(j))));
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(markersLatLng.get(i)));
            }
            googleMap.setOnMarkerClickListener(marker -> {
                Toast.makeText(getActivity(), "You click on marker!", Toast.LENGTH_SHORT).show();
                return false;
            });
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_button) {
            binding.drawerLayoutMainFragment.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }
}