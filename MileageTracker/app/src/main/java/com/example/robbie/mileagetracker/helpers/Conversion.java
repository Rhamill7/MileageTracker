package com.example.robbie.mileagetracker.helpers;

import android.content.Context;

import com.example.robbie.mileagetracker.Goal;
import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;

/**
 * Created by Robbie on 17/03/2017.
 */

public class Conversion {
Context context;
public Conversion(Context context){
    this.context = context;
}

    private double strideLength = 0.5;

    public String[] convert(Goal goal){

        String units = goal.getUnits();
        String end = "";
        int steps= 0,targetGoal = 0;
        switch (units) {
            case "Steps":
                steps = goal.getSteps();
                targetGoal= goal.getTarget();
                end = "Steps";
             //   Toast.makeText(getContext(), "Steps", Toast.LENGTH_LONG).show();
                break;
            case "Meters":
                steps= stepsToMeters(goal.getSteps());
                targetGoal = stepsToMeters(goal.getTarget());
                end = "m";
              //  Toast.makeText(getContext(), Integer.toString(steps), Toast.LENGTH_LONG).show();
                break;
            case "Kilometers":
                steps= stepsToKiloMeters(goal.getSteps());
                targetGoal = stepsToKiloMeters(goal.getTarget());
                end = "km";
            //    Toast.makeText(getContext(), Integer.toString(steps), Toast.LENGTH_LONG).show();
                break;
            case  "Yards":
                steps= stepsToYards(goal.getSteps());
                targetGoal = stepsToYards(goal.getTarget());
                end = "Yds";
                //  Toast.makeText(getContext(), Integer.toString(steps), Toast.LENGTH_LONG).show();
                break;
            case "Miles":
                steps= stepsToMiles(goal.getSteps());
                targetGoal = stepsToMiles(goal.getTarget());
                end = "Miles";
              //  Toast.makeText(getContext(), Integer.toString(steps), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        String[] conversions = {Integer.toString(steps),end,Integer.toString(targetGoal),end};
        return conversions;
    }
    public int stepsToMeters(int steps){
        double stride = 0.5;
        try {
            FeedReaderDbHelper db = new FeedReaderDbHelper(context);
            stride=db.getStride();
        }catch (Exception e){ //stride = 0.5;
             }

        int i = (int) ((double) steps * stride);
        return i;
    }

    public int stepsToKiloMeters(int steps){
        int i = (int)((double)stepsToMeters(steps) * 0.001);
        return i;
    }

    public int stepsToYards(int steps){
        int i = (int)((double)stepsToMeters(steps) * 1.09361);
        return i;
    }

    public int stepsToMiles(int steps){
        int i = (int)((double)stepsToMeters(steps) * 0.000621371);
        return i;
    }
}
