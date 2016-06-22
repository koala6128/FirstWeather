package com.example.lx.firstweather;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lx.firstweather.ui.CityWeather_fragment;

public class MainActivity extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityWeather_fragment fragment = new CityWeather_fragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();


    }
}

