package com.example.lx.firstweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by koala on 2016/6/9.
 */
public class FirstWeatherOpenHelper extends SQLiteOpenHelper {

    //city全国城市列表
    public static final String CREATE_CITY ="create table City ("
            + "id integer primary key autoincrement,"
            + "city_name text, "
            + "city_code text, "
            + "province text, "
            + "lat text, "
            + "lon text)";

    //天气类型列表
    public static final String CREATE_WEATHER = "create table Weather ("
            + "id integer primary key autoincrement, "
            + "weather_code text, "
            + "weather_des text, "
            + "weather_icon text)";

    public FirstWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_CITY);
        database.execSQL(CREATE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

    }
}
