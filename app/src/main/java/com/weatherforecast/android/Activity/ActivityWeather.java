package com.weatherforecast.android.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.CollectorActivity;
import com.weatherforecast.android.LogUtil;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;
import com.weatherforecast.android.db.MyProvince;
import com.weatherforecast.android.service.AutoUpdateService;
import com.weatherforecast.android.util.HttpUtil;
import com.weatherforecast.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
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
     * 百度定位客户端
     */
    public LocationClient mLocationClient = null;
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

//    private LinearLayout forecastHourlyLayout;
    /**
     *主页未来三天天气预报框图
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
    private NavigationView navigationView;
    /**
     *悬浮按钮控件
     */
    private FloatingActionButton floatingActionButton;
    /**
     *地址
     */
    public String mlocation = null;
    public String nlocation = null;
    /**
     * 是否第一次启动
     */
    private boolean writeProvinceAndCityIsFirst;

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
        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_weather);

        final View view = getWindow().getDecorView().findViewById(R.id.activity_weather);
        /**
         *标题栏ToolBar
         */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_weather_collapsingtoolbatlayout);
        collapsingToolbarLayout.setTitle("");
        toolbar = (Toolbar) findViewById(R.id.activity_weather_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        }
        toolBarImageView = (ImageView) findViewById(R.id.activity_weather_toobarimageview);
        Glide.with(MyApplication.getContext()).load(R.drawable.bg).into(toolBarImageView);
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

//        forecastHourlyLayout = (LinearLayout) findViewById(R.id.forecast_hourly_linearout);
        /**
         *主页未来三天天气预报框图
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
                    requestLocation();
                    LogUtil.i(TAG, "onRefresh requestLocation: ");
                }
                LogUtil.w(TAG, "onRefresh: " + mlocation);
                requestWeather(mlocation);
            }
        });
        /**
         *左划的抽屉控件、抽屉里的导航控件
         */
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_weather_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.activity_weather_navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.weather_navigation_menu_item1:
//                        AAActivity.actionStart(MyApplication.getContext());
                        break;
                    case R.id.weather_navigation_menu_item2:
                        break;
                    case R.id.weather_navigation_menu_item3:
                        break;
                    case R.id.weather_navigation_menu_item4:
                        break;
                    case R.id.weather_navigation_menu_item5:
                        break;
                    case R.id.weather_navigation_menu_item6:
                        break;
                    case R.id.weather_navigation_menu_item7:
                        break;
                    case R.id.weather_navigation_menu_item8:
                        break;
                    case R.id.weather_navigation_menu_item9:
                        break;
                    case R.id.weather_navigation_menu_item10:
                        break;
                    case R.id.weather_navigation_menu_item11:
                        break;
                    case R.id.weather_navigation_menu_item12:
                        break;
                    case R.id.weather_navigation_menu_item13:
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /**
         *悬浮按钮控件
         */
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_weather_floatingactionbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlocation != nlocation){
                    mlocation = nlocation;
                    requestWeather(mlocation);
                }else {
                    swipeRefreshLayout.setRefreshing(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(2000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    requestWeather(mlocation);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        writeProvinceAndCityIsFirst = preferences.getBoolean("writeProvinceAndCityIsFirst",true);
        LogUtil.w(TAG, "onCreate: " + writeProvinceAndCityIsFirst);
        if (writeProvinceAndCityIsFirst && DataSupport.findLast(MyProvince.class) == null){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("writeProvinceAndCityIsFirst",false);
            editor.apply();
            Utility.moveCsvFiles();
            Utility.writeMyChinaProvinceAndCityToDb();
        }

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(ActivityWeather.this,permissions,1);
        }else {
            LogUtil.i(TAG, "onCreate:asdfasdfaf ");
            initLocationOption();
            requestLocation();

//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String bingPicString = preferences.getString("bing_pic",null);
            if (bingPicString != null){
                Glide.with(MyApplication.getContext()).load(bingPicString).into(backgroundImage);
            }else {
                loadBingPic();
            }

            LogUtil.i(TAG, "onCreate: " + mlocation);
            requestWeather(mlocation);
        }
    }

    @Override
    protected void onStart() {
        LogUtil.w(TAG, "onStart: " + mlocation);
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
        mLocationClient.stop();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else if(mlocation != nlocation){
            mlocation = nlocation;
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

        }else {
            HeWeather.getWeather(MyApplication.getContext(), countyName, new HeWeather.OnResultWeatherDataListBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),"获取天气信息失败，aaa请检查网络连接",Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                            mLocationClient.stop();
                        }
                    });
                }

                @Override
                public void onSuccess(List<interfaces.heweather.com.interfacesmodule.bean.weather.Weather> list) {
//                collapsingToolbarLayout.setTitle(countyName);
                    final List<interfaces.heweather.com.interfacesmodule.bean.weather.Weather> weatherList = list;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weatherList.size() > 0){
                                mLocationClient.stop();
                                showWeatherInfo(weatherList.get(0));
                                swipeRefreshLayout.setRefreshing(false);
                            }else {
                                mLocationClient.stop();
                                Toast.makeText(MyApplication.getContext(),"获取天气信息失败，ddd请检查网络连接",Toast.LENGTH_SHORT).show();
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
        suggestionText.setText(mWeather.getLifestyle().get(1).getTxt());
//      weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(MyApplication.getContext(), AutoUpdateService.class);
        startService(intent);
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
                            ActivityWeather.actionStart(ActivityWeather.this);
                            return;
                        }
                    }
                    requestLocation();
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
     * 初始化定位选项
     */
    private void initLocationOption() {
        LocationClientOption locationOption = new LocationClientOption();
//        locationOption.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        locationOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
//        locationOption.setCoorType("bd0911");
//        locationOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
//        locationOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        locationOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        locationOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        locationOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        locationOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
//        locationOption.setIsNeedAltitude(true);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
//        locationOption.setOpenAutoNotifyMode();//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
//        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        mLocationClient.setLocOption(locationOption);
    }

    /**
     * 获取定位信息
     */
    private void requestLocation(){
        mLocationClient.start();
    }
    /**
     * 百度定位信息监听器
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
//            mCountyName = location.getDistrict();
            mlocation = location.getLatitude() + "," + location.getLongitude();
            nlocation = mlocation;
            LogUtil.w(TAG, "onReceiveLocation: " + mlocation);
            requestWeather(mlocation);
        }
    }
}
