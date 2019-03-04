package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class ActivitySetting extends BaseActivity {

    private static final String TAG = "ActivitySetting";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivitySetting.class);
        context.startActivity(intent);
    }

    private Button backbutton;
    private Button testbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backbutton = (Button) findViewById(R.id.activity_setting_title_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        testbutton = (Button) findViewById(R.id.activity_setting_title_test);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeWeather.getAirNow(MyApplication.getContext(), "beijing", new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.i(TAG, "onError: ");
                    }

                    @Override
                    public void onSuccess(List<AirNow> list) {
                        LogUtil.i(TAG, "onSuccess: " + list.size());
                    }
                });
            }
        });
    }
}
