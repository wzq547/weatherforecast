package com.weatherforecast.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
