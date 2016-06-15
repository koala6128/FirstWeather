package com.example.lx.firstweather.model;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lx.firstweather.R;
import com.example.lx.firstweather.model.AQI;
import com.example.lx.firstweather.model.Daily_forecast;
import com.example.lx.firstweather.model.NOW;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by koala on 2016/6/11.
 */
public class WeatherInfo {


    private AQI aqi;        //空气质量
    private NOW now;        //当前天气
    private Daily_forecast[] daily_forecasts;   //天气预报



    public void setAqi(JSONObject object) {
        AQI aqi = new AQI();
        Log.d("TEST", "saving AQI");
        try {
            aqi.setAqi(object.getJSONObject("city").getString("aqi"));
            aqi.setCo(object.getJSONObject("city").getString("co"));
            aqi.setNo2(object.getJSONObject("city").getString("no2"));
            aqi.setO3(object.getJSONObject("city").getString("o3"));
            aqi.setPm10(object.getJSONObject("city").getString("pm10"));
            aqi.setPm25(object.getJSONObject("city").getString("pm25"));
            aqi.setQlty(object.getJSONObject("city").getString("qlty"));
            aqi.setSo2(object.getJSONObject("city").getString("so2"));

        }catch (Exception e){
            e.printStackTrace();
            Log.d("TEST", "ERROR in set aqi");
        }
        this.aqi = aqi;
    }

    public void setNow(JSONObject object) {
        NOW now = new NOW();
        Log.d("TEST", "saving Now");
        try {
            now.setCode(object.getJSONObject("cond").getString("code"));
            now.setTxt(object.getJSONObject("cond").getString("txt"));
            now.setFl(object.getString("fl"));
            now.setHum(object.getString("hum"));
            now.setPcpn(object.getString("pcpn"));
            now.setPres(object.getString("pres"));
            now.setTmp(object.getString("tmp"));
            now.setVis(object.getString("vis"));
            now.setSc(object.getJSONObject("wind").getString("sc"));
            now.setDir(object.getJSONObject("wind").getString("dir"));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("TEST", "ERROR in set now");
        }
        this.now = now;
    }

    public void setDaily_forecasts(JSONArray jsonArray) {
        Daily_forecast[] daily_forecasts = new Daily_forecast[7];
        try {
            for (int i = 0; i < daily_forecasts.length; i++){
                daily_forecasts[i] = new Daily_forecast();
                Log.d("TEST", "saving daily " + i);
                daily_forecasts[i].setSr(jsonArray.getJSONObject(i).getJSONObject("astro").getString("sr"));
                daily_forecasts[i].setSs(jsonArray.getJSONObject(i).getJSONObject("astro").getString("ss"));
                daily_forecasts[i].setCode_d(jsonArray.getJSONObject(i).getJSONObject("cond").getString("code_d"));
                daily_forecasts[i].setCode_n(jsonArray.getJSONObject(i).getJSONObject("cond").getString("code_n"));
                daily_forecasts[i].setTxt_d(jsonArray.getJSONObject(i).getJSONObject("cond").getString("txt_d"));
                daily_forecasts[i].setTxt_n(jsonArray.getJSONObject(i).getJSONObject("cond").getString("txt_n"));
                daily_forecasts[i].setDate(jsonArray.getJSONObject(i).getString("date"));
                daily_forecasts[i].setHum(jsonArray.getJSONObject(i).getString("hum"));
                daily_forecasts[i].setPcpn(jsonArray.getJSONObject(i).getString("pcpn"));
                daily_forecasts[i].setMax(jsonArray.getJSONObject(i).getJSONObject("tmp").getString("max"));
                daily_forecasts[i].setMin(jsonArray.getJSONObject(i).getJSONObject("tmp").getString("min"));
                daily_forecasts[i].setVis(jsonArray.getJSONObject(i).getString("vis"));
                daily_forecasts[i].setDir(jsonArray.getJSONObject(i).getJSONObject("wind").getString("dir"));
                daily_forecasts[i].setSc(jsonArray.getJSONObject(i).getJSONObject("wind").getString("sc"));
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("TEST", "ERROR in set forecast");
        }
        this.daily_forecasts = daily_forecasts;
    }

    public AQI getAqi() {
        return aqi;
    }

    public NOW getNow() {
        return now;
    }

    public Daily_forecast[] getDaily_forecasts() {
        return daily_forecasts;
    }
}
