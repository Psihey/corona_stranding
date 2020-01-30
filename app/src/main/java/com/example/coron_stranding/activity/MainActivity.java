package com.example.coron_stranding.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coron_stranding.MyLocationListener;
import com.example.coron_stranding.R;
import com.example.coron_stranding.SharedPref;
import com.example.coron_stranding.event.DataSendEvent;
import com.example.coron_stranding.model.AttributeComparator;
import com.example.coron_stranding.model.AttributesModel;
import com.example.coron_stranding.model.Example;
import com.example.coron_stranding.model.Feature;
import com.example.coron_stranding.util.DistanceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.textd)
    TextView textView;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerEventBus();
        ButterKnife.bind(this);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataGet(DataSendEvent event) {
        List<AttributesModel> features = new ArrayList<>();
        Example example = event.getExample();
        for (Feature feature : example.getFeatures()) {
            Double lat = feature.getAttributes().getLat();
            Double lon = feature.getAttributes().getLong();
            String country = feature.getAttributes().getCountryRegion();
            features.add(new AttributesModel(country, lat, lon));
        }
       Double myLon = SharedPref.getInstance().getUserCoordinatesLon();
        Double myLat = SharedPref.getInstance().getUserCoordinatesLat();

        List<AttributesModel> distanceInKmWithProvince = new ArrayList<>();
        for (AttributesModel attributesModel : features) {
           double distance=  distance(attributesModel.getLat(), attributesModel.getLong(), Double.valueOf(myLat), Double.valueOf(myLon), 'K');
            distanceInKmWithProvince.add(new AttributesModel(attributesModel.getCountryRegion(), distance));
        }
        Collections.sort(distanceInKmWithProvince, new AttributeComparator());
        textView.setText("YOU DISTANCE FROM CORONA(Not beer): " + Math.round(distanceInKmWithProvince.get(0).getDistance()) + "km "+"\n"+"The Closest Province is " + distanceInKmWithProvince.get(0).getCountryRegion());

    }

private   double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }
}
