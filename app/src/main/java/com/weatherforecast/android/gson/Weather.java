package com.weatherforecast.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;
}
