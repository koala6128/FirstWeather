package com.example.lx.firstweather.model;

/**
 * Created by koala on 2016/6/9.
 */
public class Weather {

    private int id;
    private String weather_code;
    private String weather_des;
    private String weather_icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }

    public String getWeather_des() {
        return weather_des;
    }

    public void setWeather_des(String weather_des) {
        this.weather_des = weather_des;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }
}
