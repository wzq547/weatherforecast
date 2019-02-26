package com.weatherforecast.android.util;

import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.db.MyCity;
import com.weatherforecast.android.db.MyCounty;
import com.weatherforecast.android.db.MyProvince;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;
import static com.weatherforecast.android.R.raw.city;
import static com.weatherforecast.android.R.raw.county;
import static com.weatherforecast.android.R.raw.province;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class Utility {

    private static final String TAG = "Utility";

    /**
     * 移动Csv文件至/data/data/com.weatherforecast.android/files/目录下
     */
    public static void moveCsvFiles(){
        if (!new File("/data/data/com.weatherforecast.android/files/" + "province.csv").exists()){
            LogUtil.w(TAG, "onCreate: not exists");
            try {
                InputStream inputStream = MyApplication.getContext().getResources().openRawResource(province);
                FileOutputStream fileOutputStream =  MyApplication.getContext().openFileOutput("province.csv",MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int count = 0;
                int i = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    i++;
                    fileOutputStream.write(buffer, 0, count);
                    LogUtil.e(TAG, "onCreate: success");
                }
                LogUtil.i(TAG, "moveCsvFiles: province"+i+" "+count);
                inputStream.close();
                fileOutputStream.close();
            }catch (FileNotFoundException e){
                LogUtil.e(TAG, "onCreate: FileNotFoundException");
                e.printStackTrace();
            }catch (IOException e){
                LogUtil.e(TAG, "onCreate: IOException");
                e.printStackTrace();
            }
        }
        if (!new File("/data/data/com.weatherforecast.android/files/" + "city.csv").exists()){
            LogUtil.w(TAG, "onCreate: not exists");
            try {
                InputStream inputStream = MyApplication.getContext().getResources().openRawResource(city);
                FileOutputStream fileOutputStream =  MyApplication.getContext().openFileOutput("city.csv",MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int count = 0;
                int i = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    i++;
                    fileOutputStream.write(buffer, 0, count);
                    LogUtil.e(TAG, "onCreate: success");
                }
                LogUtil.i(TAG, "moveCsvFiles: province"+i+" "+count);
                inputStream.close();
                fileOutputStream.close();
            }catch (FileNotFoundException e){
                LogUtil.e(TAG, "onCreate: FileNotFoundException");
                e.printStackTrace();
            }catch (IOException e){
                LogUtil.e(TAG, "onCreate: IOException");
                e.printStackTrace();
            }
        }
        if (!new File("/data/data/com.weatherforecast.android/files/" + "county.csv").exists()){
            LogUtil.w(TAG, "onCreate: not exists");
            try {
                InputStream inputStream = MyApplication.getContext().getResources().openRawResource(county);
                FileOutputStream fileOutputStream =  MyApplication.getContext().openFileOutput("county.csv",MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int count = 0;
                int i = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    i++;
                    fileOutputStream.write(buffer, 0, count);
                    LogUtil.e(TAG, "onCreate: success");
                }
                LogUtil.i(TAG, "moveCsvFiles: province"+i+" "+count);
                inputStream.close();
                fileOutputStream.close();
            }catch (FileNotFoundException e){
                LogUtil.e(TAG, "onCreate: FileNotFoundException");
                e.printStackTrace();
            }catch (IOException e){
                LogUtil.e(TAG, "onCreate: IOException");
                e.printStackTrace();
            }
        }
    }
    /**
     * 向Myprovince、Mycity表中写数据
     */
    public static void writeMyChinaProvinceAndCityToDb(){
        int iProvince = 0;
        try {
            File provinceCsv = new File("/data/data/com.weatherforecast.android/files/" + "province.csv"); // CSV文件路径
            BufferedReader provinceBr = new BufferedReader(new FileReader(provinceCsv));
            String line = "";
            while ((line = provinceBr.readLine()) != null ) {
                iProvince++;
            }
            provinceBr.close();
            LogUtil.e(TAG, "run: provincereadfinish"+ iProvince);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int jProvince = 0;
        MyProvince lastProvince = DataSupport.findLast(MyProvince.class);
        if (lastProvince == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File provinceCsv = new File("/data/data/com.weatherforecast.android/files/" + "province.csv"); // CSV文件路径
                        LogUtil.e(TAG, "readMonDataCsv: " + provinceCsv);
                        BufferedReader provincebr = new BufferedReader(new FileReader(provinceCsv));
                        String line = "";
                        while ((line = provincebr.readLine()) != null ) {
                            String buffer[] = line.split(",");// 以逗号分隔
                            MyProvince myProvince = new MyProvince();
                            myProvince.setProvince_CN(buffer[1]);
                            myProvince.setProvince_ID(buffer[3]);
                            myProvince.save();
                        }
                        provincebr.close();
                        LogUtil.e(TAG, "run: provincefinish");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else if ((jProvince = lastProvince.getId()) < iProvince){
            LogUtil.i(TAG, "onCreate: "+ jProvince);
            final int kProvince = jProvince;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File provinceCsv = new File("/data/data/com.weatherforecast.android/files/" + "province.csv"); // CSV文件路径
                        BufferedReader provincebr = new BufferedReader(new FileReader(provinceCsv));
                        String line = "";
                        for (int i = kProvince; i >= 1; i--){
                            line = provincebr.readLine();
                        }
                        while ((line = provincebr.readLine()) != null) {
                            String buffer[] = line.split(",");// 以逗号分隔
                            MyCounty myCounty = new MyCounty();
                            myCounty.setCounty_CN(buffer[0]);
                            myCounty.setCounty_ID(buffer[3]);
                            myCounty.setCity_CN(buffer[2]);
                            myCounty.save();
                        }
                        provincebr.close();
                        LogUtil.e(TAG, "run: province2finish");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        int iCity = 0;
        try {
            File cityCsv = new File("/data/data/com.weatherforecast.android/files/" + "city.csv"); // CSV文件路径
            BufferedReader cityBr = new BufferedReader(new FileReader(cityCsv));
            String line = "";
            while ((line = cityBr.readLine()) != null ) {
                iCity++;
            }
            cityBr.close();
            LogUtil.e(TAG, "run: cityreadfinish"+ iCity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int jCity = 0;
        MyCity lastCity = DataSupport.findLast(MyCity.class);
        if (lastCity == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        File cityCsv = new File("/data/data/com.weatherforecast.android/files/" + "city.csv"); // CSV文件路径
                        BufferedReader citybr = new BufferedReader(new FileReader(cityCsv));
                        String line = "";
                        while ((line = citybr.readLine()) != null ) {
                            String buffer[] = line.split(",");// 以逗号分隔
                            MyCity myCity = new MyCity();
                            myCity.setCity_CN(buffer[2]);
                            myCity.setCity_ID(buffer[3]);
                            myCity.setProvince_CN(buffer[1]);
                            myCity.save();
                        }
                        citybr.close();
                        LogUtil.e(TAG, "run: cityfinish");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }else if ((jCity = lastCity.getId()) < iCity){
            LogUtil.i(TAG, "onCreate: "+ jCity);
            final int kCity = jCity;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File cityCsv = new File("/data/data/com.weatherforecast.android/files/" + "city.csv"); // CSV文件路径
                        BufferedReader citybr = new BufferedReader(new FileReader(cityCsv));
                        String line = "";
                        for (int i = kCity; i >= 1; i--){
                            line = citybr.readLine();
                        }
                        while ((line = citybr.readLine()) != null) {
                            String buffer[] = line.split(",");// 以逗号分隔
                            MyCounty myCounty = new MyCounty();
                            myCounty.setCounty_CN(buffer[0]);
                            myCounty.setCounty_ID(buffer[3]);
                            myCounty.setCity_CN(buffer[2]);
                            myCounty.save();
                        }
                        citybr.close();
                        LogUtil.e(TAG, "run: cityfinish");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    /**
     * 向Mycounty表中写数据
     */
    public static void writeMyChinaCountyToDb() {
        int i = 0;
        try {
            File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
            BufferedReader countyBr = new BufferedReader(new FileReader(countyCsv));
            String line = "";
            while ((line = countyBr.readLine()) != null ) {
                i++;
            }
            countyBr.close();
            LogUtil.e(TAG, "run: provincefinish"+ i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int j = 0;
        MyCounty last = DataSupport.findLast(MyCounty.class);
        if (last == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
                        BufferedReader countybr = new BufferedReader(new FileReader(countyCsv));
                        String line = "";
                        while ((line = countybr.readLine()) != null) {
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
            }).start();
        }else if ((j = last.getId()) < i){
            LogUtil.i(TAG, "onCreate: "+ j);
            final int k = j;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File countyCsv = new File("/data/data/com.weatherforecast.android/files/" + "county.csv"); // CSV文件路径
                        BufferedReader countybr = new BufferedReader(new FileReader(countyCsv));
                        String line = "";
                        for (int i = k; i >= 1; i--){
                            line = countybr.readLine();
                        }
                        while ((line = countybr.readLine()) != null) {
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
            }).start();
        }
    }
}
