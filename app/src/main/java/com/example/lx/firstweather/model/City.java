package com.example.lx.firstweather.model;

/**
 * Created by koala on 2016/6/9.
 */
public class City {

    private int id;
    private String city_name;
    private String city_code;
    private String province;
    private String lat;
    private String lon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
