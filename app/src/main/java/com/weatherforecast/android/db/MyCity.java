package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/25.
 */

public class MyCity extends DataSupport {
    private int id;
    private String  City_ID;
    private String  City_CN;
    private String  Province_CN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_CN() {
        return City_CN;
    }

    public void setCity_CN(String city_CN) {
        City_CN = city_CN;
    }

    public String getCity_ID() {
        return City_ID;
    }

    public void setCity_ID(String city_ID) {
        City_ID = city_ID;
    }

    public String getProvince_CN() {
        return Province_CN;
    }

    public void setProvince_CN(String province_CN) {
        Province_CN = province_CN;
    }
}
