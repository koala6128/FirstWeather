package com.example.lx.firstweather.tools;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lx.firstweather.model.AQI;
import com.example.lx.firstweather.model.Daily_forecast;
import com.example.lx.firstweather.model.NOW;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by koala on 2016/6/11.
 */
public class QueryWeather {

    private static final String adress = "https://api.heweather.com/x3/weather?cityid=";
    private static final String key = "0b2a5de562144ab481b4e4a02adfe4f3";
    private static final String TAG = "Query Weather Info";
    //private QueryWeather queryWeather;
    private String city_code;   //待查询天气的城市代码
    private int day_number;     //预报的天数 （7天内）
    private Context mContext;
    private AQI aqi;        //空气质量
    private NOW now;        //当前天气
    private Daily_forecast[] daily_forecasts;   //天气预报

    public QueryWeather(Context context, String code, int number){
       // queryWeatherInfo(code);
        city_code = code;
        mContext = context;
        day_number = number;
    }


    public void queryWeatherInfo(){
        String url = adress + city_code + "&key=" + key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("HeWeather data service 3.0");
                    setAqi(jsonArray.getJSONObject(0).getJSONObject("aqi"));
                    setNow(jsonArray.getJSONObject(0).getJSONObject("now"));
                    setDaily_forecasts(jsonArray.getJSONObject(0).getJSONArray("daily_forecast"));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TEST", "ERROR in queryWeatherInfo");
            }
        });

        request.setTag(TAG);
        Singleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);
    }

    public void setAqi(JSONObject object) {
        AQI aqi = null;
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
        NOW now = null;
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
        Daily_forecast[] daily_forecasts = new Daily_forecast[day_number];
        try {
            for (int i = 0; i < daily_forecasts.length; i++){
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
