package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/27.
 */

public class MyLikedCounty extends DataSupport {
    private int id;
    private String County_ID;
    private String County_CN;
    private String City_CN;
    private String Province_CN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCounty_CN() {
        return County_CN;
    }

    public void setCounty_CN(String county_CN) {
        County_CN = county_CN;
    }

    public String getCounty_ID() {
        return County_ID;
    }

    public void setCounty_ID(String county_ID) {
        County_ID = county_ID;
    }

    public String getCity_CN() {
        return City_CN;
    }

    public void setCity_CN(String city_CN) {
        City_CN = city_CN;
    }

    public String getProvince_CN() {
        return Province_CN;
    }

    public void setProvince_CN(String province_CN) {
        Province_CN = province_CN;
    }
}
