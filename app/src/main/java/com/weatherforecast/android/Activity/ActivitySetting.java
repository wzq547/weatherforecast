package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.weatherforecast.android.R;

public class ActivitySetting extends AppCompatActivity {

    private static final String TAG = "ActivitySetting";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivitySetting.class);
        context.startActivity(intent);
    }

    private Button backbutton;

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
    }
}
