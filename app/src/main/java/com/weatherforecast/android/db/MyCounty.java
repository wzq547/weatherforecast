package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/25.
 */

public class MyCounty extends DataSupport {
    private int id;
    private String County_ID;
    private String County_CN;
    private String City_CN;

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
}
