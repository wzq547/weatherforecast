package com.weatherforecast.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wzq547 on 2019/2/21.
 */

public class County extends DataSupport {
    private int id;
    private String coutyName;
    private int coutyCode;
    private String weatherId;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoutyName() {
        return coutyName;
    }

    public void setCoutyName(String coutyName) {
        this.coutyName = coutyName;
    }

    public int getCoutyCode() {
        return coutyCode;
    }

    public void setCoutyCode(int coutyCode) {
        this.coutyCode = coutyCode;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
