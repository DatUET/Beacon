package com.example.beaconapp;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.example.beaconapp.model.Beacon;
import com.example.beaconapp.model.BeaconNotificationsManager;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static final String DEFAULT_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private boolean beaconNotificationEnable = false;
    private List<Beacon> beaconList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        beaconList.add(new Beacon(DEFAULT_UUID, 1, 1));
        EstimoteSDK.initialize(getApplicationContext(), "datuet180899-gmail-com-s-y-6l1", "fd4a692e75298a26c77d38514da06628");
    }

    public void enableBeaconNotification() {
        if (beaconNotificationEnable) {
            return;
        }
        BeaconNotificationsManager beaconNotificationsManager = new BeaconNotificationsManager(this);
        for (Beacon beacon : beaconList) {
            beaconNotificationsManager.addNotification(beacon, "Enter", "Exit");
        }
        beaconNotificationsManager.startMonitoring();
        beaconNotificationEnable = true;
    }

    public boolean isBeaconNotificationEnable() {
        return beaconNotificationEnable;
    }
}
