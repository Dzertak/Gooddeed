package com.kravchenko.apps.gooddeed.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.kravchenko.apps.gooddeed.DialogManager;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.FragmentMainBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.iniativemap.InitiativeMapAdapter;
import com.kravchenko.apps.gooddeed.util.AppConstants;
import com.kravchenko.apps.gooddeed.util.FilterDrawerListener;
import com.kravchenko.apps.gooddeed.util.LocationUtil;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.SnapOnScrollListener;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.util.dialog.ProgressDialogFragment;
import com.kravchenko.apps.gooddeed.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainFragment extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = "MainFragment";
    private static final int REQUEST_SWITCH_ON_GPS = 3331;
    private final static int REQUEST_CODE = 101;
    private DrawerLayout drawerLayout;
    private FragmentMainBinding binding;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocation;
    private ArrayList<LatLng> markersLatLng;
    private ArrayList<String> markersTitle;
    private MapViewModel mapViewModel;
    private GoogleMap mMap;
    private List<Initiative> mSavedInitiatives;
    private List<Marker> mMarkersOnMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboard();
        setHasOptionsMenu(true);
        mMarkersOnMap = new ArrayList<>();
    }

    @Override
    public void clear() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        InitiativeMapAdapter mapAdapter = new InitiativeMapAdapter();
        mMap = googleMap;
        LatLng latLngOdessa = AppConstants.ODESSA_COORDINATES;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOdessa, 11));

        mapViewModel.getAllInitiatives().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.status.equals(Resource.Status.SUCCESS)) {
                DialogManager.hideDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                mSavedInitiatives = listResource.data != null ? listResource.data : new ArrayList<>();
                mapAdapter.setItems(mSavedInitiatives);
                mapAdapter.notifyDataSetChanged();
                for (Initiative initiative : mSavedInitiatives) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(initiative.getLat()), Double.parseDouble(initiative.getLng())))
                            .title(initiative.getTitle())
                            .icon(getIconByCategoryId(initiative.getCategoryId(), false)));
                    if (marker != null) {
                        marker.setTag(initiative);
                        mMarkersOnMap.add(marker);
                    }
                }
            } else if (listResource.status.equals(Resource.Status.LOADING)) {
                DialogManager.showDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null)
                binding.rvInitiatives.smoothScrollToPosition(mapAdapter.getPositionByInitiativeId(((Initiative) marker.getTag()).getInitiativeId()));
            return true;
        });

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvInitiatives);
        binding.rvInitiatives.setAdapter(mapAdapter);
        AtomicReference<Marker> previousMarker = new AtomicReference<>();
        SnapOnScrollListener scrollListener = new SnapOnScrollListener(snapHelper, position -> {
            if (previousMarker.get() != null && previousMarker.get().getTag() != null) {
                previousMarker.get().setIcon(getIconByCategoryId(((Initiative) previousMarker.get().getTag()).getCategoryId(), false));
                previousMarker.get().setZIndex(0);
            }
            String initiativeId = mapAdapter.getInitiativeIdByPosition(position);
            for (Marker marker : mMarkersOnMap) {
                if (marker.getTag() != null && initiativeId.equals(((Initiative) marker.getTag()).getInitiativeId())) {
                    previousMarker.set(marker);
                    Initiative currentInitiative = (Initiative) marker.getTag();
                    marker.setIcon(getIconByCategoryId(currentInitiative.getCategoryId(), true));
                    marker.setZIndex(1);
                }
            }
        });
        mapAdapter.setListener((initiative, position) -> getNavController().navigate(MainFragmentDirections.actionMainFragmentToCurrentInitiativeFragment(initiative.getInitiativeId())));
        binding.rvInitiatives.addOnScrollListener(scrollListener);
    }

    @SuppressLint("NonConstantResourceId")
    private void buildDrawerToggle() {
        drawerLayout = (DrawerLayout) binding.getRoot();

        drawerLayout.addDrawerListener(new FilterDrawerListener(mapViewModel));
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
                    getNavController().navigate(R.id.action_mainFragment_to_loginFragment);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideKeyboard();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext());
        getLastLocation();

