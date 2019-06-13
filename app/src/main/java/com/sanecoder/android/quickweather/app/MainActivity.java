package com.sanecoder.android.quickweather.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherFragment weatherFragment = new WeatherFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, weatherFragment).commit();
    }
}
