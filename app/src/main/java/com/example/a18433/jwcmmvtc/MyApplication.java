package com.example.a18433.jwcmmvtc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.a18433.jwcmmvtc.Service.cookieService;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        Intent intent = new Intent(this, cookieService.class);
        startService(intent);
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

}
