package com.example.lx.firstweather.model;

/**
 * Created by koala on 2016/6/10.
 */
public class Weather_Info {

    private AQI aqi;
    private NOW now;

    //空气质量指数
    public class AQI{
        private String qlty;    //空气质量类别
    }

    //实况天气
    public class NOW{
        private String code;    //天气代码
        private String txt;     //天气描述
        private String tmp;     //当前温度
        private String hum;     //湿度
        private String wind;    //风力
        private String dir;     //风向（方向）
    }

    //天气预报
    public class Daily_forecast{
        
    }


}
