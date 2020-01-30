package com.example.coron_stranding;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.coron_stranding.api.Api;

public class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        double longitude = loc.getLongitude();
        double latitude = loc.getLatitude();
        SharedPref.getInstance().saveUserCoordinatesLat(latitude);
        SharedPref.getInstance().saveUserCoordinatesLon(longitude);
        Api.getAttributes();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
