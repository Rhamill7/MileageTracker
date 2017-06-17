package com.example.robbie.mileagetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.robbie.mileagetracker.Goal;

import java.util.ArrayList;

/**
 * Created by Robbie on 09/02/2017.
 */



public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
  //  public static final int DATABASE_VERSION = 1;
   // public static final String DATABASE_NAME = "FeedReader.db";
    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 10;
    public static final String GOAL_TABLE_NAME = "goal";
    public static final String GOAL_COLUMN_ID = "_id";
    public static final String GOAL_COLUMN_NAME = "name";
    public static final String GOAL_COLUMN_STEPS = "steps";
    public static final String GOAL_COLUMN_TARGET = "target";
    public static final String GOAL_COLUMN_DATE = "date";
    public static final String GOAL_COLUMN_ACTIVE = "active";
    public static final String GOAL_COLUMN_UNITS = "units";

    public static final String TEST_TABLE_NAME = "test";
    public static final String TEST_COLUMN_ID = "_id";
    public static final String TEST_COLUMN_NAME = "name";
    public static final String TEST_COLUMN_DATE = "date";
    public static final String TEST_COLUMN_ACTIVE = "active";
    public static final String TEST_COLUMN_UNITS = "units";
    public static final String TEST_COLUMN_EDIT = "editable";
    public static final String TEST_COLUMN_STRIDE = "stride";
    public static final String TEST_COLUMN_INIT = "init";        ;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GOAL_TABLE_NAME +
                "(" + GOAL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                GOAL_COLUMN_NAME + " TEXT, " +
                GOAL_COLUMN_STEPS + " INTEGER, " +
                GOAL_COLUMN_TARGET + " INTEGER, " +
                GOAL_COLUMN_DATE + " TEXT, " +
                GOAL_COLUMN_ACTIVE + " INTEGER, " +
                GOAL_COLUMN_UNITS + " TEXT)");

        db.execSQL("CREATE TABLE " + TEST_TABLE_NAME +
                "(" + TEST_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                TEST_COLUMN_NAME + " TEXT, " +
                TEST_COLUMN_DATE + " TEXT, " +
                TEST_COLUMN_ACTIVE + " INTEGER, " +
                TEST_COLUMN_UNITS + " TEXT, " +
                TEST_COLUMN_EDIT + " INTEGER, " +
                TEST_COLUMN_STRIDE+ " REAL, " +
                TEST_COLUMN_INIT + " INTEGER)");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    ////////////////////////////Test Mode//////////////////////////////

    public boolean initialiseSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_COLUMN_NAME, "Test");
        contentValues.put(TEST_COLUMN_DATE, "None");
        contentValues.put(TEST_COLUMN_ACTIVE, 0);
        contentValues.put(TEST_COLUMN_UNITS, "Default");
        contentValues.put(TEST_COLUMN_EDIT, 1);
        contentValues.put(TEST_COLUMN_STRIDE, 0.5);
        contentValues.put(TEST_COLUMN_INIT, 1);

        db.insert(TEST_TABLE_NAME, null, contentValues);
        return true;
    }

    public void setUnits(String units) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEST_COLUMN_UNITS,units);
        // args.put(TEST_COLUMN_ACTIVE, active);
        db.update(TEST_TABLE_NAME, args, TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});
    }

    public String getUnits() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + "=?", new String[]{"Test"});
        cursor.moveToFirst();
        String act = cursor.getString(4);
        return act;
    }
    public void setStride(double stride) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEST_COLUMN_STRIDE,stride);
        // args.put(TEST_COLUMN_ACTIVE, active);
        db.update(TEST_TABLE_NAME, args, TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});
    }

    public double getStride() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + "=?", new String[]{"Test"});
        cursor.moveToFirst();
        double act = Integer.parseInt(cursor.getString(6));
        return act;
    }
    public void updateUnits(String units) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEST_COLUMN_UNITS,units);
      // args.put(TEST_COLUMN_ACTIVE, active);
        db.update(TEST_TABLE_NAME, args, TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});

    }

    public void updateEditable(int edit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEST_COLUMN_EDIT,edit);
        db.update(TEST_TABLE_NAME, args, TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});

    }
    public void updateTestMode(String date, int active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEST_COLUMN_DATE,date);
        args.put(TEST_COLUMN_ACTIVE, active);
        db.update(TEST_TABLE_NAME, args, TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});

    }

    public boolean testInit() {
        boolean bool = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + "=?", new String[]{"Test"});
        cursor.moveToFirst();
        int act = Integer.parseInt(cursor.getString(7));
        if (act == 1){
            bool = true;
        }
        return bool;
    }
    public boolean testEditable() {
        boolean bool = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + "=?", new String[]{"Test"});
        cursor.moveToFirst();
        int act = Integer.parseInt(cursor.getString(5));
        //     Log.d("HERE", Integer.toString(act));
        if (act == 1){
            bool = true;
        }
        return bool;
    }

    public boolean testActive() {
       boolean bool = false;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + "=?", new String[]{"Test"});
       cursor.moveToFirst();
        //String id = cursor.getString()
       //String date = cursor.getString(2);
       int act = Integer.parseInt(cursor.getString(3));
   //     Log.d("HERE", Integer.toString(act));
       if (act == 1){
         bool = true;
       }
        return bool;
    }

    public String getDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TEST_TABLE_NAME + " WHERE " +
                TEST_COLUMN_NAME + " = ? ", new String[]{"Test"});
        cursor.moveToFirst();
        String date = cursor.getString(2);
        return date;
    }

    ///////////////////////////////////////////////////////////////////////

