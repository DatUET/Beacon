package com.example.beaconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication application = (MyApplication) getApplication();
        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Bluetooth is turn off");
        } else if (!application.isBeaconNotificationEnable()) {
            application.enableBeaconNotification();
        }
    }
}