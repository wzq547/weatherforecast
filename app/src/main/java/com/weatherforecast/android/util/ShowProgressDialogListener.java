package com.weatherforecast.android.util;

/**
 * Created by wzq547 on 2019/2/28.
 */

public interface ShowProgressDialogListener {
    void showProgressDialog(int current,int total);
    void closeProgressDialog();
}
