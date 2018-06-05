package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.w3c.dom.Text;
import android.graphics.drawable.GradientDrawable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{
    //separator of the string location
    private static final String LOCATION_SEPARATOR = " of ";
    String primaryLocation;
    String locationOffset;

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  @NonNull ViewGroup parent) {
        //check if the existing view is being used, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);



        //////////////////////MAGNITUDE////////////////////////////////
        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getmMagnitude());
        magTextView.setText(formattedMagnitude);

        /////////////////////////LOCATIONS/////////////////////////////
        String originalLocation = currentEarthquake.getmLocation();

        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String[] separatedLocation = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = separatedLocation[0] + LOCATION_SEPARATOR;
            primaryLocation = separatedLocation[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationTextView.setText(primaryLocation);
        TextView offsetLocationTextView = (TextView) listItemView.findViewById(R.id.location_offset);
        offsetLocationTextView.setText(locationOffset);


        ///////////////////// DATE and TIME //////////
        //Set millisecondstime into readable time and date
        //receive currentEarthquake.getnTime Long number as an argument and turns it into an object
        Date dateObject = new Date(currentEarthquake.getmTime());

        //grab view and call the customized method on the object see ^
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);
        //grab view and call the customized method on the object see ^
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeTextView.setText(formattedTime);


        //returns the inflated view, populated with list item elements
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 1:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
             default:
                 magnitudeColorResourceId = R.color.magnitude10plus;
                 break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
