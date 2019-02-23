package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.R;

public class ActivityChooseArea extends BaseActivity {

    private static final String TAG = "ActivityMain";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivityChooseArea.class);
        context.startActivity(intent);
    }

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_main_floatingactionbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void ReBackCountyNameToActivityWeather(String countyName){
        Intent intent = new Intent();
        intent.putExtra("get_county_name",countyName);
        setResult(RESULT_OK,intent);
        finish();
    }
}
