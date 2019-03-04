package com.weatherforecast.android.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.CollectorActivity;
import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;
import com.weatherforecast.android.SearchArea.LikedAreaList;
import com.weatherforecast.android.SearchArea.LikedAreaListAdapter;
import com.weatherforecast.android.db.MyLikedCounty;
import com.weatherforecast.android.util.HttpUtil;
import com.weatherforecast.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ActivityWeather extends BaseActivity {

    private static final String TAG = "ActivityWeather";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActivityWeather.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context,String string){
        Intent intent = new Intent(context,ActivityWeather.class);
        intent.putExtra("get_county_name",string);
        context.startActivity(intent);
    }

    /**
     * 此活动的全局View
     */
    private View view;
    /**
     *标题栏ToolBar
     */
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView toolBarImageView;
    /**
     *主页背景图
     */
    private ImageView backgroundImage;
    /**
     *主页标题title
     */
    private TextView titleCity;
    /**
     *主页实时天气1
     */
    private ImageView nowimage;
    private TextView nowweatherdegreeText;
    private TextView nowweatherInfoText;
    private TextView nowweatherUpdataText;
    /**
     *主页实时天气2
     */
    private TextView now2getFi;
    private TextView now2getHum;
    private TextView now2getPcpn;
    /**
     * 主页未来24小时天气预报框图
     */
    private LinearLayout forecastHourlyLayout;
    /**
     *主页未来七天天气预报框图
     */
    private LinearLayout forecastLayout;
    /**
     *主页今日建议框图
     */
    private LinearLayout suggestionLayout;
    private TextView suggestionText;
    /**
     *主页刷新控件、左划的抽屉控件、抽屉里的导航控件
     */
    public SwipeRefreshLayout swipeRefreshLayout;
    public DrawerLayout drawerLayout;
    private ListView likedListView;
    private LikedAreaListAdapter adapter;
    private List<MyLikedCounty> myLikedCountyList;
    private List<LikedAreaList> dataList = new ArrayList<>();
    private Button settingButton;
    /**
     *悬浮按钮控件
     */
    private FloatingActionButton floatingActionButton;
    /**
     *地址
     */
    public String mlocation = null;
    public String flocation = null;
    /**
     * 返回的天气表
     */
    private List<Weather> backWeatherList;

    /**
     * 处理从上一个活动返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    mlocation = data.getStringExtra("get_county_name");
                    LogUtil.i(TAG, "onActivityResult: " + mlocation);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 创建ToolBar菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }
    //菜单按键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_toolbar_item1:
                Intent intent = new Intent(MyApplication.getContext(),ActivityChooseArea.class);
                startActivityForResult(intent,1);
                break;
            default:;break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        /**
         * 此活动的全局View
         */
        view = getWindow().getDecorView().findViewById(R.id.activity_weather);
        /**
         *标题栏ToolBar
         */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_weather_collapsingtoolbatlayout);
        toolbar = (Toolbar) findViewById(R.id.activity_weather_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        }
        /**
         *主页背景图
         */
        backgroundImage = (ImageView) findViewById(R.id.activity_weather_imageview);
        /**
         *主页标题title
         */
        titleCity = (TextView) findViewById(R.id.title_city);
        /**
         *主页实时天气1
         */
        nowimage = (ImageView) findViewById(R.id.now_weather_image);
        nowweatherdegreeText = (TextView) findViewById(R.id.now_degree_text);
        nowweatherInfoText = (TextView) findViewById(R.id.now_weather_info_text);
        nowweatherUpdataText = (TextView) findViewById(R.id.now_weather_update_text);
        /**
         *主页实时天气2
         */
        now2getFi = (TextView) findViewById(R.id.now2_getFI);
        now2getHum = (TextView) findViewById(R.id.now2_getHum);
        now2getPcpn = (TextView) findViewById(R.id.now2_getPcpn);
        /**
         *主页未来24小时天气预报框图
         */
        forecastHourlyLayout = (LinearLayout) findViewById(R.id.forecast_hourly_linearout);
        /**
         *主页未来七天天气预报框图
         */
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_linearout);
        /**
         *主页今日建议框图
         */
        suggestionLayout = (LinearLayout) findViewById(R.id.suggestion_linearout);
        suggestionText = (TextView) findViewById(R.id.suggestion_text);
        /**
         *主页刷新控件
         */
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_weather_swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mlocation == null){
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view,"还未获取定位信息，无法刷新天气信息，请检查网络后重启程序重试",Snackbar.LENGTH_LONG)
                            .setAction("重启", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CollectorActivity.finishAll();
                                    ActivityWeather.actionStart(ActivityWeather.this);
                                }
                            }).show();
                }else {
                    LogUtil.w(TAG, "onRefresh: " + mlocation);
                    requestWeather(mlocation);
                }
            }
        });
        /**
         *左划的抽屉控件&抽屉里的控件
         */
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_weather_drawerlayout);
        likedListView = (ListView) findViewById(R.id.activity_weather_drawerlayout_likedlist);
        initDataList();
        adapter = new LikedAreaListAdapter(this,R.layout.list_liked_area_adapter,dataList);
        likedListView.setAdapter(adapter);
        likedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LikedAreaList likedAreaList = dataList.get(position);
                drawerLayout.closeDrawers();
                swipeRefreshLayout.setRefreshing(true);
                mlocation = likedAreaList.getCid();
                requestWeather(mlocation);
            }
        });
        settingButton = (Button) findViewById(R.id.activity_weather_drawerlayout_Setupbutton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySetting.actionStart(ActivityWeather.this);
                drawerLayout.closeDrawers();
            }
        });
        /**
         *悬浮按钮控件
         */
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_weather_floatingactionbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backWeatherList == null){

                }else {
                    if (DataSupport.where("County_ID = ?",String.valueOf(backWeatherList.get(0).getBasic().getCid())).find(MyLikedCounty.class).isEmpty()){
                        MyLikedCounty myLikedCounty = new MyLikedCounty();
                        myLikedCounty.setProvince_CN(backWeatherList.get(0).getBasic().getAdmin_area());
                        myLikedCounty.setCity_CN(backWeatherList.get(0).getBasic().getParent_city());
                        myLikedCounty.setCounty_CN(backWeatherList.get(0).getBasic().getLocation());
                        myLikedCounty.setCounty_ID(backWeatherList.get(0).getBasic().getCid());
                        myLikedCounty.save();
                        floatingActionButton.setImageResource(R.drawable.ic_liked);
                    }else if (!DataSupport.where("County_ID = ?",String.valueOf(backWeatherList.get(0).getBasic().getCid())).find(MyLikedCounty.class).isEmpty()){
                        DataSupport.deleteAll(MyLikedCounty.class,"County_ID = ?",String.valueOf(backWeatherList.get(0).getBasic().getCid()));
                        floatingActionButton.setImageResource(R.drawable.ic_like);
                    }
                    myLikedCountyList = DataSupport.findAll(MyLikedCounty.class);
                    if (myLikedCountyList.size() > 0){
                        likedListView.setVisibility(View.VISIBLE);
                        dataList.clear();
                        for (MyLikedCounty myLikedCounty1 : myLikedCountyList){
                            dataList.add(new LikedAreaList(myLikedCounty1.getCounty_ID(),myLikedCounty1.getProvince_CN(),myLikedCounty1.getCity_CN(),myLikedCounty1.getCounty_CN()));
                        }
                        adapter.notifyDataSetChanged();
                        likedListView.setSelection(0);
                    }else if (myLikedCountyList.size() == 0){
                        likedListView.setVisibility(View.INVISIBLE);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        likedListView.setSelection(0);
                    }
                }
            }
        });

        Utility.moveCsvFiles();
        Utility.writeMyChinaProvinceAndCityToDb();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        backWeatherList = null;

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(ActivityWeather.this,permissions,1);
        }else if (connectivityManager.getActiveNetworkInfo() != null){
            HeWeather.getSearch(this, new HeWeather.OnResultSearchBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    Snackbar.make(view,"获取定位信息失败，请重试",Snackbar.LENGTH_INDEFINITE)
                            .setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CollectorActivity.finishAll();
                                    ActivityWeather.actionStart(ActivityWeather.this);
                                }
                            }).show();
                }
                @Override
                public void onSuccess(Search search) {
                    flocation = search.getBasic().get(0).getCid();
                    mlocation = search.getBasic().get(0).getCid();
                    requestWeather(mlocation);
                }
            });
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String bingPicString = preferences.getString("bing_pic",null);
            if (bingPicString != null){
                Glide.with(MyApplication.getContext()).load(bingPicString).into(backgroundImage);
            }else {
                loadBingPic();
            }
        }else {
            Snackbar.make(view,"您未连接到任何网络，请连接网络后重启程序",Snackbar.LENGTH_INDEFINITE)
                    .setAction("重启", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CollectorActivity.finishAll();
                            ActivityWeather.actionStart(ActivityWeather.this);
                        }
                    }).show();
        }
    }

    @Override
    protected void onStart() {
        requestWeather(mlocation);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else if(mlocation != flocation){
            mlocation = flocation;
            requestWeather(mlocation);
        }else{
            super.onBackPressed();
        }
    }

    /**
     * 根据城市名称请求城市天气信息
     */
    public void requestWeather(final String countyName){
        if (countyName == null){
            backWeatherList = null;
        }else {
            HeWeather.getWeather(MyApplication.getContext(), countyName, new HeWeather.OnResultWeatherDataListBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(view,"未成功获取定位，请重启程序以重试",Snackbar.LENGTH_SHORT)
                                    .setAction("重启", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CollectorActivity.finishAll();
                                            ActivityWeather.actionStart(MyApplication.getContext());
                                        }
                                    }).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

                @Override
                public void onSuccess(List<interfaces.heweather.com.interfacesmodule.bean.weather.Weather> list) {
                    backWeatherList = list;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (backWeatherList.size() > 0){
                                showWeatherInfo(backWeatherList.get(0));
                                swipeRefreshLayout.setRefreshing(false);
                            }else {
                                Toast.makeText(MyApplication.getContext(),"获取天气信息失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                }
            });
            loadBingPic();
        }
    }
    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather mWeather){
        //title
        titleCity.setText(mWeather.getBasic().getParent_city() + " " + mWeather.getBasic().getLocation());
        //now
        Glide.with(MyApplication.getContext()).load("https://cdn.heweather.com/cond_icon/" + mWeather.getNow().getCond_code() + ".png").into(nowimage);
        nowweatherdegreeText.setText(mWeather.getNow().getTmp() + "℃");
        nowweatherInfoText.setText(mWeather.getNow().getCond_txt());
        nowweatherUpdataText.setText("最后更新时间：" + mWeather.getUpdate().getLoc());
        //now2
        now2getFi.setText(mWeather.getNow().getFl() + "℃");
        now2getHum.setText(mWeather.getNow().getHum());
        now2getPcpn.setText(mWeather.getNow().getPcpn());
        //hourly_forecast
        forecastHourlyLayout.removeAllViews();
        for (HourlyBase hourlyBase : mWeather.getHourly()){
            View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.forecasthourly_item,forecastHourlyLayout,false);
            TextView getData = (TextView) view.findViewById(R.id.forecast_hourly_item_getData);
            TextView getTmp = (TextView) view.findViewById(R.id.forecast_hourly_item_getTmp);
            TextView getPop = (TextView) view.findViewById(R.id.forecast_hourly_item_getPop);
            TextView getCond_txt_d = (TextView) view.findViewById(R.id.forecast_hourly_item_getCond_txt_d);
            ImageView getCond_code_d = (ImageView) view.findViewById(R.id.forecast_hourly_item_getCond_code_d);
            getData.setText(hourlyBase.getTime());
            getTmp.setText(hourlyBase.getTmp() + "℃");
            getPop.setText(hourlyBase.getPop() + "%");
            getCond_txt_d.setText(hourlyBase.getCond_txt());
            Glide.with(MyApplication.getContext()).load("https://cdn.heweather.com/cond_icon/" + hourlyBase.getCond_code() + ".png").into(getCond_code_d);
            forecastHourlyLayout.addView(view);
        }
        //daily_forecast
        forecastLayout.removeAllViews();
        for (ForecastBase dailyForecast : mWeather.getDaily_forecast()){
            View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView getData = (TextView) view.findViewById(R.id.forecast_item_getData);
            TextView getTmp = (TextView) view.findViewById(R.id.forecast_item_getTmp);
            TextView getSr = (TextView) view.findViewById(R.id.forecast_item_getSr);
            TextView getSs = (TextView) view.findViewById(R.id.forecast_item_getSs);
            TextView getPop = (TextView) view.findViewById(R.id.forecast_item_getPop);
            TextView getCond_txt_d = (TextView) view.findViewById(R.id.forecast_item_getCond_txt_d);
            ImageView getCond_code_d = (ImageView) view.findViewById(R.id.forecast_item_getCond_code_d);
            getData.setText(dailyForecast.getDate());
            getTmp.setText(dailyForecast.getTmp_min() + "～" + dailyForecast.getTmp_max() + "℃");
            getSr.setText(dailyForecast.getSr());
            getSs.setText(dailyForecast.getSs());
            getPop.setText(dailyForecast.getPop() + "%");
            getCond_txt_d.setText(dailyForecast.getCond_txt_d());
            Glide.with(MyApplication.getContext()).load("https://cdn.heweather.com/cond_icon/" + dailyForecast.getCond_code_d() + ".png").into(getCond_code_d);
            forecastLayout.addView(view);
        }
        //suggesstion
        suggestionLayout.removeAllViews();
        for (LifestyleBase lifestyleBase: mWeather.getLifestyle()){
            View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.suggestion_item,suggestionLayout,false);
            TextView typeText = (TextView) view.findViewById(R.id.suggestion_item_type_text);
            TextView brfText = (TextView) view.findViewById(R.id.suggestion_item_brf_text);
            String getTypeText = lifestyleBase.getType();
            switch (getTypeText){
                case "comf":
                    typeText.setText("舒适度指数 : ");
                    break;
                case "cw":
                    typeText.setText("洗车指数 : ");
                    break;
                case "drsg":
                    typeText.setText("穿衣指数 : ");
                    break;
                case "flu":
                    typeText.setText("感冒指数 : ");
                    break;
                case "sport":
                    typeText.setText("运动指数 : ");
                    break;
                case "trav":
                    typeText.setText("旅游指数 : ");
                    break;
                case "uv":
                    typeText.setText("紫外线指数 : ");
                    break;
                case "air":
                    typeText.setText("空气污染扩散条件指数 : ");
                    break;
                case "ac":
                    typeText.setText("空调开启指数 : ");
                    break;
                case "ag":
                    typeText.setText("过敏指数 : ");
                    break;
                case "gl":
                    typeText.setText("太阳镜指数 : ");
                    break;
                case "mu":
                    typeText.setText("化妆指数 : ");
                    break;
                case "airc":
                    typeText.setText("晾晒指数 : ");
                    break;
                case "ptfc":
                    typeText.setText("交通指数 : ");
                    break;
                case "fsh":
                    typeText.setText("钓鱼指数 : ");
                    break;
                case "spi":
                    typeText.setText("防晒指数 : ");
                    break;
                default:
                    typeText.setText("无今日建议 : ");
                    break;
            }
            brfText.setText(lifestyleBase.getBrf());
            suggestionLayout.addView(view);
        }
        suggestionText.setText(mWeather.getLifestyle().get(0).getTxt());
        /**
         * 根据当前页面显示的地区设置悬浮按钮的图标
         */
        if (DataSupport.where("County_ID = ?",String.valueOf(backWeatherList.get(0).getBasic().getCid())).find(MyLikedCounty.class).isEmpty()){
            floatingActionButton.setImageResource(R.drawable.ic_like);
        }else if (!DataSupport.where("County_ID = ?",String.valueOf(backWeatherList.get(0).getBasic().getCid())).find(MyLikedCounty.class).isEmpty()){
            floatingActionButton.setImageResource(R.drawable.ic_liked);
        }
    }
    /**
     * 获取必应每日一图
     */
    private void loadBingPic(){
        String loadBingPicUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(loadBingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MyApplication.getContext()).load(bingPic).into(backgroundImage);
                    }
                });
            }
        });
    }
    /**
     * 获取权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MyApplication.getContext(),"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            CollectorActivity.finishAll();
                            return;
                        }
                    }
                }else {
                    Toast.makeText(MyApplication.getContext(),"发生未知错误",Toast.LENGTH_SHORT).show();
                    CollectorActivity.finishAll();
                    ActivityWeather.actionStart(ActivityWeather.this);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 初始化dataList
     */
    private void initDataList(){
        myLikedCountyList = DataSupport.findAll(MyLikedCounty.class);
        if (myLikedCountyList.size() > 0){
            likedListView.setVisibility(View.VISIBLE);
            dataList.clear();
            for (MyLikedCounty myLikedCounty1 : myLikedCountyList){
                dataList.add(new LikedAreaList(myLikedCounty1.getCounty_ID(),myLikedCounty1.getProvince_CN(),myLikedCounty1.getCity_CN(),myLikedCounty1.getCounty_CN()));
            }
            likedListView.setSelection(0);
        }else if (myLikedCountyList.size() == 0){
            likedListView.setVisibility(View.INVISIBLE);
            dataList.clear();
            likedListView.setSelection(0);
        }
    }
}
