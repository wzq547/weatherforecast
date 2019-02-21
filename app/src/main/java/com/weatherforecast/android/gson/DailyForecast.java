package com.weatherforecast.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class DailyForecast {
    public String data;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        @SerializedName("max")
        public String max;
        @SerializedName("min")
        public String min;
    }
}
