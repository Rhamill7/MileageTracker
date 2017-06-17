package com.example.robbie.mileagetracker.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    FeedReaderDbHelper db;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - Statistics");
        db = new FeedReaderDbHelper(getContext());
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

        String[] spinner = new String[]{"Units","Steps", "Meters", "Kilometers",
                "Yards", "Miles"};
        String[] spinner2 = new String[]{"All","Completed","Above %", "Below %"};
        String[] spinner3 = new String[]{"All","Average","Minimum", "Maximum"};

        Spinner  s = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner2);
        s.setAdapter(adapter);


        Spinner  s2 = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner);
        s2.setAdapter(adapter2);

        Spinner  s3 = (Spinner) view.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, spinner3);
        s3.setAdapter(adapter3);
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

        ArrayList<String> names = new ArrayList<String>();
        Button button5 = (Button) getView().findViewById(R.id.button5);
        Button button3 = (Button) getView().findViewById(R.id.button3);
        String startDate = dateFormat(button5.getText().toString());
        String endDate = dateFormat(button3.getText().toString());
        ArrayList<Goal> goals = new ArrayList<Goal>();
        if (startDate==(null)|| endDate ==(null)){
            Toast.makeText(getContext(), "Please enter a start and end date.", Toast.LENGTH_LONG).show();
        }else {
            try {
                goals = db.getHistory(startDate, endDate);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Please enter a start and end date.", Toast.LENGTH_LONG).show();
            }


            List<BarEntry> stepEntries = new ArrayList<BarEntry>();

            List<BarEntry> targetEntries = new ArrayList<BarEntry>();

            Spinner s = (Spinner) getView().findViewById(R.id.spinner);
            Spinner s2 = (Spinner) getView().findViewById(R.id.spinner2);
            Spinner s3 = (Spinner) getView().findViewById(R.id.spinner3);
            EditText percent = (EditText) getView().findViewById(R.id.editText2);
            String temp = percent.getText().toString();
            int per = 50;
            if (!temp.equals("")) {
                per = Integer.parseInt(temp);
            }
            String filter = s.getSelectedItem().toString();
            String units = s2.getSelectedItem().toString();
            String sel = s3.getSelectedItem().toString();
            if (units.equals("Units")) {
                units = "Steps";
            }

            Conversion convert = new Conversion(getContext());
            int i = 0, minVal = 101, maxVal = -1;
            Goal min = null;
            Goal max = null;
            ArrayList<Goal> filteredGoals = new ArrayList<Goal>();
            for (Goal data : goals) {
                Double cCom = ((double) data.getSteps() / (double) data.getTarget()) * 100;
                int progressPercentage = (int) Math.round(cCom);

                switch (filter) {
                    case "Completed":
                        if (progressPercentage < 100) {

                        } else {
                            filteredGoals.add(data);

                        }
                        break;
                    case "Above %":
                        if (progressPercentage < per) {
                        } else {
                            filteredGoals.add(data);

                        }
                        break;
                    case "Below %":
                        if (progressPercentage > per) {

                        } else {
                            filteredGoals.add(data);
                        }
                        break;
                    default:
                        filteredGoals.add(data);
                        break;
                }

                i++;
            }
            if(filteredGoals.size()>0)
            switch (sel) {

                case "All":
                    int avgStepsAll = 0, targetStepsAll = 0;
                    for (Goal goal : filteredGoals) {
                        avgStepsAll = avgStepsAll + goal.getSteps();
                        targetStepsAll = targetStepsAll + goal.getTarget();
                        int  progressPercentage =(int) ((double)goal.getSteps()/(double)goal.getTarget());

                        if (progressPercentage > maxVal){
                            maxVal = progressPercentage;
                            max = goal;
                        }
                        else if (progressPercentage == maxVal){
                            if (goal.getSteps()>max.getSteps()){
                                maxVal = progressPercentage;
                                max = goal;
                            }
                        }

                        if (progressPercentage < minVal){
                            minVal = progressPercentage;
                            min = goal;
                        }
                        else if (progressPercentage == minVal){
                            if (goal.getSteps()<min.getSteps()){
                                minVal = progressPercentage;
                                min = goal;
                            }
                        }

                    }

                    avgStepsAll = (int) ((double) avgStepsAll / (double) filteredGoals.size());
                    targetStepsAll = (int) ((double) targetStepsAll / (double) filteredGoals.size());
                    Goal avgAll = new Goal("test",avgStepsAll, targetStepsAll, "this", 0, units );
                    String[] stepEntryAll = convert.convert(avgAll);
                    int oneAll = Integer.parseInt(stepEntryAll[0]);
                    int twoAll = Integer.parseInt(stepEntryAll[2]);
                    stepEntries.add(new BarEntry(0, oneAll));
                    targetEntries.add(new BarEntry(0, twoAll));
                    names.add("Avg");

                    max
                            .setUnits(units);
                    String[] stepEntryAll2 = convert.convert(max);
                    int oneAll2 = Integer.parseInt(stepEntryAll2[0]);
                    int twoAll2 = Integer.parseInt(stepEntryAll2[2]);
                    stepEntries.add(new BarEntry(1, oneAll2));
                    targetEntries.add(new BarEntry(1, twoAll2));
                    names.add("Max: (" + max.getDate() + ") " + max.getName());

                    min.setUnits(units);
                    String[] stepEntryAll3 = convert.convert(min);
                    int oneAll3 = Integer.parseInt(stepEntryAll3[0]);
                   int twoAll3 = Integer.parseInt(stepEntryAll3[2]);
                    stepEntries.add(new BarEntry(2, oneAll3));
                    targetEntries.add(new BarEntry(2, twoAll3));
                    names.add("Min: (" + min.getDate() + ") " + min.getName());

                    break;
                case "Average":

                    int avgSteps = 0, targetSteps = 0;
                    for (Goal goal : filteredGoals) {
                        avgSteps = avgSteps + goal.getSteps();
                        targetSteps = targetSteps + goal.getTarget();
                    }
                    avgSteps = (int) ((double) avgSteps / (double) filteredGoals.size());
                    targetSteps = (int) ((double) targetSteps / (double) filteredGoals.size());
                    Goal avg = new Goal("test",avgSteps, targetSteps, "this", 0, units );
                    String[] stepEntry = convert.convert(avg);
                    int one = Integer.parseInt(stepEntry[0]);
                    int two = Integer.parseInt(stepEntry[2]);
                    stepEntries.add(new BarEntry(0, one));
                   targetEntries.add(new BarEntry(0, two));
                    names.add("Avg");
                    break;

                case "Maximum":
                    for (Goal goal : filteredGoals) {
                     int  progressPercentage =(int) ((double)goal.getSteps()/(double)goal.getTarget());
                     if (progressPercentage > maxVal){
                        maxVal = progressPercentage;
                        max = goal;
                     }
                     else if (progressPercentage == maxVal){
                        if (goal.getSteps()>max.getSteps()){
                            maxVal = progressPercentage;
                            max = goal;
                        }
                     }
                    }
                    max.setUnits(units);
                    String[] stepEntryMax = convert.convert(max);
                    int maxOne = Integer.parseInt(stepEntryMax[0]);
                    int maxTwo = Integer.parseInt(stepEntryMax[2]);
                    stepEntries.add(new BarEntry(0, maxOne));
                    targetEntries.add(new BarEntry(0, maxTwo));
                    names.add("Max: (" + max.getDate() + ") " + max.getName());
                    break;

                case "Minimum":
                    for (Goal goal : filteredGoals) {
                        int  progressPercentage =(int) ((double)goal.getSteps()/(double)goal.getTarget());

                        if (progressPercentage < minVal){
                            minVal = progressPercentage;
                            min = goal;
                        }
                        else if (progressPercentage == minVal){
                            if (goal.getSteps()<min.getSteps()){
                                minVal = progressPercentage;
                                min = goal;
                            }
                        }
                    }
                min.setUnits(units);
                    String[] stepEntryMin = convert.convert(min);
                    int minOne = Integer.parseInt(stepEntryMin[0]);
                    int minTwo = Integer.parseInt(stepEntryMin[2]);
                    stepEntries.add(new BarEntry(0, minOne));
                    targetEntries.add(new BarEntry(0, minTwo));

                    names.add("Min: (" + min.getDate() + ") " + min.getName());

                    break;
                default:
                    break;
            }

            BarChart chart = (BarChart) getView().findViewById(R.id.chart);
            BarDataSet dataSet = new BarDataSet(stepEntries, "Current Progress"); // add entries to dataset
            BarDataSet dataSet2 = new BarDataSet(targetEntries, "Target"); // add entries to dataset
            dataSet.setColors(Color.GREEN);
            dataSet2.setColors(Color.LTGRAY);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(dataSet2);
            dataSets.add(dataSet);

            BarData barData = new BarData(dataSets);



            try {
                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                xAxis.setValueFormatter(new MyXAxisValueFormatter(names.toArray(new String[names.size()+2])));
                chart.getDescription().setEnabled(false);
                chart.setData(barData);
                chart.invalidate();
            }catch(Exception e){
                Toast.makeText(getContext(), "Whoops! Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }

//////////////////Caleder Picker Code///////////////////////

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



