package com.weatherforecast.android.util;

/**
 * Created by wzq547 on 2019/2/28.
 */

public interface ShowProgressDialogListener {
    void initProgressDialog(int total);
    void showProgressDialog(int current);
    void closeProgressDialog();
}
