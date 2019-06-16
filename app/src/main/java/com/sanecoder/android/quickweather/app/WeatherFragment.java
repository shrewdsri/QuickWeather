package com.sanecoder.android.quickweather.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    final String LOG_TAG = WeatherFragment.class.getSimpleName();
    public ArrayAdapter<String> wForecastAdapter = null;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        View rootView = inflater.inflate(R.layout.fragment_container, container, false);

        try {
//            URL url = new URL("https://api.weatherbit.io/v2.0/forecast/daily?city=chennai,IN&key=a06e7db930e54f83a25bfc5167404435");
//
//            urlConnection = (HttpsURLConnection) url.openConnection();
//
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputstream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            String rawJsonResponse = null;
//
//            if (inputstream == null) {
//                rawJsonResponse = null;
//                return rootView;
//            }
//
//            bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                buffer.append(line);
//            }
//
//            if (buffer.length() == 0) {
//                rawJsonResponse = null;
//                return rootView;
//            }
//
//            rawJsonResponse = buffer.toString();
//
//            //new FetchWeatherTask().execute("https://api.weatherbit.io/v2.0/forecast/daily?city=chennai,IN&key=a06e7db930e54f83a25bfc5167404435");
//            ArrayList<String> weatherDataList = new ArrayList<>();
//            JSONObject obj = new JSONObject(rawJsonResponse);
//            JSONArray array = obj.getJSONArray("data");
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject dailyData = array.getJSONObject(i);
//                JSONObject weatherDetails = dailyData.getJSONObject("weather");
//                String weatherDate = dailyData.getString("datetime");
//                Double maxTemp = dailyData.getDouble("max_temp");
//                Double minTemp = dailyData.getDouble("min_temp");
//                String weatherDesc = weatherDetails.getString("description");
//                weatherDataList.add(String.format(Locale.ENGLISH, "%s - %s - %3.0f/%3.0f", weatherDate, weatherDesc, maxTemp, minTemp));
//            }
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
            wForecastAdapter = new ArrayAdapter<>(context,
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    weeklyForecast);

            Log.i(LOG_TAG, weeklyForecast.toString());
            ListView listView = rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(wForecastAdapter);

        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error", ex);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weatherfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_refresh) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            builder.path("api.weatherbit.io");
            builder.appendPath("v2.0/forecast/daily");
            builder.appendQueryParameter("city", "chennai,IN");
            builder.appendQueryParameter("key", "a06e7db930e54f83a25bfc5167404435");
            Uri weatherUri = builder.build();
            String weatherUrl = weatherUri.toString();
            new FetchWeatherTask().execute(weatherUrl);
        }
        return true;
    }


    public class FetchWeatherTask extends AsyncTask<String, Integer, String> {

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
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error", ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (final IOException e) {
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

                if (!weatherDataList.isEmpty()) {
                    wForecastAdapter.clear();
                    for(String dayForecast:weatherDataList){
                        wForecastAdapter.add(dayForecast);
                    }
                }
            } catch (Exception ex1) {
                Log.e(LOG_TAG, "Error - PostExecute", ex1);
            }
        }


    }
}
