package com.example.lx.firstweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.example.lx.firstweather.ui.CityWeather_fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    
    private String[] CITY_LIST = {"西安", "北京", "无锡"};
    private PagerAdapter mPagerAdapter;

    @BindView(R.id.view_pager) ViewPager mPager;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                invalidateOptionsMenu();
            }
        });
    }

    private class WeatherPagerAdapter extends FragmentStatePagerAdapter {

        public WeatherPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position){

            Log.d("TEST", "position is: " + position);
            return CityWeather_fragment.create(CITY_LIST[position]);
        }

        @Override
        public int getCount(){
            return CITY_LIST.length;
        }
    }
}

