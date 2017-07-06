package com.example.robbie.mileagetracker.fragments;


import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robbie.mileagetracker.R;
import com.example.robbie.mileagetracker.location.LocationApiWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackerFragment extends Fragment {

    public String date;
    public Boolean testMode;


    public TrackerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch mySwitch = (Switch) view.findViewById(R.id.switch1);
        Button myButton = (Button) view.findViewById(R.id.button4);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - Export");

        final TextView dateEntered = (TextView) view.findViewById(R.id.textView3);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                LocationApiWrapper loc = new LocationApiWrapper(getContext());
                Location location = loc.getCurrentLocation();
//                Toast.makeText(getContext(),
//                        "olc: lat: " + String.valueOf(location.getLatitude()) +
//                        "long: " + String.valueOf(location.getLongitude()),
//                        Toast.LENGTH_SHORT).show();
               }catch (Exception exception){
                   Toast.makeText(getContext(), "NAWWWW", Toast.LENGTH_LONG).show();
               }

            }
        });
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){

            }else{

            }}

    });


    }

}

