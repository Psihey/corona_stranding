package com.example.coron_stranding;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static final String PREF_NAME = "com.coron.pref";
    private static final String USER_COORDINATES_LON = "com.coron.coordinates.lon";
    private static final String USER_COORDINATES_LAT = "com.coron.coordinates.lat";
    private static SharedPref sharedPref;
    private SharedPreferences preferences;

    public static SharedPref getInstance() {
        if (sharedPref == null) {
            synchronized (SharedPref.class) {
                if (sharedPref == null) {
                    sharedPref = new SharedPref(CoronSrandingApp.getInstance());
                }
            }
        }

        return sharedPref;
    }

    private SharedPref(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public void saveUserCoordinatesLon(double lon) {
        preferences.edit().putFloat(USER_COORDINATES_LON, (float) lon).apply();
    }

    public void saveUserCoordinatesLat(double lat) {
        preferences.edit().putFloat(USER_COORDINATES_LAT, (float) lat).apply();
    }

    public double getUserCoordinatesLon() {
        return preferences.getFloat(USER_COORDINATES_LON, 0.0f);
    }

    public double getUserCoordinatesLat() {
        return preferences.getFloat(USER_COORDINATES_LAT, 0.0f);
    }

}
