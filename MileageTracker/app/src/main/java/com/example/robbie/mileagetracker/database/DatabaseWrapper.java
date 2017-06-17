package com.example.robbie.mileagetracker.database;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Robbie on 07/06/2017.
 */

public class DatabaseWrapper extends SQLiteOpenHelper {

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
    public static final String TEST_COLUMN_INIT = "init";

    public DatabaseWrapper(Context context) {
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
}
