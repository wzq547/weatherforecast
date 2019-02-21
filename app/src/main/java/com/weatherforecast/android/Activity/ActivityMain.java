package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.weatherforecast.android.R;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivityMain.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
