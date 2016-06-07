package com.example.lx.firstweather;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lx.firstweather.ui.Detail_fragment;
import com.example.lx.firstweather.ui.Summary_fragment;

public class MainActivity extends AppCompatActivity {

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


    }
}
