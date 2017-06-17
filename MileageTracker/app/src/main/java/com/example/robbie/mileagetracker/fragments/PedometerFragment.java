package com.example.robbie.mileagetracker.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.robbie.mileagetracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedometerFragment extends Fragment {

    public String date;
    public Boolean testMode;

    public PedometerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedometer, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch mySwitch = (Switch) view.findViewById(R.id.switch1);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - Export");

        final TextView dateEntered = (TextView) view.findViewById(R.id.textView3);


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){

            }else{

            }
        }

    });}

}

