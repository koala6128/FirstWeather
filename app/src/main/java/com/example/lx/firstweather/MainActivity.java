package com.example.lx.firstweather;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lx.firstweather.db.FirstWeatherDB;
import com.example.lx.firstweather.model.City;
import com.example.lx.firstweather.model.Weather;
import com.example.lx.firstweather.ui.Detail_fragment;
import com.example.lx.firstweather.ui.Summary_fragment;

public class MainActivity extends AppCompatActivity {

    private FirstWeatherDB firstWeatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Detail_fragment detailFragment = new Detail_fragment();
        Summary_fragment summaryFragment = new Summary_fragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container0, detailFragment);
        transaction.add(R.id.fragment_container1, summaryFragment);
        transaction.commit();

        firstWeatherDB = FirstWeatherDB.getInstance(this);
        Log.d("TEST", "begin load weather");
        Weather weather = firstWeatherDB.loadWeatherbyCode("102");
        Log.d("TEST", "weather of 102 is: " + weather.getWeather_des());
        Log.d("TEST", "begin load city");
        City city = firstWeatherDB.loadCitybyName("无锡");
        Log.d("TEST", "code of 无锡 is: " + city.getCity_code());
    }
}
