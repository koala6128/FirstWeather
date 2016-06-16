package com.example.lx.firstweather;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lx.firstweather.db.FirstWeatherDB;
import com.example.lx.firstweather.model.City;
import com.example.lx.firstweather.model.Weather;
import com.example.lx.firstweather.tools.Singleton;
import com.example.lx.firstweather.model.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.security.PublicKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirstWeatherDB firstWeatherDB;
    private WeatherInfo weatherInfo;
    //private WeatherInfo queryWeather;

    private String CITY_NAME;
    private String CITY_CODE;
    private City city;

    @BindView(R.id.temp_value) TextView now_temp;
    @BindView(R.id.weather_value) TextView now_weather;
    @BindView(R.id.wind_name) TextView now_wind_name;
    @BindView(R.id.wind_value) TextView now_wind_value;
    @BindView(R.id.wet_value) TextView now_wet;
    @BindView(R.id.air_value) TextView aqi_aqi;
    @BindView(R.id.today_image) ImageView today_image;
    @BindView(R.id.today_weather_des) TextView today_weather_des;
    @BindView(R.id.today_weather_high_temp) TextView today_high;
    @BindView(R.id.today_weather_low_temp) TextView today_low;
    @BindView(R.id.tomorrow_image) ImageView tomorrow_image;
    @BindView(R.id.tomorrow_weather_des) TextView tomorrow_weather_des;
    @BindView(R.id.tomorrow_weather_high_temp) TextView tomorrow_high;
    @BindView(R.id.tomorrow_weather_low_temp) TextView tomorrow_low;
    @BindView(R.id.after_tomorrow_image) ImageView after_image;
    @BindView(R.id.after_tomorrow_weather_des) TextView after_weather_des;
    @BindView(R.id.after_tomorrow_weather_high_temp) TextView after_high;
    @BindView(R.id.after_tomorrow_weather_low_temp) TextView after_low;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

/*        Detail_fragment detailFragment = new Detail_fragment();
        Summary_fragment summaryFragment = new Summary_fragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container0, detailFragment);
        transaction.add(R.id.fragment_container1, summaryFragment);
        transaction.commit();*/

        CITY_NAME = "无锡";
        CITY_CODE = "CN101190201";

        firstWeatherDB = FirstWeatherDB.getInstance(this);
        weatherInfo = new WeatherInfo();
        //firstWeatherDB.initDB();
        //Log.d("TEST", "begin load weather");
        Weather weather = firstWeatherDB.loadWeatherbyCode("102");
        Log.d("TEST", "weather of 102 is: " + weather.getWeather_des());

        Log.d("TEST", "begin load city");
        City city = firstWeatherDB.loadCitybyName("无锡");
        Log.d("TEST", "code of 无锡 is: " + city.getCity_code());

        queryWeatherInfo();
        //now_temp.setText(weatherInfo.getNow().getTmp());
        // firstWeatherDB.loadAllCity();

/*        queryWeather = new WeatherInfo(this, city.getCity_code(), 3);
        Log.d("TEST", "begin query weather");
        queryWeather.queryWeatherInfo();

        Log.d("TEST", "begin to show");*/
//        Log.d("TEST", "aqi of " + city.getCity_name() + "is " + queryWeather.getAqi().getAqi());
        //Log.d("TEST", "now tmp of " + city.getCity_name() + "is " + queryWeather.getNow().getTmp());
        //Log.d("TEST", "tomorrow weather of " + city.getCity_name() + "is " + queryWeather.getDaily_forecasts()[1].getTxt_d());
    }

    public void queryWeatherInfo(){
        String address = "https://api.heweather.com/x3/weather?cityid=";
        String key = "0b2a5de562144ab481b4e4a02adfe4f3";
        String TAG = "Query Weather Info";
        String url = address + CITY_CODE + "&key=" + key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("HeWeather data service 3.0");
                    Log.d("TEST", "query now temp is: " + jsonArray.getJSONObject(0).getJSONObject("now").getString("tmp"));
                    weatherInfo.setAqi(jsonArray.getJSONObject(0).getJSONObject("aqi"));
                    weatherInfo.setNow(jsonArray.getJSONObject(0).getJSONObject("now"));
                    weatherInfo.setDaily_forecasts(jsonArray.getJSONObject(0).getJSONArray("daily_forecast"));

                    setCurrentWeather(weatherInfo);

                    //URL today_url = new URL(firstWeatherDB.loadWeatherbyCode(weatherInfo.getDaily_forecasts()[0].getCode_d()).getWeather_code());
                    //Log.d("TEST", today_url.toString());
                    //today_image.setImageURI(today_url);

                    setForecast(weatherInfo);

                    setWeatherImage(weatherInfo);

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
        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);

    }

    public void setCurrentWeather(WeatherInfo weatherInfo){
        now_temp.setText(weatherInfo.getNow().getTmp());
        now_weather.setText(weatherInfo.getNow().getTxt());
        now_wind_name.setText(weatherInfo.getNow().getDir());
        now_wind_value.setText(weatherInfo.getNow().getSc() + "级");
        now_wet.setText(weatherInfo.getNow().getHum() + "%");
        aqi_aqi.setText(weatherInfo.getAqi().getQlty());
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

    }

    public void setWeatherImage(WeatherInfo weatherInfo){

        String todayWeatherCode;
        String tomorrowWeatherCode;
        String aftertomorrowWeatherCode;
        String icon_0;
        String icon_1;
        String icon_2;


        todayWeatherCode = weatherInfo.getDaily_forecasts()[0].getCode_d();
        tomorrowWeatherCode = weatherInfo.getDaily_forecasts()[1].getCode_d();
        aftertomorrowWeatherCode = weatherInfo.getDaily_forecasts()[2].getCode_d();

        icon_0 = firstWeatherDB.loadWeatherbyCode(todayWeatherCode).getWeather_icon();
        Log.d("TEST", "icon is:" + icon_0);
        icon_1 = firstWeatherDB.loadWeatherbyCode(tomorrowWeatherCode).getWeather_icon();
        icon_2 = firstWeatherDB.loadWeatherbyCode(aftertomorrowWeatherCode).getWeather_icon();


        ImageRequest imageRequest_0 = new ImageRequest(icon_0, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                today_image.setImageBitmap(response);

            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ImageRequest imageRequest_1 = new ImageRequest(icon_1, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                tomorrow_image.setImageBitmap(response);

            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ImageRequest imageRequest_2 = new ImageRequest(icon_2, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                after_image.setImageBitmap(response);

            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(imageRequest_0);
        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(imageRequest_1);
        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(imageRequest_2);

    }
}

