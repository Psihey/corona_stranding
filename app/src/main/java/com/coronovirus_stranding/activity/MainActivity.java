package com.coronovirus_stranding.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.coronovirus_stranding.MyLocationListener;
import com.coronovirus_stranding.R;
import com.coronovirus_stranding.SharedPref;
import com.coronovirus_stranding.event.DataSendEvent;
import com.coronovirus_stranding.event.DataSuccessfullyEvent;
import com.coronovirus_stranding.fragment.DistanceFragment;
import com.coronovirus_stranding.fragment.InfoFragment;
import com.coronovirus_stranding.fragment.MapFragment;
import com.coronovirus_stranding.fragment.SendDataListener;
import com.coronovirus_stranding.model.AttributesModel;
import com.coronovirus_stranding.model.Example;
import com.coronovirus_stranding.model.Feature;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.coronovirus_stranding.util.DistanceUtils.distance;

public class MainActivity extends AppCompatActivity implements SendDataListener {


    private static final char KILOMETERS = 'K';
    public static final String DEATH = "death";
    public static final String CONFIRM = "confirm";
    public static final int RC_LOCATION = 88;


    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;


    private Fragment selectedFragment;
    private List<AttributesModel> distanceInKmWithProvince;
    private List<AttributesModel> features;
    private Map<String, Integer> statistic = new HashMap<>();
    private int totalDeath;
    private int totalConfirmed;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        methodRequiresTwoPermission();
        registerEventBus();
        ButterKnife.bind(this);;
        replaceFragmentWithoutBackStack(this, new DistanceFragment(), R.id.main_activity_fragment_container, DistanceFragment.class.getSimpleName());
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_distance:
                    selectedFragment = new DistanceFragment();
                    break;
                case R.id.nav_map:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.nav_info:
                    selectedFragment = new InfoFragment();
                    break;
            }
            if (selectedFragment != null) {
                String tag = selectedFragment.getClass().getSimpleName();
                replaceFragmentWithoutBackStack(this, selectedFragment, R.id.main_activity_fragment_container, tag);
            }
            return true;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataGet(DataSendEvent event) {
        features = new ArrayList<>();
        Example example = event.getExample();
        totalDeath = 0;
        totalConfirmed = 0;
        for (Feature feature : example.getFeatures()) {
            totalDeath += feature.getAttributes().getDeaths();
            totalConfirmed += feature.getAttributes().getConfirmed();
            Double lat = feature.getAttributes().getLat();
            Double lon = feature.getAttributes().getLong();
            String country = feature.getAttributes().getCountryRegion();
            features.add(new AttributesModel(country, lat, lon, feature.getAttributes().getConfirmed(), feature.getAttributes().getDeaths(), feature.getAttributes().getRecovered()));
        }
        double myLon = SharedPref.getInstance().getUserCoordinatesLon();
        double myLat = SharedPref.getInstance().getUserCoordinatesLat();

        distanceInKmWithProvince = new ArrayList<>();
        for (AttributesModel attributesModel : features) {
            double distance = distance(attributesModel.getLat(), attributesModel.getLong(), myLat, myLon, KILOMETERS);
            distanceInKmWithProvince.add(new AttributesModel(attributesModel.getCountryRegion(), distance));
        }
        EventBus.getDefault().post(new DataSuccessfullyEvent());
    }

    private void replaceFragmentWithoutBackStack(@NonNull FragmentActivity activity, @NonNull Fragment fragment, int containerId, String tag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();
    }

    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public List<AttributesModel> getData() {
        return distanceInKmWithProvince;
    }

    @Override
    public List<AttributesModel> getCoordinates() {
        return features;
    }

    @Override
    public Map<String, Integer> getStatistic() {
        statistic.put(DEATH, totalDeath);
        statistic.put(CONFIRM, totalConfirmed);
        return statistic;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void startScan() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.app_name),
                    RC_LOCATION, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
