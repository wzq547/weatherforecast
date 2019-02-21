package com.weatherforecast.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weatherforecast.android.BaseActivity;
import com.weatherforecast.android.MyApplication;
import com.weatherforecast.android.R;
import com.weatherforecast.android.gson.DailyForecast;
import com.weatherforecast.android.gson.Weather;
import com.weatherforecast.android.util.HttpUtil;
import com.weatherforecast.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityWeather extends BaseActivity {

    private static final String TAG = "ActivityWeather";

    public static void actionStart(Context context,String weatherId){
        Intent intent = new Intent(context,ActivityWeather.class);
        intent.putExtra("weather_id",weatherId);
        context.startActivity(intent);
    }

    private NestedScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView backImage;
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherId;
    public DrawerLayout drawerLayout;
    private Button navigationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherLayout = (NestedScrollView) findViewById(R.id.activity_weather_nestedscrollview);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_updata_time);
        degreeText = (TextView) findViewById(R.id.now_degree_text);
        weatherInfoText = (TextView) findViewById(R.id.now_weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_linearout);
        aqiText = (TextView) findViewById(R.id.aqi_aqi_text);
        pm25Text = (TextView) findViewById(R.id.aqi_pm25_text);
        comfortText = (TextView) findViewById(R.id.suggestion_comfort_text);
        carWashText = (TextView) findViewById(R.id.suggestion_carwash_text);
        sportText = (TextView) findViewById(R.id.suggestion_sport_text);
        backImage = (ImageView) findViewById(R.id.activity_weather_imageview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_weather_swiperefreshlayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_weather_drawerlayout);
        navigationButton = (Button) findViewById(R.id.title_button);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather",null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        String bingPicString = sharedPreferences.getString("bing_pic",null);
        if (bingPicString != null){
            Glide.with(MyApplication.getContext()).load(bingPicString).into(backImage);
        }else {
            loadBingPic();
        }

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin/tech/api/weather?cityid=" + weatherId + "&key=dbd94bda62674598b468b4ab6dc46165";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),"获取天气信息失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(MyApplication.getContext(),"获取天气信息失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }
    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityname;
        String updateTime = weather.basic.updata.updataTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : weather.dailyForecastList){
            View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) findViewById(R.id.forecast_item_data_text);
            TextView infoText = (TextView) findViewById(R.id.forecast_item_info_text);
            TextView maxText = (TextView) findViewById(R.id.forecast_item_max_text);
            TextView minText = (TextView) findViewById(R.id.forecast_item_min_text);
            dateText.setText(dailyForecast.data);
            infoText.setText(dailyForecast.more.info);
            maxText.setText(dailyForecast.temperature.max);
            minText.setText(dailyForecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null ){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度： " + weather.suggestion.comfort.info;
        String carWash = "洗车指数： " + weather.suggestion.carWash.info;
        String sport = "运动建议： " + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
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
                        Glide.with(MyApplication.getContext()).load(bingPic).into(backImage);
                    }
                });
            }
        });
    }
}
