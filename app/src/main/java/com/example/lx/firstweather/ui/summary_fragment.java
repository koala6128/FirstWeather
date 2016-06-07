package com.example.lx.firstweather.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lx.firstweather.R;

/**
 * Created by LX on 2016/6/7.
 */
public class Summary_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.summary_fragment, container, false);
        return view;
    }
}
