package com.weatherforecast.android.gson;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
