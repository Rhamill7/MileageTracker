package com.example.robbie.mileagetracker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.robbie.mileagetracker.helpers.Conversion;
import com.example.robbie.mileagetracker.Goal;
import com.example.robbie.mileagetracker.helpers.MyXAxisValueFormatter;
import com.example.robbie.mileagetracker.R;
import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    FeedReaderDbHelper db;
    List<String> names;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new FeedReaderDbHelper(getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - History");
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    /*Setting on Click listeners and spinners*/
    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to clear history?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteAllGoals();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                Dialog d = builder.create();
                d.show();

            }
        });


        final Button button5 = (Button) view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Perform action on click
                DialogFragment dialogFragment = new StartDatePicker();
                dialogFragment.show(getFragmentManager(), "start_date_picker");
            }
        });

        final Button button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                DialogFragment dialogFragment = new endDatePicker();
                dialogFragment.show(getFragmentManager(), "end_date_picker");
            }
        });

        final Button updateGraph = (Button) view.findViewById(R.id.button_update);
        updateGraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
               updateChart();
            }
        });

        /*************Setting up spinners****************/
        String[] spinner = new String[]{"Units","Steps", "Meters", "Kilometers",
                "Yards", "Miles"};
        String[] spinner2 = new String[]{"All","Completed","Above %", "Below %"};

        Spinner  s = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner2);
        s.setAdapter(adapter);

        Spinner  s2 = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner);
        s2.setAdapter(adapter2);

    }

    public String dateFormat(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date testDate = null;
        String formattedDate=null;
        try{
            testDate= (Date) format.parseObject(date);
            formattedDate = format.format(testDate);
        }catch (Exception e){

        }
        return formattedDate;
    }
    public void updateStartDateDisplay() {
        Button button5 = (Button) getView().findViewById(R.id.button5);
        String date = (startYear + "-" + (startMonth+1) + "-" + startDay);
        button5.setText(date);
    }

    public void updateEndDateDisplay() {
        Button button3 = (Button) getView().findViewById(R.id.button3);
        String date = (endYear + "-" + (endMonth+1) + "-" + endDay);
        button3.setText(date);
    }
    public void updateChart(){
        try {
        Button button5 = (Button) getView().findViewById(R.id.button5);
        Button button3 = (Button) getView().findViewById(R.id.button3);
        String startDate = dateFormat(button5.getText().toString());
        String endDate = dateFormat(button3.getText().toString());
       ArrayList<Goal> goals = new ArrayList<Goal>();
        if (startDate==(null)|| endDate ==(null)){
            Toast.makeText(getContext(), "Please enter a start and end date.", Toast.LENGTH_LONG).show();
           }else {
            try {
                goals.clear();
                goals = db.getHistory(startDate, endDate);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Please enter a start and end date.", Toast.LENGTH_LONG).show();
            }

            BarChart chart = (BarChart) getView().findViewById(R.id.chart);
            List<BarEntry> stepEntries = new ArrayList<BarEntry>();
            List<BarEntry> targetEntries = new ArrayList<BarEntry>();
            names = new ArrayList<String>();
            Spinner s = (Spinner) getView().findViewById(R.id.spinner);
            Spinner s2 = (Spinner) getView().findViewById(R.id.spinner2);
            EditText percent = (EditText) getView().findViewById(R.id.editText);
            String temp = percent.getText().toString();
            int per = 50;
            if (!temp.equals("")) {
                per = Integer.parseInt(temp);
            }
            String filter = s.getSelectedItem().toString();
            String units = s2.getSelectedItem().toString();
            if (units.equals("Units")) {
                units = "Steps";
            }
            BarDataSet dataSet = new BarDataSet(stepEntries, "Current Progress"); // add entries to dataset
            BarDataSet dataSet2 = new BarDataSet(targetEntries, "Target"); // add entries to dataset
            dataSet.setColors(Color.GREEN);
            dataSet2.setColors(Color.RED);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(dataSet2);
            dataSets.add(dataSet);
            BarData barData = new BarData(dataSets);

            chart.getDescription().setEnabled(false);
            chart.setData(barData);
            chart.invalidate();
            Conversion convert = new Conversion(getContext());
            int i = 0;
            ArrayList<Goal> filteredGoal = new ArrayList<Goal>();
            for (Goal data : goals) {
                //data.setUnits(units);
                Double cCom = ((double) data.getSteps() / (double) data.getTarget()) * 100;
                int progressPercentage = (int) Math.round(cCom);
                switch (filter) {
                    case "All":
                        filteredGoal.add(data);
                        break;
                    case "Completed":
                        if ((progressPercentage == 100) || (progressPercentage > 100)) {

                            filteredGoal.add(data);
                        }
                        break;
                    case "Above %":
                        if ((progressPercentage > per)) {
                            filteredGoal.add(data);
                        }
                        break;
                    case "Below %":
                        if (progressPercentage < per) {
                            filteredGoal.add(data);
                        }
                        break;

                    default:
                        break;
                }


            }
            if (filteredGoal.size() > 0) {
                names.clear();
                for (Goal g : filteredGoal) {
                    g.setUnits(units);
                    String[] stepEntry = convert.convert(g);
                    int one = Integer.parseInt(stepEntry[0]);
                    int two = Integer.parseInt(stepEntry[2]);
                    stepEntries.add(new BarEntry(i, one));
                    targetEntries.add(new BarEntry(i, two));
                    names.add(g.getDate() + "(" + g.getName() + ")");
                    i++;
                }
            }


            dataSet = new BarDataSet(stepEntries, "Current Progress"); // add entries to dataset
            dataSet2 = new BarDataSet(targetEntries, "Target"); // add entries to dataset
            dataSet.setColors(Color.GREEN);
            dataSet2.setColors(Color.RED);
            dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(dataSet2);
            dataSets.add(dataSet);
            barData = new BarData(dataSets);

            XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new MyXAxisValueFormatter(names.toArray(new String[names.size()])));

            chart.getDescription().setEnabled(false);
            chart.setData(barData);
            chart.invalidate();
        }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Whoops! Something went wrong!", Toast.LENGTH_LONG).show();
                }


    }

    /************Calender Picker Code****************/

    Calendar c = Calendar.getInstance();
    int startYear = c.get (Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

    int endYear = c.get (Calendar.YEAR);
    int endMonth = c.get(Calendar.MONTH);
    int endDay = c.get(Calendar.DAY_OF_MONTH);

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

public class endDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, endYear, endMonth, endDay);
            return dialog;

        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
            endYear = year;
            endMonth = monthOfYear;
            endDay = dayOfMonth;
            updateEndDateDisplay();

        }

    }


}
