package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;

public class ActivityMain extends BaseActivity {

    private static final String TAG = "ActivityMain";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivityMain.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if (sharedPreferences.getString("weather",null) != null){
            Intent intent = new Intent(this,ActivityWeather.class);
            startActivity(intent);
            finish();
        }
    }
}
