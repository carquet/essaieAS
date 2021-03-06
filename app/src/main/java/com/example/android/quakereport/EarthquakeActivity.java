/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /** Sample JSON response for a USGS query */
    private static final String USGS_URL_STRING  = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
     EarthquakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        GetEarthquakes task = new GetEarthquakes();
        task.execute(USGS_URL_STRING);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of earthquakes
         adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (earthquakeListView != null) {
            earthquakeListView.setAdapter(adapter);
        }

        if (earthquakeListView != null) {
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Earthquake currentEarthquake = adapter.getItem(i);
                    Uri urlParsed = Uri.parse(currentEarthquake.getmUrl());
                    Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, urlParsed);
                    startActivity(openUrlIntent);
                }
            });
        }


    }

        private class GetEarthquakes extends AsyncTask<String, Void, List<Earthquake>>{


            @Override
            protected List<Earthquake> doInBackground(String... urls) {
                // Perform the HTTP request for earthquake data and process the response.
                // Create a fake list of earthquake locations.
                List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
                return result;


            }

            @Override
            protected void onPostExecute(final List<Earthquake> earthquakes) {
                super.onPostExecute(earthquakes);
                adapter.clear();
                if (earthquakes != null && !earthquakes.isEmpty()) {
                    adapter.addAll(earthquakes);
                }
            }
        }
}
