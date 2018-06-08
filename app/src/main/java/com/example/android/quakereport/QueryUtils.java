package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {





    private QueryUtils() {
    }

    public static Earthquake fetchEarthquakeData(String requestUrl){
        // given a the url string, I turn it into a URL object
        URL url = createUrl(requestUrl);
        //declaring an array of Objects earthquakes

        //make a http request with the URL Object
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        Earthquake earthquake = (Earthquake) extractEarthquakes(jsonResponse);

        return earthquake;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if the url is null, then return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //see if the request is successful: code 200
                //if it is read the bytes!
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String UrlString) {
        URL url = null;
        try {
            url = new URL(UrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String earthquakeJSON) {



        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            //convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject jsonRootObject = new JSONObject(earthquakeJSON);

            //extract features JSONarray
            JSONArray featuresArray = jsonRootObject.optJSONArray("features");


            //Loop through the array to retrieve each object at position i
            for(int index = 0; index < featuresArray.length(); index++){
                //Get earthquake JSONObject at position i
                JSONObject JSONObject = featuresArray.getJSONObject(index);

                //Get “properties” JSONObject
                JSONObject propertiesJSONObject = JSONObject.getJSONObject("properties");

                //Extract “mag” for magnitude
                double mag = propertiesJSONObject.getDouble("mag");
                //Extract “place” for location
                String location = propertiesJSONObject.optString("place");
                //Extract “time” for time
                Long time = propertiesJSONObject.getLong("time");
                //extract url
                String url = propertiesJSONObject.optString("url");

                //Create Earthquake java object from magnitude, location, and time
               return (List<Earthquake>) new Earthquake(mag, location, time, url);
            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return null;
    }

}