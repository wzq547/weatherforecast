package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.R;

public class AAActivity extends BaseActivity {

    private static final String TAG = "AAActivity";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,AAActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aa);

        TextView textView = (TextView) findViewById(R.id.activity_aa_text);
        textView.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
