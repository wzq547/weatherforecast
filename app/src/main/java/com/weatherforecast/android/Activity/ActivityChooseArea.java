package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.R;

public class ActivityChooseArea extends BaseActivity {

    private static final String TAG = "ActivityMain";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivityChooseArea.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
    }

    public void ReBackCountyNameToActivityWeather(String countyName){
        Intent intent = new Intent();
        intent.putExtra("get_county_name",countyName);
        setResult(RESULT_OK,intent);
        finish();
    }
}
