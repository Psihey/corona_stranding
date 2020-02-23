package com.coronovirus_stranding;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class CoronSrandingApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static CoronSrandingApp coronSrandingApp;

    @Override
    public void onCreate() {
        super.onCreate();
        coronSrandingApp = this;
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static CoronSrandingApp getInstance() {
        return coronSrandingApp;
    }

}
