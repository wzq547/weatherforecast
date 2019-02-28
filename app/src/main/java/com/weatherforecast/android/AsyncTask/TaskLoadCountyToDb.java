package com.weatherforecast.android.AsyncTask;

import android.os.AsyncTask;

import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.db.MyCounty;
import com.weatherforecast.android.util.ShowProgressDialogListener;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by wzq547 on 2019/2/28.
 */

public class TaskLoadCountyToDb extends AsyncTask<Void,Integer,Boolean> {

    private static final String TAG = "TaskLoadCountyToDb";

    private ShowProgressDialogListener listener;

    public TaskLoadCountyToDb(ShowProgressDialogListener listener){
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        int i = 0;
        try {
            File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
            BufferedReader countyBr = new BufferedReader(new FileReader(countyCsv));
            String line = "";
            while ((line = countyBr.readLine()) != null ) {
                i++;
            }
            countyBr.close();
            LogUtil.i(TAG, "doInBackground: "+1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int j = 0;
        MyCounty last = DataSupport.findLast(MyCounty.class);
        if (last == null){
            try {
                File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
                BufferedReader countybr = new BufferedReader(new FileReader(countyCsv));
                String line = "";
                while ((line = countybr.readLine()) != null) {
                    j++;
                    publishProgress(j,i);
                    String buffer[] = line.split(",");// 以逗号分隔
                    MyCounty myCounty = new MyCounty();
                    myCounty.setCounty_CN(buffer[0]);
                    myCounty.setCounty_ID(buffer[3]);
                    myCounty.setCity_CN(buffer[2]);
                    myCounty.save();
                }
                countybr.close();
                LogUtil.e(TAG, "run: countyfinish");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if ((j = last.getId()) < i){
            LogUtil.i(TAG, "onCreate: "+ j);
            final int k = j;
            try {
                File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
                BufferedReader countybr = new BufferedReader(new FileReader(countyCsv));
                String line = "";
                for (int l = k; l >= 1; l--){
                    line = countybr.readLine();
                }
                while ((line = countybr.readLine()) != null) {
                    j++;
                    publishProgress(j,i);
                    String buffer[] = line.split(",");// 以逗号分隔
                    MyCounty myCounty = new MyCounty();
                    myCounty.setCounty_CN(buffer[0]);
                    myCounty.setCounty_ID(buffer[3]);
                    myCounty.setCity_CN(buffer[2]);
                    myCounty.save();
                }
                countybr.close();
                LogUtil.e(TAG, "run: countyfinish");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int current = values[0];
        int total = values[1];
        LogUtil.i(TAG, "onProgressUpdate: "+current);
        listener.showProgressDialog(current,total);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean == true){
            listener.closeProgressDialog();
        }
    }
}
