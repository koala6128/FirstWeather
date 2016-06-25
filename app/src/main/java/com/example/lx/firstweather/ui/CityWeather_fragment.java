package com.example.lx.firstweather.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lx.firstweather.R;
import com.example.lx.firstweather.db.FirstWeatherDB;
import com.example.lx.firstweather.model.City;
import com.example.lx.firstweather.model.Weather;
import com.example.lx.firstweather.model.WeatherInfo;
import com.example.lx.firstweather.tools.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by LX on 2016/6/7.
 */
public class CityWeather_fragment extends Fragment {

    private FirstWeatherDB firstWeatherDB;
    private WeatherInfo weatherInfo;
    private static String ARG_NAME = "arg_name";
    private String CITY_NAME;
    private String CITY_CODE;
    private String WEATHER_ICON;

    private boolean CurrentWeatherStatus = false;
    private boolean ForecastStatus = false;
    private boolean WeatherImageStatus = false;
    private int flag = 0;

    String KEY = "0b2a5de562144ab481b4e4a02adfe4f3";
    String CITY_URL = "https://api.heweather.com/x3/citylist?search=";
    String CITY_TYPE = "allchina";
    String WEATHER_URL = "https://api.heweather.com/x3/condition?search=";
    String WEATHER_TYPE = "allcond";
    String address = "https://api.heweather.com/x3/weather?cityid=";