///////////////*Goal Tables Edits Here*//////////////////////
    public boolean insertGoal(String name, int steps, int target, String date, int active, String units) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_COLUMN_NAME, name);
        contentValues.put(GOAL_COLUMN_STEPS, steps);
        contentValues.put(GOAL_COLUMN_TARGET, target);
        contentValues.put(GOAL_COLUMN_DATE, date);
        contentValues.put(GOAL_COLUMN_ACTIVE, active);
        contentValues.put(GOAL_COLUMN_UNITS, units);
        db.insert(GOAL_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, GOAL_TABLE_NAME);
        return numRows;
    }

    public boolean updateGoal( String oldName, String newName, int oldTarget, int newTarget, String units) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(GOAL_COLUMN_NAME,newName);
        args.put(GOAL_COLUMN_TARGET, newTarget);
        args.put(GOAL_COLUMN_UNITS, units);
        db.update(GOAL_TABLE_NAME, args, GOAL_COLUMN_NAME + " = ? AND " + GOAL_COLUMN_TARGET + " = ? ", new String[]{oldName, Integer.toString(oldTarget)});
        return true;
    }

    public void deleteGoal(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GOAL_TABLE_NAME,
            GOAL_COLUMN_NAME + " = ? ", new String[] { name });
    }

    public void deleteAllGoals(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ GOAL_TABLE_NAME);
    }

    public boolean checkGoalExists(String name, String date) {
       boolean check = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE " +
                GOAL_COLUMN_NAME + " = ? AND " + GOAL_COLUMN_DATE + " = ? ", new String[]{name,date});
        cursor.moveToFirst();
        try {
            String goalName = cursor.getString(1);
            int steps = Integer.parseInt(cursor.getString(2));
            int target = Integer.parseInt(cursor.getString(3));
            String gdate = cursor.getString(4);
            int active = Integer.parseInt(cursor.getString(5));
            String units = cursor.getString(6);
            check = true;
        } catch (Exception e){

        }

        return check;
    }

    public Goal getGoal(String name, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE " +
                GOAL_COLUMN_NAME + " = ? AND " + GOAL_COLUMN_DATE + " = ? ", new String[]{name,date});
        cursor.moveToFirst();
        String goalName = cursor.getString(1);
        int steps = Integer.parseInt(cursor.getString(2));
        int target = Integer.parseInt(cursor.getString(3));
        String gdate = cursor.getString(4);
        int active = Integer.parseInt(cursor.getString(5));
        String  units = cursor.getString(6);

        Goal object = new Goal(name, steps, target, gdate, active, units );
        return object;
    }

    public ArrayList<Goal> getGoalsOnDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE " +
                GOAL_COLUMN_DATE + " = ? AND " + GOAL_COLUMN_ACTIVE + " = ? "  , new String[]{date,Integer.toString(0)});
        ArrayList<Goal> goals = new ArrayList<Goal>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //the .getString(int x) method of the cursor returns the column
                //of the table your query returned
                String name = cursor.getString(1);
                int steps = Integer.parseInt(cursor.getString(2));
                int target = Integer.parseInt(cursor.getString(3));
                String gdate = cursor.getString(4);
                int active = Integer.parseInt(cursor.getString(5));
                String  units = cursor.getString(6);

                Goal object = new Goal(name, steps, target, gdate, active, units );
                // Adding contact to list
                goals.add(object);
            } while (cursor.moveToNext());
        }
        return goals;

    }

    public Goal getActiveGoal(String date) {

        String boop = GOAL_COLUMN_DATE + "=" + date;
        int t = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE " +
                GOAL_COLUMN_ACTIVE + " = ? AND " + GOAL_COLUMN_DATE + " = ? ", new String[]{(Integer.toString(t)),date});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        int steps = Integer.parseInt(cursor.getString(2));
        int target = Integer.parseInt(cursor.getString(3));
        String gDate = cursor.getString(4);
        int active = Integer.parseInt(cursor.getString(5));
        String  units = cursor.getString(6);
        Goal object = new Goal(name, steps, target, gDate, active, units );
        return object;
    }

    public void setActiveGoal(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int k = 0, i = 1;
        ContentValues ct = new ContentValues();
        ct.put(GOAL_COLUMN_ACTIVE, k);
        db.update(GOAL_TABLE_NAME, ct, GOAL_COLUMN_ACTIVE + " = ? AND " + GOAL_COLUMN_DATE +
                " = ? ", new String[]{Integer.toString(i),date});
        ContentValues args = new ContentValues();
        args.put(GOAL_COLUMN_ACTIVE,i);
        db.update(GOAL_TABLE_NAME, args, GOAL_COLUMN_NAME + " = ? AND " + GOAL_COLUMN_DATE +
                " = ? ", new String[]{name,date});
    }

    public void updateStepsforGoal(int steps, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(GOAL_COLUMN_STEPS,steps);
        db.update(GOAL_TABLE_NAME, args, GOAL_COLUMN_NAME + " = ? AND " + GOAL_COLUMN_DATE + " = ? ", new String[]{name,date});

    }

    public ArrayList<Goal> getHistory(String startDate, String endDate){
       ArrayList<Goal> history = new ArrayList<Goal>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ GOAL_TABLE_NAME +
                " WHERE " + GOAL_COLUMN_DATE +
                " BETWEEN ?  AND ?", new String[]{startDate, endDate});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //the .getString(int x) method of the cursor returns the column
                //of the table your query returned
                String name = cursor.getString(1);
                int steps = Integer.parseInt(cursor.getString(2));
                int target = Integer.parseInt(cursor.getString(3));
                String dates = cursor.getString(4);
                int active = Integer.parseInt(cursor.getString(5));
                String  units = cursor.getString(6);
                Goal object = new Goal(name, steps, target, dates,active, units);
                // Adding contact to list
              if (active == 1){
                history.add(object);}


            } while (cursor.moveToNext());
        }

        return history;
    }

    public Cursor getInactiveGoals(){
    int t = 0;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor =  db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE " +
            GOAL_COLUMN_ACTIVE + "=?", new String[]{(Integer.toString(t))});
        return cursor;
    }
    public Cursor getAllGoals() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + GOAL_TABLE_NAME, null );
        return res;
    }
}