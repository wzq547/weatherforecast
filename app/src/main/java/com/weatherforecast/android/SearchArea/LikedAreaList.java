package com.weatherforecast.android.SearchArea;

/**
 * Created by wzq547 on 2019/2/27.
 */

public class LikedAreaList {
    private String Cid;
    private String Admin_area;
    private String Parent_city;
    private String Location;

    public LikedAreaList(String cid,String admin_area,String parent_city,String location){
        this.Cid = cid;
        this.Admin_area = admin_area;
        this.Parent_city = parent_city;
        this.Location = location;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getAdmin_area() {
        return Admin_area;
    }

    public void setAdmin_area(String admin_area) {
        Admin_area = admin_area;
    }

    public String getParent_city() {
        return Parent_city;
    }

    public void setParent_city(String parent_city) {
        Parent_city = parent_city;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
