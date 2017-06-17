package com.example.robbie.mileagetracker.helpers;

/**
 * Created by Robbie on 11/02/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.*;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robbie.mileagetracker.Goal;
import com.example.robbie.mileagetracker.R;
import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    private ArrayList<Goal> goals;
    private Context mContext;
    private ViewGroup vg;
    private FragmentManager fm;
    EditText step, name;
    private String date;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView, mTextViewLower, mOptions;
        public  FeedReaderDbHelper db;

      //  public TextView mOptions;
        public FragmentManager fm;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            mTextViewLower = (TextView) v.findViewById(R.id.lowerText);
            mOptions = (TextView) v.findViewById(R.id.txtOptionDigit);
            db = new FeedReaderDbHelper(v.getContext());


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter( ArrayList<Goal> goals, Context mContext) {
        this.goals = goals;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        vg = parent;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        boolean bool = false;
        try{
           bool  = holder.db.testActive();
        }
        catch(Exception e){
            //holder.db.insertTest(getDateTime(), 0);
        }

        if(bool){
            try {
                date = holder.db.getDate();
            }
            catch (Exception e){}
        }
        else{
            date = getDateTime();
        }
//        String units = holder.db.getUnits();
//        if (!units.equals("Default")){
//            goals.get(position).setUnits(units);
//        }
        Conversion convert = new Conversion(mContext);
        String[] stepEntry = convert.convert(goals.get(position));
       // int one = Integer.parseInt(stepEntry[0]);
        int two = Integer.parseInt(stepEntry[2]);
        holder.mTextView.setText(goals.get(position).getName());
        holder.mTextViewLower.setText("Target: " + Integer.toString(two)+
                " "+ goals.get(position).getUnits());
       // holder.mCardView.findViewById(R.id.).setOnClickListener(new holder.);
        holder.mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.mOptions );
                popupMenu.inflate(R.menu.summary_screen);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_edit:
                                if (holder.db.testEditable()){
                              // Toast.makeText(mContext, "Edited", Toast.LENGTH_LONG).show();
                               final String tim = holder.mTextView.getText().toString();
                               // Goal goalEdit = holder.db.getGoal(tim);
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                Activity active = (Activity)mContext;
                                LayoutInflater inflater = active.getLayoutInflater();
                                final View v =inflater.inflate(R.layout.custom_alert_dialog, null);
                                builder.setView(v);
                                TextView txt = (TextView) v.findViewById(R.id.titleFour);
                                txt.setText("Edit Goal");
                                step = (EditText) v.findViewById(R.id.goalSteps1);
                                 name = (EditText) v.findViewById(R.id.goalName1);
                                    String[] spinner = new String[]{"Steps", "Meters", "Kilometers",
                                            "Yards", "Miles"};
                                   final Spinner s = (Spinner) v.findViewById(R.id.spinner3);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>((mContext),
                                            android.R.layout.simple_dropdown_item_1line, spinner);
                                    s.setAdapter(adapter);


                                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {

                                        try{
                                            String newGoalName = name.getText().toString();
                                            int newGoalSteps = Integer.parseInt(step.getText().toString());
                                            Goal g = holder.db.getGoal(tim, date);
                                            for (int i = 0; i < goals.size(); i++) {
                                                if (goals.get(i).getName().equals(tim)) {
                                                    goals.remove(i);
                                                }
                                            }
                                            holder.db.updateGoal(g.getName(), newGoalName, g.getTarget(), newGoalSteps, s.getSelectedItem().toString());
                                            g = holder.db.getGoal(newGoalName, date);
                                            goals.add(g);
                                        }
                                        catch (Exception e){
                                            Toast.makeText(mContext, "Please Enter a Name and/or Step Goal", Toast.LENGTH_LONG).show();
                                        }
                                       notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                Dialog d = builder.create();
                                d.show();}
                                else {
                                    Toast.makeText(mContext, "Goals not Editable", Toast.LENGTH_LONG).show();

                                }
                                break;

                            case R.id.action_delete:
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show();
                                String temp = holder.mTextView.getText().toString();
                                holder.db.deleteGoal(temp);
                                for (int i = 0; i< goals.size(); i++){
                                    if (goals.get(i).getName().equals(temp)){
                                        goals.remove(i);
                                       notifyDataSetChanged();
                                    }
                                }
                                break;

                            case R.id.action_set_active:
                                Goal tempGoal = null;
                               // Goal bob = null;

                                Conversion conv = new Conversion(mContext);
                                TextView txtView = (TextView) ((Activity)mContext).findViewById(R.id.Active_Goal_Name);
                                TextView current = (TextView) ((Activity)mContext).findViewById(R.id.currentSteps5) ;
                                TextView txtView2 = (TextView) ((Activity)mContext).findViewById(R.id.currentSteps6);
                                Toast.makeText(mContext, "Set As Active", Toast.LENGTH_LONG).show();
                                String tempTwo = holder.mTextView.getText().toString();
                                String title = txtView.getText().toString();
                                if (title.equals("No Goal Set")){
                                    holder.db.setActiveGoal(tempTwo, date);
                                   // Toast.makeText(mContext, "yes? " + title, Toast.LENGTH_LONG).show();
                                    for (int i =0; i<goals.size();i++){
                                        if (goals.get(i).getName().equals(tempTwo)){
                                            tempGoal = goals.get(i);

                                            goals.remove(i);
                                        }
                                    }

                                    String[] converted = conv.convert(tempGoal);
                                    txtView.setText(tempGoal.getName());
                                    current.setText(converted[0]+ " " + converted[1]);
                                    txtView2.setText(converted[2]+ " "+ converted[3]);
                                    notifyDataSetChanged();
                                }else {
                                    goals.add(holder.db.getActiveGoal(date));
                                    //
                                    int Step = holder.db.getActiveGoal(date).getSteps();
                                    holder.db.updateStepsforGoal(0, holder.db.getActiveGoal(date).getName(),date);
                                    holder.db.setActiveGoal(tempTwo, date);
                                    holder.db.updateStepsforGoal(Step, holder.db.getActiveGoal(date).getName(),date);
                                    Goal goal = holder.db.getGoal(tempTwo, date);

                                    for (int i = 0; i < goals.size(); i++) {
                                        if (goals.get(i).getName().equals(tempTwo)) {
                                            tempGoal = goals.get(i);
                                            tempGoal.setSteps(Step);
                                            goals.remove(i);
                                            notifyDataSetChanged();
                                        }

                                    }
                                    txtView.setText(tempGoal.getName());
                                    String[] converted = conv.convert(tempGoal);
                                   // txtView.setText(tempGoal.getName());
                                    current.setText(converted[0]+ " " + converted[1]);
                                    txtView2.setText(converted[2]+ " "+ converted[3]);
                                   // txtView2.setText(Integer.toString(tempGoal.getTarget()));
                                    CardView cv = (CardView) ((Activity)mContext).findViewById(R.id.card_view_active);
                                    ProgressBar progress = (ProgressBar) cv.findViewById(R.id.progressBarActive) ;
                                    Double cCom = ((double)goal.getSteps()/goal.getTarget()) * 100;
                                    int progressPercentage = (int)Math.round(cCom);
                                    TextView output = (TextView) ((Activity)mContext).findViewById(R.id.textView11) ;
                                    progress.setProgress(progressPercentage);
                                    output.setText(Integer.toString(progressPercentage) + "%");
                                }notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                // "yyyy-MM-dd HH:mm:ss", Locale.getDefault();
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }
}