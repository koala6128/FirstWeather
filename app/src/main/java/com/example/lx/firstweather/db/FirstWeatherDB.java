package com.example.lx.firstweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.lx.firstweather.model.City;
import com.example.lx.firstweather.model.Weather;

/**
 * Created by koala on 2016/6/9.
 */
public class FirstWeatherDB {

    public static final String DB_NAME = "first_weather";
    public static final int version = 1;
    private static FirstWeatherDB firstWeatherDB;
    private SQLiteDatabase db;
    //private Context mContext;

    private FirstWeatherDB(Context context){
        FirstWeatherOpenHelper dbHelper = new FirstWeatherOpenHelper(context, DB_NAME, null, version);
        db = dbHelper.getWritableDatabase();
        //mContext = context;
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
            Log.d("TEST", "saving city");
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCity_name());
            values.put("city_code", city.getCity_code());
            values.put("province", city.getProvince());
            values.put("lat", city.getLat());
            values.put("lon", city.getLon());

            db.insert("City", null, values);
            Log.d("TEST", "city saved");
        }else {
            Log.d("TEST", "saving city ERROR");
        }
    }

    //从数据库中读取city
    public City loadCitybyName(String name){
        City city = new City();
        Cursor cursor = db.query("City", null, "city_name = ?", new String[]{name}, null, null, null);

        if (cursor.moveToFirst()){
            Log.d("TEST", "load city from db");
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
            city.setLat(cursor.getString(cursor.getColumnIndex("lat")));
            city.setLon(cursor.getColumnName(cursor.getColumnIndex("lon")));
        }
        cursor.close();
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
            Log.d("TEST", "load weather from db");
            weather.setId(cursor.getInt(cursor.getColumnIndex("id")));
            weather.setWeather_code(cursor.getString(cursor.getColumnIndex("weather_code")));
            weather.setWeather_des(cursor.getString(cursor.getColumnIndex("weather_des")));
            weather.setWeather_icon(cursor.getString(cursor.getColumnIndex("weather_icon")));

        }
        cursor.close();
        return weather;
    }

}
