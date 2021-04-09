package com.example.beaconapp.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.beaconapp.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeaconNotificationsManager {
    private static final String TAG = "BeaconNotifications";
    private BeaconManager beaconManager;
    private List<BeaconRegion> regionsToMonitor = new ArrayList<>();
    private HashMap<String, String> enterMessage = new HashMap<>();
    private HashMap<String, String> exitMessage = new HashMap<>();

    private Context context;
    private int notificationId = 0;

    public BeaconNotificationsManager(Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                String message = enterMessage.get(beaconRegion.getIdentifier());
                if (message != null) {
                    showNotification(message);
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                String message = exitMessage.get(beaconRegion.getIdentifier());
                if (message != null) {
                    showNotification(message);
                }
            }
        });
    }

    public void addNotification(com.example.beaconapp.model.Beacon beacon, String enterMessage, String exitMessage) {
        BeaconRegion region = beacon.toBeaconRegion();
        this.enterMessage.put(region.getIdentifier(), enterMessage);
        this.exitMessage.put(region.getIdentifier(), exitMessage);
        regionsToMonitor.add(region);
    }

    private void showNotification(String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Beacon notifications")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, builder.build());
    }

    public void startMonitoring() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (BeaconRegion region : regionsToMonitor) {
                    beaconManager.startMonitoring(region);
                }
            }
        });
    }
}
