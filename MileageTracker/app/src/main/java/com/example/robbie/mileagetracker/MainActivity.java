package com.example.robbie.mileagetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  Toolbar myToolbar = (Toolbar) findViewById(R.id.);
        // setSupportActionBar(myToolbar);

        SQLiteDatabase db=openOrCreateDatabase("GoalsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS goal(steps VARCHAR,date VARCHAR,goal VARCHAR);");


        android.app.FragmentManager manager = getFragmentManager();
       // GoalsFragment goalsFragment = new GoalsFragment();
       // manager.beginTransaction().replace(R.id.content_summary_screen, goalsFragment, goalsFragment.getTag()).commit();

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
       /* int id = item.getItemId();

        if (id == R.id.nav_summary) {
            GoalsFragment goalsFragment = new GoalsFragment();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, goalsFragment, goalsFragment.getTag()).commit();
        } else if (id == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, historyFragment, historyFragment.getTag()).commit();
        } else if (id == R.id.nav_stats) {
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, statisticsFragment, statisticsFragment.getTag()).commit();
        }  else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, settingsFragment, settingsFragment.getTag()).commit();
        } else if (id == R.id.nav_test) {
            PedometerFragment pedometerFragment = new PedometerFragment();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_summary_screen, pedometerFragment, pedometerFragment.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }
}
