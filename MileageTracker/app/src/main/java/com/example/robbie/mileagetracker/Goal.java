package com.example.robbie.mileagetracker;

/**
 * Created by Robbie on 08/02/2017.
 */

public class Goal {
    private String name;
    private int steps;
    private int target;
    private String date;
    private boolean active = false;
    private String units;

    public Goal(String name, int steps, int target, String date, int active, String units){
        this.name = name;
        this.steps = steps;
        this.target= target;
        this.date = date;
        this.units = units;

        if (active == 1){
            this.active = true;
        }
        else {
            this.active = false;
        }

    }

    public String getName(){
        return name;
    }

    public int getSteps(){
        return steps;
    }

    public int getTarget(){
        return target;
    }

    public String getDate() {
        return date;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(){
        this.active = true;
    }

    public String getUnits() {
        return units;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
