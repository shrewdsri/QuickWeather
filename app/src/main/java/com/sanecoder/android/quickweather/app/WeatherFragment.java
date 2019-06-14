package com.sanecoder.android.quickweather.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


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
        HttpsURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_container, container, false);

        try {
            URL url = new URL("https://api.weatherbit.io/v2.0/forecast/daily?city=chennai,IN&key=a06e7db930e54f83a25bfc5167404435");

            urlConnection = (HttpsURLConnection)url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputstream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            String rawJsonResponse = null;

            if(inputstream == null) {
                rawJsonResponse = null;
                return rootView;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputstream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line);
            }

            if(buffer.length() == 0){
                rawJsonResponse = null;
                return rootView;
            }

            rawJsonResponse = buffer.toString();

            //new FetchWeatherTask().execute("https://api.weatherbit.io/v2.0/forecast/daily?city=chennai,IN&key=a06e7db930e54f83a25bfc5167404435");
            ArrayList<String> weatherDataList = new ArrayList<>();
            JSONObject obj = new JSONObject(rawJsonResponse);
            JSONArray array = obj.getJSONArray("data");
            for(int i=0; i<array.length(); i++)
            {
                JSONObject dailyData = array.getJSONObject(i);
                JSONObject weatherDetails = dailyData.getJSONObject("weather");
                String weatherDate = dailyData.getString("datetime");
                Double maxTemp = dailyData.getDouble("max_temp");
                Double minTemp = dailyData.getDouble("min_temp");
                String weatherDesc = weatherDetails.getString("description");
                weatherDataList.add(String.format(Locale.ENGLISH,"%s - %s - %3.0f/%3.0f", weatherDate, weatherDesc, maxTemp, minTemp));
            }

            Context context = getContext();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    weatherDataList);

            ListView listView = rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);

        }catch (Exception ex){
            Log.e("WeatherFragment", "Error", ex);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weatherfragment, menu);
    }

}

class FetchWeatherTask extends AsyncTask<String, Integer, String>{

    final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    @Override
    protected String doInBackground(String... strings) {

        HttpsURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String rawJsonResponse = null;

        try {
            if (strings.length > 0) {
                URL url = new URL(strings[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputstream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputstream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputstream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }

                rawJsonResponse = buffer.toString();
            }
        }catch (Exception ex){
            Log.e(LOG_TAG, "Error", ex);
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }catch (final IOException e){
                Log.e(LOG_TAG, "Error closing stream", e);
            }
            return rawJsonResponse;
        }
    }

    @Override
    protected void onPostExecute(String rawJsonResponse) {
        try {
            ArrayList<String> weatherDataList = new ArrayList<>();
            JSONObject obj = new JSONObject(rawJsonResponse);
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject dailyData = array.getJSONObject(i);
                JSONObject weatherDetails = dailyData.getJSONObject("weather");
                String weatherDate = dailyData.getString("datetime");
                Double maxTemp = dailyData.getDouble("max_temp");
                Double minTemp = dailyData.getDouble("min_temp");
                String weatherDesc = weatherDetails.getString("description");
                weatherDataList.add(String.format(Locale.ENGLISH, "%s - %s - %3.0f/%3.0f", weatherDate, weatherDesc, maxTemp, minTemp));
            }

//            Context context = getContext();
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
//                    R.layout.list_item_forecast,
//                    R.id.list_item_forecast_textview,
//                    weatherDataList);
//
//            ListView listView = rootView.findViewById(R.id.listview_forecast);
//            listView.setAdapter(adapter);
        }
        catch (Exception ex1){
            Log.e(LOG_TAG, "Error - PostExecute", ex1);
        }
    }


}
