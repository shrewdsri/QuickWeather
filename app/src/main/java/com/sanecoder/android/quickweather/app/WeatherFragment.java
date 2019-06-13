package com.sanecoder.android.quickweather.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[]  data = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Wednesday - Cloudy - 72/63",
                "Thursday - Sunny - 90/70",
                "Friday - Rainy - 65/59",
                "Saturday - Foggy - 74/63",
                "Sunday - Cloudy - 80/69"
        };
        List<String> weeklyForecast = new ArrayList<>(Arrays.asList(data));
        Context context = getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weeklyForecast);

        View rootView = inflater.inflate(R.layout.fragment_container, container, false);

        ListView listView = rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        return rootView;
    }

}
