package com.kravchenko.apps.gooddeed.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentMainBinding;
import com.kravchenko.apps.gooddeed.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import static com.kravchenko.apps.gooddeed.screen.LoginFragment.SIGNED_OUT_FLAG;

public class MainFragment extends BaseFragment {

    private static final String TAG = "TAG";
    private final static int REQUEST_CODE = 101;
    private final static String titleName = "MARKER";
    private FragmentMainBinding binding;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocation;
    private ArrayList<LatLng> markersLatLng;
    private ArrayList<String> markersTitle;
    private Marker mMarker;

    private MapViewModel mapViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboard();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        getCurrentLocation();
        return binding.getRoot();
    }

    private void getCurrentLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
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
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void addInitiativeButton() {
        binding.addInitiativeFloatingButton.setOnClickListener(v -> getNavController().navigate(R.id.action_mainFragment_to_editInitiativeFragment));
    }

    @SuppressLint("NonConstantResourceId")
    private void buildDrawerToggle() {
        DrawerLayout drawerLayout = (DrawerLayout) binding.getRoot();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_item:
                    getNavController().navigate(R.id.action_mainFragment_to_accountFragment);
                    break;
                case R.id.chats_item:
                    getNavController().navigate(R.id.action_mainFragment_to_chatsFragment);
                    break;
                case R.id.settings_item:
                    getNavController().navigate(R.id.action_mainFragment_to_settingsFragment);
                    break;
                case R.id.initiative_item:
                    getNavController().navigate(R.id.action_mainFragment_to_myInitiativesFragment);
                    break;
                case R.id.sign_out_item:
                    mapViewModel.signOutUser();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(SIGNED_OUT_FLAG, true);
                    getNavController().navigate(R.id.action_mainFragment_to_loginFragment, bundle);
                    break;
            }
            return true;
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getLatLng().observe(getViewLifecycleOwner(), latLngs -> initMapWithInitiatives());
        mapViewModel.getTitle().observe(getViewLifecycleOwner(), strings -> initMapWithInitiatives());

        addInitiativeButton();
        yourLocation();
        onMapClick();
        onMarkerClick();

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        buildDrawerToggle();

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


        //TODO
        // set values
        View headerLayout = binding.navView.getHeaderView(0);
        TextView username = headerLayout.findViewById(R.id.tv_username);
        ImageView userAvatar = headerLayout.findViewById(R.id.imv_ava);

    }

    private void yourLocation() {
        binding.icGps.setOnClickListener(v -> getCurrentLocation());
    }


    private void hideKeyboard() {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e){
            //it's for test. Need make listener class for errors
//            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    private void onMapClick() {
        supportMapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {

            String snippet = "Some info here";
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(titleName);
            markerOptions.snippet(snippet);
            mMarker = googleMap.addMarker(markerOptions);
            mapViewModel.newCoordinates(latLng);
            mapViewModel.newTitle(titleName);
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
    public void onPause() {
        super.onPause();
        binding.drawerLayout.closeDrawer(binding.navView);
    }

    @Override
    public void onDestroyView() {
        if (supportMapFragment != null) supportMapFragment.onDestroyView();
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        if (supportMapFragment != null) supportMapFragment.onDestroy();
        if (mMarker != null){
            mMarker.remove();
            mMarker = null;
        }
        super.onDestroy();
    }

    private void onMarkerClick() {
        supportMapFragment.getMapAsync(googleMap -> {
            googleMap.setOnMarkerClickListener(marker -> {
                getNavController().navigate(R.id.action_mainFragment_to_currentInitiativeFragment);
                return false;
            });
        });
    }

    private void initMapWithInitiatives() {
        if (markersLatLng != null && markersTitle != null) {
            supportMapFragment.getMapAsync(googleMap -> {
                for (int i = 0; i < markersLatLng.size(); i++) {
                    for (int j = 0; j < markersTitle.size(); j++) {
                        googleMap.addMarker(new MarkerOptions().position(markersLatLng.get(i)).title(String.valueOf(markersTitle.get(j))));
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(markersLatLng.get(i)));
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.search_menu).setOnActionExpandListener(onActionExpandListener);


        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu).getActionView();
        searchView.setQueryHint(getString(R.string.type_to_search));
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mapViewModel.searchFunction(searchView.getQuery().toString());
                hideKeyboard();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_button) {
            binding.drawerLayout.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

}