package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/25.
 */

public class MyProvince extends DataSupport {
    private int id;
    private String Province_ID;
    private String Province_CN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince_CN() {
        return Province_CN;
    }

    public void setProvince_CN(String province_CN) {
        Province_CN = province_CN;
    }

    public String getProvince_ID() {
        return Province_ID;
    }

    public void setProvince_ID(String province_ID) {
        Province_ID = province_ID;
    }
}
