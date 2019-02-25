package com.weatherforecast.android.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weatherforecast.android.Activity.ActivityChooseArea;
import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;
import com.weatherforecast.android.db.MyCity;
import com.weatherforecast.android.db.MyCounty;
import com.weatherforecast.android.db.MyProvince;
import com.weatherforecast.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class FragmentChooseArea extends Fragment {

    private static final String TAG = "FragmentChooseArea";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private FloatingActionButton floatingActionButton;

    /**
     * 省列表
     */
    private List<MyProvince> provinceList;
    /**
     * 市列表
     */
    private List<MyCity> cityList;
    /**
     * 县列表
     */
    private List<MyCounty> countyList;
    /**
     * 选中的省份
     */
    private MyProvince selectedProvince;
    /**
     * 选中的城市
     */
    private MyCity selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;

    private boolean writeCountyIsFirst;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        writeCountyIsFirst = preferences.getBoolean("writeCountyIsFirst",true);
        if (writeCountyIsFirst){
            LogUtil.e(TAG, "onCreate: isFrist:"+ writeCountyIsFirst);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("writeCountyIsFirst",false);
            editor.apply();
            Utility.writeMyChinaCountyToDb();
        }
        LogUtil.e(TAG, "onCreate: "+ writeCountyIsFirst);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.fragment_choose_area_title_text);
        backButton = (Button) view.findViewById(R.id.fragment_choose_area_title_button);
        listView = (ListView) view.findViewById(R.id.fragment_choose_area_title_list);
        adapter = new ArrayAdapter<String>(MyApplication.getContext(),R.layout.list_item1,dataList);
        listView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_choose_area_floatingActionButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }else if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_PROVINCE){
                    getActivity().finish();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getCounty_ID();
                    if (getActivity() instanceof ActivityChooseArea){
                        LogUtil.i(TAG, "onItemClick: " + weatherId);
                        ActivityChooseArea activitychoosearea = (ActivityChooseArea) getActivity();
                        activitychoosearea.ReBackCountyNameToActivityWeather(weatherId);
                    }//else if (getActivity() instanceof ActivityWeather){
//                        ActivityWeather activityWeather = (ActivityWeather) getActivity();
//                        activityWeather.drawerLayout.closeDrawers();
//                        activityWeather.swipeRefreshLayout.setRefreshing(true);
//                        activityWeather.mlocation = weatherId;
//                        activityWeather.requestWeather(weatherId);
//                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    /**
     * 查询全国所有的省，优先从数据库查询
     */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(MyProvince.class);
        if (provinceList.size() > 0 ){
            dataList.clear();
            for (MyProvince myProvince : provinceList){
                dataList.add(myProvince.getProvince_CN());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            Toast.makeText(getActivity(),"数据库加载中，大概需要30s，请不要关闭程序",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 查询选中省内所有的省，优先从数据库查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvince_CN());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("Province_CN = ?",String.valueOf(selectedProvince.getProvince_CN())).find(MyCity.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (MyCity myCity : cityList){
                dataList.add(myCity.getCity_CN());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            Toast.makeText(getActivity(),"数据库加载中，大概需要30s，请不要关闭程序",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 查询选中市内所有的县，优先从数据库查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCity_CN());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("City_CN = ?",String.valueOf(selectedCity.getCity_CN())).find(MyCounty.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (MyCounty myCounty : countyList){
                dataList.add(myCounty.getCounty_CN());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            Toast.makeText(getActivity(),"数据库加载中，大概需要30s，请不要关闭程序",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}