//        mMapAdapter.setListener((initiative, position) -> getNavController()
//        .navigate(MainFragmentDirections.actionMainFragmentToCurrentInitiativeFragment(initiative.getInitiativeId())));

//        mapViewModel.getLatLng().observe(getViewLifecycleOwner(), latLngs -> initMapWithInitiatives());
//        mapViewModel.getTitle().observe(getViewLifecycleOwner(), strings -> initMapWithInitiatives());

        binding.addInitiativeFloatingButton.setOnClickListener(v -> getNavController().navigate(R.id.action_mainFragment_to_editInitiativeFragment));
        binding.icGps.setOnClickListener(v -> getCurrentLocation());

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        buildDrawerToggle();
        mapViewModel.getIsBackPressed()
                .observe(getViewLifecycleOwner(), isBackPressed -> {
                    if (isBackPressed) {
                        drawerLayout.closeDrawer(GravityCompat.END);
                        mapViewModel.setIsBackPressed(false);
                    }
                });

//        binding.placeInfo.setOnClickListener(v -> {
//            try {
//                if (mMarker.isInfoWindowShown()) {
//                    mMarker.hideInfoWindow();
//                } else {
//                    mMarker.showInfoWindow();
//                }
//            } catch (NullPointerException e) {
//                Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
//            }
//        });


    }

    private void hideKeyboard() {
        try {
//            final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            //it's for test. Need make listener class for errors
//            FirebaseCrashlytics.getInstance().recordException(e);
        }
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
        supportMapFragment.onPause();
        binding.drawerLayout.closeDrawer(binding.navView);
    }

    @Override
    public void onStop() {
        super.onStop();
        supportMapFragment.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        supportMapFragment.onStart();
    }

    @Override
    public void onDestroyView() {
        if (mMap != null) mMap.clear();
        if (supportMapFragment != null) supportMapFragment.onDestroy();
        supportMapFragment = null;
        binding = null;
        super.onDestroyView();
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mMap!=null) mMap.clear();
//        if (supportMapFragment != null) supportMapFragment.onDestroy();
//    }

    private void initMapWithInitiatives() {
        if (markersLatLng != null && markersTitle != null) {
            for (int i = 0; i < markersLatLng.size(); i++) {
                for (int j = 0; j < markersTitle.size(); j++) {
                    mMap.addMarker(new MarkerOptions().position(markersLatLng.get(i)).title(String.valueOf(markersTitle.get(j))));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(markersLatLng.get(i)));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
//        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return true;
//            }
//        };
//        menu.findItem(R.id.search_menu).setOnActionExpandListener(onActionExpandListener);
//
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu).getActionView();
//        searchView.setQueryHint(getString(R.string.type_to_search));
//        searchView.setBackgroundColor(getResources().getColor(R.color.white));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mapViewModel.searchFunction(searchView.getQuery().toString());
//                hideKeyboard();
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SWITCH_ON_GPS) {
            LocationManager manager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (statusOfGPS) {
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!statusOfGPS) {
                LocationUtil.buildAlertMessageNoGps(this, REQUEST_SWITCH_ON_GPS);
            } else {
                fusedLocation.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                        .addOnCompleteListener(task -> {
                            if (task != null && task.getResult() != null) {
                                LocationUtil.moveToCurrentLocation(new LatLng(task.getResult().getLatitude(),
                                        task.getResult().getLongitude()), mMap, 18);
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
                    LocationUtil.moveToCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()), mMap, 11);
                }
            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    // Creates map marker from icon and map marker background
    // NOTE: Use different bounding for your vectors
    @NonNull
    private BitmapDescriptor getIconByCategoryId(long id, boolean highlightMarker) {
        Drawable background;
        if (highlightMarker) {
            background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_marker_highlighted);
        } else {
            background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_marker);
        }
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), Utils.getIconForCategory(id));
        int boundLeft = (background.getIntrinsicWidth() - vectorDrawable.getIntrinsicWidth()) / 2;
        int boundTop = (background.getIntrinsicHeight() - vectorDrawable.getIntrinsicHeight()) / 3;
        vectorDrawable.setBounds(boundLeft, boundTop, boundLeft + vectorDrawable.getIntrinsicWidth(),
                boundTop + vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}