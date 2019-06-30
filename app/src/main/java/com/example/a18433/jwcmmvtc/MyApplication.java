package com.example.a18433.jwcmmvtc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.example.a18433.jwcmmvtc.Service.cookieService;

public class MyApplication extends Application {
    private static Context context;
    private Intent intent;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        intent = new Intent(this, cookieService.class);
        startService(intent);
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i("applo", "onTerminate: ");
        startService(intent);
    }
}