    //实时天气
    @BindView(R.id.position_value) TextView city_name;
    @BindView(R.id.temp_value) TextView now_temp;
    @BindView(R.id.weather_value) TextView now_weather;
    @BindView(R.id.wind_name) TextView now_wind_name;
    @BindView(R.id.wind_value) TextView now_wind_value;
    @BindView(R.id.wet_value) TextView now_wet;
    @BindView(R.id.air_value) TextView aqi_aqi;
    //今天的天气情况
    @BindView(R.id.today_image) ImageView today_image;
    @BindView(R.id.today_weather_des) TextView today_weather_des;
    @BindView(R.id.today_weather_high_temp) TextView today_high;
    @BindView(R.id.today_weather_low_temp) TextView today_low;
    //明天的天气情况
    @BindView(R.id.tomorrow_image) ImageView tomorrow_image;
    @BindView(R.id.tomorrow_weather_des) TextView tomorrow_weather_des;
    @BindView(R.id.tomorrow_weather_high_temp) TextView tomorrow_high;
    @BindView(R.id.tomorrow_weather_low_temp) TextView tomorrow_low;
    //后天的天气情况
    @BindView(R.id.after_tomorrow_image) ImageView after_image;
    @BindView(R.id.after_tomorrow_weather_des) TextView after_weather_des;
    @BindView(R.id.after_tomorrow_weather_high_temp) TextView after_high;
    @BindView(R.id.after_tomorrow_weather_low_temp) TextView after_low;
    //下拉刷新布局
    @BindView(R.id.ptr_frame_layout) PtrFrameLayout ptrFrameLayout;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.city_weather_fragment, container, false);
        ButterKnife.bind(this, view);
       // CITY_NAME = "无锡";
        CITY_NAME = getArguments().getString(ARG_NAME);
        city_name.setText(CITY_NAME);


        firstWeatherDB = FirstWeatherDB.getInstance(getActivity().getApplicationContext());
        weatherInfo = new WeatherInfo();

        Log.d("TEST", "CITY NAME is: " + CITY_NAME);
        Log.d("TEST", "begin load city");
        CITY_CODE = firstWeatherDB.loadCitybyName(CITY_NAME).getCity_code();
        if (CITY_CODE != null){
            Log.d("TEST", "city In DB");
            queryWeatherInfo();

        }else {
            Log.d("TEST", "city From Server");
            queryCity();
            queryWeatherInfo();
        }

        Log.d("TEST", "CODE-1 is: " + CITY_CODE);

        MaterialHeader header = new MaterialHeader(getActivity());
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                queryWeatherInfo();

                frame.refreshComplete();
            }
        });

        return view;
    }

    public static CityWeather_fragment create(String pass_name){
        CityWeather_fragment fragment = new CityWeather_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, pass_name);
        fragment.setArguments(args);
        return fragment;
    }

    public CityWeather_fragment(){}

    //从网络查询城市信息
    public void queryCity(){
        String url = CITY_URL + CITY_TYPE + "&key=" + KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status") .equals("ok")){
                        JSONArray city_info = response.getJSONArray("city_info");
                        for (int i = 0; i < city_info.length(); i++) {
                            JSONObject jsonObject = city_info.getJSONObject(i);
                            if (jsonObject.getString("city") .equals(CITY_NAME)){
                                City city = new City();
                                city.setCity_name(jsonObject.getString("city"));
                                city.setCity_code(jsonObject.getString("id"));
                                city.setLat(jsonObject.getString("lat"));
                                city.setLon(jsonObject.getString("lon"));
                                city.setProvince(jsonObject.getString("prov"));
                                firstWeatherDB.saveCity(city);
                                CITY_CODE = jsonObject.getString("id");
                                Log.d("TEST", "query_city_ok");
                                Log.d("TEST", "CODE-2 is: " + CITY_CODE);
                                break;
                            }
                        }
                    }else {
                        Log.d("TEST", "city status is NOT OK");
                    }
                }catch (Exception e){
                    Log.d("TEST", "query_weather_EXCEPTION");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TEST", "query_city_ERROR");
            }
        });

        Log.d("TEST", "request finish");

        request.setTag("query city");
        Singleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    //从网络查询各类天气
    public void queryWeather(final String weather_code){
        String url = WEATHER_URL + WEATHER_TYPE + "&key=" + KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status .equals("ok")){
                        Log.d("TEST", "weather status is OK");
                        JSONArray cond_info = response.getJSONArray("cond_info");
                        for (int i = 0; i < cond_info.length(); i++){
                            JSONObject object = cond_info.getJSONObject(i);
                            if(object.getString("code") .equals(weather_code)){
                                Weather weather = new Weather();
                                weather.setWeather_code(object.getString("code"));
                                weather.setWeather_des(object.getString("txt"));
                                weather.setWeather_icon(object.getString("icon"));
                                firstWeatherDB.saveWeather(weather);
                                WEATHER_ICON = object.getString("icon");
                                Log.d("TEST", "query_weather_ok");
                            }
                        }

                    }else {
                        Log.d("TEST", "weather status is NOT OK");
                    }
                }catch (Exception e){
                    Log.d("TEST", "query_weather_EXCEPTION");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TEST", "query_weather_ERROR");
            }
        });
        request.setTag("query weather");
        Singleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    //查询指定城市的天气信息
    public void queryWeatherInfo(){
        String TAG = "Query Weather Info";
        String url = address + CITY_CODE + "&key=" + KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("HeWeather data service 3.0");
                    //解析天气信息
                    weatherInfo.setAqi(jsonArray.getJSONObject(0).getJSONObject("aqi"));
                    weatherInfo.setNow(jsonArray.getJSONObject(0).getJSONObject("now"));
                    weatherInfo.setDaily_forecasts(jsonArray.getJSONObject(0).getJSONArray("daily_forecast"));

                    setCurrentWeather(weatherInfo);     //加载当前天气信息
                    setForecast(weatherInfo);           //加载天气预报
                    setWeatherImage(weatherInfo);       //加载天气图片

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
        Singleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);

    }

    public void setCurrentWeather(WeatherInfo weatherInfo){
        //Log.d("TEST", "now temp is: " + weatherInfo.getNow().getTmp());
        now_temp.setText(weatherInfo.getNow().getTmp());
        now_weather.setText(weatherInfo.getNow().getTxt());
        now_wind_name.setText(weatherInfo.getNow().getDir());
        now_wind_value.setText(weatherInfo.getNow().getSc() + "级");
        now_wet.setText(weatherInfo.getNow().getHum() + "%");
        aqi_aqi.setText(weatherInfo.getAqi().getQlty());
        CurrentWeatherStatus = true;
    }

    public void setForecast(WeatherInfo weatherInfo){

        today_weather_des.setText(weatherInfo.getDaily_forecasts()[0].getTxt_d());
        today_high.setText(weatherInfo.getDaily_forecasts()[0].getMax());
        today_low.setText(weatherInfo.getDaily_forecasts()[0].getMin());

        tomorrow_weather_des.setText(weatherInfo.getDaily_forecasts()[1].getTxt_d());
        tomorrow_high.setText(weatherInfo.getDaily_forecasts()[1].getMax());
        tomorrow_low.setText(weatherInfo.getDaily_forecasts()[1].getMin());

        after_weather_des.setText(weatherInfo.getDaily_forecasts()[2].getTxt_d());
        after_high.setText(weatherInfo.getDaily_forecasts()[2].getMax());
        after_low.setText(weatherInfo.getDaily_forecasts()[2].getMin());

        ForecastStatus = true;
    }

    public void setWeatherImage(WeatherInfo weatherInfo){

        String todayWeatherCode;
        String tomorrowWeatherCode;
        String aftertomorrowWeatherCode;

        todayWeatherCode = weatherInfo.getDaily_forecasts()[0].getCode_d();
        tomorrowWeatherCode = weatherInfo.getDaily_forecasts()[1].getCode_d();
        aftertomorrowWeatherCode = weatherInfo.getDaily_forecasts()[2].getCode_d();

        setImage(todayWeatherCode, today_image);
        setImage(tomorrowWeatherCode, tomorrow_image);
        setImage(aftertomorrowWeatherCode, after_image);

    }

    public void setImage(String code, final ImageView imageView){
        ImageRequest imageRequest;
        String icon = firstWeatherDB.loadWeatherbyCode(code).getWeather_icon();
        if (icon != null){
            Log.d("TEST", "icon In DB");
            imageRequest = new ImageRequest(icon, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);

                }
            }, 0, 0, null, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TEST", "request image ERROR");
                }
            });
        }else {
            Log.d("TEST", "icon from server");
            queryWeather(code);
            imageRequest = new ImageRequest(WEATHER_ICON, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);

                }
            }, 0, 0, null, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TEST", "request image ERROR");
                }
            });
        }
        Singleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(imageRequest);
    }
}
