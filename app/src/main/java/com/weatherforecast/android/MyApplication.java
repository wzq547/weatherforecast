package com.weatherforecast.android;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by wzq547 on 2019/2/20.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext() {
        return context;
    }
}
