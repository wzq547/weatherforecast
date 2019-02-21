package com.weatherforecast.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.weatherforecast.android.Activity.ActivityMain;

/**
 * Created by wzq547 on 2019/1/30.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private ForceOfflineReceiver receiver;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG,getClass().getSimpleName());
        CollectorActivity.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.wzq547.allexample.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CollectorActivity.removeActivity(this);
    }

    //广播接收器
    class ForceOfflineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("您被强制下线，请重新登录");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CollectorActivity.finishAll();
                    ActivityMain.actionStart(MyApplication.getContext());
                }
            });
            builder.show();
        }
    }
}
