package com.example.robbie.mileagetracker.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robbie.mileagetracker.helpers.Conversion;
import com.example.robbie.mileagetracker.Goal;
import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;
import com.example.robbie.mileagetracker.helpers.MyAdapter;
import com.example.robbie.mileagetracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    FeedReaderDbHelper db;
    View rootView;
    String goalName;
    int currentSteps = 0, goalSteps = 0;
    public Goal goal=null;
    EditText steps, name;
    String date;
    Spinner s;

    public TripsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean bool = false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MileageTracker - Trips");
        db = new FeedReaderDbHelper(getContext());

        try{
            db.testInit();
        }
        catch(Exception e){
            db.initialiseSettings();
        }

       if(db.testActive()){
           try {
               date = db.getDate();
               ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_title_black_18dp);
           }
           catch (Exception e){}
       }
       else{
           date = getDateTime();
       }
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
        rootView = inflater.inflate(R.layout.fragment_goals, container, false);
        ArrayList<Goal> goals = populateGoals();
        getActiveGoal();
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        FragmentManager fm = getFragmentManager();
        MyAdapter rvAdapter = new MyAdapter(goals, getContext());
        rv.setAdapter(rvAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.github.clans.fab.FloatingActionButton fabStep = (com.github.clans.fab.FloatingActionButton)view.findViewById(R.id.menu1);
        com.github.clans.fab.FloatingActionButton fabGoal = (com.github.clans.fab.FloatingActionButton)view.findViewById(R.id.menu2);
        fabStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSteps();
//                notify();

            }
        });
        fabGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGoal();
            }
        });
    }

    public void addSteps(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v =inflater.inflate(R.layout.add_step_dialog, null);
        builder.setView(v);
        steps = (EditText) v.findViewById(R.id.goalSteps2) ;
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                try{
                currentSteps = db.getActiveGoal(date).getSteps() + (Integer.parseInt(steps.getText().toString()));
                CardView cv = (CardView) rootView.findViewById(R.id.card_view_active);
                TextView current = (TextView) cv.findViewById(R.id.currentSteps5);
                current.setText(Integer.toString(currentSteps));
                    db.updateStepsforGoal(currentSteps, db.getActiveGoal(date).getName(), date);
                    getActiveGoal();

                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Please enter No. of Steps", Toast.LENGTH_LONG).show();
                }
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

    public void addGoal(){
        String[] spinner = new String[]{"Steps", "Meters", "Kilometers",
                "Yards", "Miles"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v =inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(v);
        steps = (EditText) v.findViewById(R.id.goalSteps1);
        name = (EditText) v.findViewById(R.id.goalName1);
        s = (Spinner) v.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
             android.R.layout.simple_dropdown_item_1line, spinner);
        s.setAdapter(adapter);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                rv.setHasFixedSize(true);
                try{
                    String temp = steps.getText().toString();
                    goalSteps = Integer.parseInt(temp);
                    goalName = name.getText().toString();
                    if(db.checkGoalExists(goalName,date)){
                        Toast.makeText(getContext(), "Goal already Exists for today!", Toast.LENGTH_LONG).show();
                    }else {
                        db.insertGoal(goalName, 0, goalSteps, date, 0, s.getSelectedItem().toString());
                    }
                } catch (Exception e){
                    Toast.makeText(getContext(), "Please Enter a Name and/or Step Goal", Toast.LENGTH_LONG).show();
                }
                ArrayList<Goal> goals = populateGoals();
                MyAdapter adapter = new MyAdapter( goals, getContext());
                rv.setAdapter(adapter);
                rv.invalidate();
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

    public void getActiveGoal(){
        try {
            goal = db.getActiveGoal(date);
            Conversion convert= new Conversion(getContext());
            CardView cv = (CardView) rootView.findViewById(R.id.card_view_active);
            TextView active = (TextView) cv.findViewById(R.id.Active_Goal_Name) ;
            TextView current = (TextView) cv.findViewById(R.id.currentSteps5) ;
            TextView target = (TextView) cv.findViewById(R.id.currentSteps6) ;
            TextView output = (TextView) cv.findViewById(R.id.textView11);
            active.setText(goal.getName());
            String units = db.getUnits();
            if (!units.equals("Default") ){
                goal.setUnits(units);
            }
            String[] converted = convert.convert(goal);
            current.setText(converted[0] + " " + converted[1]);
            target.setText(converted[2]+" "+converted[3]);
            ProgressBar progress = (ProgressBar) cv.findViewById(R.id.progressBarActive) ;
            Double cCom = ((double)goal.getSteps()/goal.getTarget()) * 100;
            int progressPercentage = (int)Math.round(cCom);
            output.setText(Integer.toString(progressPercentage) + "%");
            progress.setProgress(progressPercentage);

        } catch (Exception e){
            Toast.makeText(getContext(), "No active Goal Set", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Goal> populateGoals(){
        ArrayList<Goal> goals = db.getGoalsOnDate(date);

        return goals;
    }

    public void notification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.ic_directions_run_white_18dp)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        // "yyyy-MM-dd HH:mm:ss", Locale.getDefault();
        "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}

