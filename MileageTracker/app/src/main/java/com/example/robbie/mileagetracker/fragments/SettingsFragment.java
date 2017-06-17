package com.example.robbie.mileagetracker.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robbie.mileagetracker.R;
import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    FeedReaderDbHelper db;
    public String date;
    public Boolean testMode, goalsEditable;

    public SettingsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new FeedReaderDbHelper(getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - Settings");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch mySwitch = (Switch) view.findViewById(R.id.switch1);
        Switch editSwitch = (Switch) view.findViewById(R.id.switch2);
         EditText edit = (EditText)  view.findViewById(R.id.editText4);
        final TextView dateEntered = (TextView) view.findViewById(R.id.textView3);
        try{
            dateEntered.setText(db.getDate());
            if (db.testActive()){
                testMode = Boolean.TRUE;
                mySwitch.setChecked(true);
            }
            if (db.testEditable()){
                goalsEditable = Boolean.TRUE;
                editSwitch.setChecked(true);
            }
        }catch(Exception e){
        }

        String[] spinner = new String[]{"Default", "Steps", "Meters", "Kilometers",
                "Yards", "Miles"};

        final Spinner s = (Spinner) view.findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String g = s.getSelectedItem().toString();
                db.setUnits(g);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Check for test mode ON/OFF
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    testMode = Boolean.TRUE;
                    try{
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_title_black_18dp);
                        DialogFragment dialogFragment = new SettingsFragment.StartDatePicker();
                        dialogFragment.show(getFragmentManager(), "start_date_picker");
                    }catch (Exception e){

                    }
                }else{
                    testMode = Boolean.FALSE;
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                    db.updateTestMode(date, 0);
                }
            }

        });

        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    goalsEditable = Boolean.TRUE;
                    try{
                       db.updateEditable(1);
                    }catch (Exception e){

                    }
                }else{
                    goalsEditable = Boolean.FALSE;
                    db.updateEditable(0);
                }
            }

        });


        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Double d  = Double.parseDouble(s.toString());
                   // Toast.makeText(getContext(), d.toString(), Toast.LENGTH_LONG).show();
                   db.setStride(d);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Nope", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

    }

    public void updateStartDateDisplay() {
        Date testDate = null;
        final TextView dateEntered = (TextView) getView().findViewById(R.id.textView3);
         date = (startYear + "-" + (startMonth+1) + "-" + startDay);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try{
           testDate= (Date) format.parseObject(date);
           date = format.format(testDate);
        }catch (Exception e){

        }

        dateEntered.setText(date);
        try{
        db.updateTestMode(date, 1);}
        catch(Exception e){
            String bob = e.toString();
        }
    }

    Calendar c = Calendar.getInstance();
    int startYear = c.get (Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, startYear, startMonth, startDay);
            return dialog;

        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            updateStartDateDisplay();


        }

    }
}
