package com.weatherforecast.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * Created by wzq547 on 2019/1/30.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG,getClass().getSimpleName());
        CollectorActivity.addActivity(this);

        HeConfig.switchToFreeServerNode();
        HeConfig.init("HE1902220140201908","642167faf9944e49a48c4fddafc1fb59");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CollectorActivity.removeActivity(this);
    }
}
