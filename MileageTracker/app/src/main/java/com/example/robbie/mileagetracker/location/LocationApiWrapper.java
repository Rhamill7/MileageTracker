package com.example.robbie.mileagetracker.location;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.robbie.mileagetracker.database.FeedReaderDbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class LocationApiWrapper implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private Context mContext;
    private final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates = true;
    FeedReaderDbHelper db = new FeedReaderDbHelper(mContext);

    public LocationApiWrapper(Context context) {
        mContext = context;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                startLocationUpdates();
            }
        }, 2000);

    }

    public Location getCurrentLocation() {
        Location location = db.getCurrentLocation();
        return location;
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        db.storeLocation(location, mContext);
        Toast.makeText(mContext, "olc: lat: " + String.valueOf(mCurrentLocation.getLatitude()) + "long: " + String.valueOf(mCurrentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        Log.d("OnlocationChanged","it changed");
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(this.getClass().getSimpleName(), "Permission Denied");
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
