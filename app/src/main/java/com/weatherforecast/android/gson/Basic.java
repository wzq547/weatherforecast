package com.weatherforecast.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class Basic {
    @SerializedName("city")
    public String cityname;
    @SerializedName("id")
    public String weatherId;
    public Updata updata;
    public class Updata{
        @SerializedName("loc")
        public String updataTime;
    }
}
