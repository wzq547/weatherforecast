package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int provinceId() {
        return provinceId;
    }

    public void setProvinceCode(int provinceId) {
        this.provinceId = provinceId;
    }
}
