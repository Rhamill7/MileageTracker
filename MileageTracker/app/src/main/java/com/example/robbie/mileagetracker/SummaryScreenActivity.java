package com.example.robbie.mileagetracker;

import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.robbie.mileagetracker.fragments.TripsFragment;
import com.example.robbie.mileagetracker.fragments.HistoryFragment;
import com.example.robbie.mileagetracker.fragments.SettingsFragment;
import com.example.robbie.mileagetracker.fragments.StatisticsFragment;
import com.example.robbie.mileagetracker.fragments.TrackerFragment;

public class SummaryScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  Toolbar myToolbar = (Toolbar) findViewById(R.id.);
       // setSupportActionBar(myToolbar);

        SQLiteDatabase db=openOrCreateDatabase("GoalsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS goal(steps VARCHAR,date VARCHAR,goal VARCHAR);");


       FragmentManager manager = getFragmentManager();
        TripsFragment tripsFragment = new TripsFragment();
        manager.beginTransaction().replace(R.id.content_summary_screen, tripsFragment, tripsFragment.getTag()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_summary) {
            TripsFragment tripsFragment = new TripsFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, tripsFragment, tripsFragment.getTag()).commit();
        } else if (id == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, historyFragment, historyFragment.getTag()).commit();
        } else if (id == R.id.nav_stats) {
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, statisticsFragment, statisticsFragment.getTag()).commit();
        }  else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
           FragmentManager manager = getFragmentManager();
           manager.beginTransaction().replace(R.id.content_summary_screen, settingsFragment, settingsFragment.getTag()).commit();
        } else if (id == R.id.nav_test) {
            TrackerFragment trackerFragment = new TrackerFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, trackerFragment, trackerFragment.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
