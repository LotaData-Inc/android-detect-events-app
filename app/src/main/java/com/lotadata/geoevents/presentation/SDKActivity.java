package com.lotadata.geoevents.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.lotadata.geoevents.R;
import com.lotadata.moments.ConnectionResult;
import com.lotadata.moments.Moments;
import com.lotadata.moments.MomentsClient;
import com.lotadata.moments.events.LDEvent;
import com.lotadata.moments.events.LDEventListener;

public class SDKActivity extends AppCompatActivity {

    private static final String TAG = "GEOEVENTS";

    private Moments mMomentsClient;
    private TextView eventCounterTextView;
    private Toolbar toolbar;

    protected String[] mPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    protected String[] mPermissionsForQ = {Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION};

    protected static final int PERMISSIONS_REQUEST = 100;
    protected int count = 0;
    protected Context mContext = null;
    private LDEventListener mEventListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_activity);

        //Initialize signal counter
        eventCounterTextView = (TextView) findViewById(R.id.eventCounterView);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        eventCounterTextView.setText(getString(R.string.event_count_field_name) + count);

        checkPermissionsAndLaunch();
    }

    protected void checkPermissionsAndLaunch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                for(int i = 0; i < mPermissionsForQ.length; i++) {
                    mPermissions = ArrayUtils.appendToArray(mPermissions, mPermissionsForQ[i]);
                }
            }

            if(!hasPermissions(this, mPermissions)){
                ActivityCompat.requestPermissions(this, mPermissions, PERMISSIONS_REQUEST);
            }
        }
        initializeLotaDataSDK();
    }

    private void initializeLotaDataSDK() {

        MomentsClient.getInstance(this, new MomentsClient.ConnectionListener() {
            @Override
            public void onConnected(Moments client) {
                Toast.makeText(mContext,"Initialization Successful",Toast.LENGTH_LONG).show();
                mMomentsClient = client;

                trackEvents();
            }

            @Override
            public void onConnectionError(ConnectionResult result) {
                Toast.makeText(mContext,"Initialization Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mMomentsClient != null) {
            untrackEvents();
            Toast.makeText(this,"See you!",Toast.LENGTH_LONG).show();
            mMomentsClient.recordEvent("Stop SDK");
            mMomentsClient.disconnect();
        }
        super.onDestroy();
    }

    private static boolean hasPermissions(Activity activity, String... permissions) {
        if (activity != null && permissions != null) {
            for (String permission : permissions) {
                if ((ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) ||
                        (((ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)))){
                    return false;
                }
            }
        }
        return true;
    }

    protected void trackEvents() {
        mEventListener = new LDEventListener() {
            @Override
            public void onEvent(LDEvent ldEvent) {
                //Updates table of events
                    //Sanity check
                    if((ldEvent == null) || (ldEvent.type == null)) return;
                    count++;
                    Toast.makeText(mContext, "Event detected: " + ldEvent.type, Toast.LENGTH_SHORT).show();
                    eventCounterTextView.setText(getString(R.string.event_count_field_name) + count);
            }
        };

        if(mMomentsClient != null) mMomentsClient.trackEvents(mEventListener);
    }

    protected void untrackEvents() {
        if(mMomentsClient != null) mMomentsClient.untrackEvents(mEventListener);
    }
}
