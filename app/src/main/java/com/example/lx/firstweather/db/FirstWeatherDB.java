package com.example.lx.firstweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lx.firstweather.model.City;
import com.example.lx.firstweather.model.Weather;
import com.example.lx.firstweather.tools.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by koala on 2016/6/9.
 */
public class FirstWeatherDB {

    public static final String DB_NAME = "first_weather";
    public static final int version = 1;
    private static FirstWeatherDB firstWeatherDB;
    private SQLiteDatabase db;
    private Context mContext;

    private static final String KEY = "0b2a5de562144ab481b4e4a02adfe4f3";
    private static final String CITY_URL = "https://api.heweather.com/x3/citylist?search=";
    private static final String CITY_TYPE = "allchina";

    private static final String WEATHER_URL = "https://api.heweather.com/x3/condition?search=";
    private static final String WEATHER_TYPE = "allcond";

    private FirstWeatherDB(Context context){
        FirstWeatherOpenHelper dbHelper = new FirstWeatherOpenHelper(context, DB_NAME, null, version);
        db = dbHelper.getWritableDatabase();
        mContext = context;
    }

    //获取数据库的实例
    public synchronized static FirstWeatherDB getInstance(Context context){
        if (firstWeatherDB == null){
            firstWeatherDB = new FirstWeatherDB(context);
        }
        return firstWeatherDB;
    }

    //将city写入数据库
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCity_name());
            values.put("city_code", city.getCity_code());
            values.put("province", city.getProvince());
            values.put("lat", city.getLat());
            values.put("lon", city.getLon());

            db.insert("City", null, values);
        }
    }

    //从数据库中读取city
    public City loadCitybyName(String name){
        City city = new City();
        Cursor cursor = db.query("City",null, "city_name = ?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()){
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
            city.setLat(cursor.getString(cursor.getColumnIndex("lat")));
            city.setLon(cursor.getColumnName(cursor.getColumnIndex("lon")));
            cursor.close();
        }else {
            //从网络查询
            city = queryCityFromServer(name);
        }
        return city;
    }

    public City queryCityFromServer(final String name){
        final City city = new City();
        String url = CITY_URL + CITY_TYPE + "&key=" + KEY;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray city_info = response.getJSONArray("city_info");
                    for (int i = 0; i < city_info.length(); i++){
                        JSONObject jsonObject = city_info.getJSONObject(i);
                        if (jsonObject.getString("city") .equals(name)){
                            city.setCity_name(jsonObject.getString("city"));
                            city.setCity_code(jsonObject.getString("id"));
                            city.setLat(jsonObject.getString("lat"));
                            city.setLon(jsonObject.getString("lon"));
                            city.setProvince(jsonObject.getString("prov"));
                            saveCity(city);
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Json", "query_city_ERROR");
            }
        });
        request.setTag("query city");
        Singleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);

        return city;
    }

    //将weather写入数据库
    public void saveWeather(Weather weather){
        if (weather != null){
            ContentValues values = new ContentValues();
            values.put("weather_code", weather.getWeather_code());
            values.put("weather_des", weather.getWeather_des());
            values.put("weather_icon", weather.getWeather_icon());

            db.insert("Weather", null, values);
        }
    }

    //从数据库读取weather
    public Weather loadWeatherbyCode(String code){
        Weather weather = new Weather();
        Cursor cursor = db.query("Weather", null, "weather_code = ?", new String[]{code}, null, null, null);
        if (cursor.moveToFirst()){
            weather.setId(cursor.getInt(cursor.getColumnIndex("id")));
            weather.setWeather_code(cursor.getString(cursor.getColumnIndex("weather_code")));
            weather.setWeather_des(cursor.getString(cursor.getColumnIndex("weather_des")));
            weather.setWeather_icon(cursor.getString(cursor.getColumnIndex("weather_icon")));
            cursor.close();
        }else {
            //从网络查询
            weather = queryWeatherFromServer(code);
        }
        return weather;
    }

    public Weather queryWeatherFromServer(final String code){
        final Weather weather = new Weather();

        String url = WEATHER_URL + WEATHER_TYPE + "&key=" + KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status .equals("ok")){
                        JSONArray cond_info = response.getJSONArray("cond_info");
                        for (int i = 0; i < cond_info.length(); i++){
                            JSONObject object = cond_info.getJSONObject(i);
                            if (object.getString("code") .equals(code)){
                                weather.setWeather_code(object.getString("code"));
                                weather.setWeather_des(object.getString("txt"));
                                weather.setWeather_icon(object.getString("icon"));
                                saveWeather(weather);
                                break;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setTag("query weather");
        Singleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);
        return weather;
    }
}
