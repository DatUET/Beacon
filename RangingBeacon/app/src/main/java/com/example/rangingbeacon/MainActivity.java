package com.example.rangingbeacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.rangingbeacon.adapter.BeaconAdapter;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

public class MainActivity extends AppCompatActivity {

    public static final String INTENT_BEACON_MAC_ADDRESS = "intent_main";
    public static final String DEFAULT_BEACON_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String DEFAULT_BEACON_ID = "rid";
    private static final BeaconRegion ALL_ESTIMOTE_BEACONS_REGIONS = new BeaconRegion(DEFAULT_BEACON_ID, UUID.fromString(DEFAULT_BEACON_UUID), null, null);

    private BeaconManager beaconManager;
    private BeaconAdapter beaconAdapter;
    RecyclerView recy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recy = findViewById(R.id.recy);
        bindingBeaconManager();
        recy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recy.getChildLayoutPosition(v);
                String macAddress = new Gson().toJson(beaconAdapter.getItemAt(itemPosition).getMacAddress());
                Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra(INTENT_BEACON_MAC_ADDRESS, macAddress);
                startActivity(intent);
            }
        });
    }

    private void bindingBeaconManager() {
        beaconManager = new BeaconManager(this);
        bindingDevice();
        bindingRangingBeacon();
    }

    private void bindingDevice() {
        beaconAdapter = new BeaconAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recy.setAdapter(beaconAdapter);
        recy.setLayoutManager(layoutManager);
    }

    private void bindingRangingBeacon() {
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                beaconAdapter.replaceWith(beacons);
                            }
                        }
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconManager != null) {
            beaconManager.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    @Override
    protected void onStop() {
        if (beaconManager != null) {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGIONS);
        }
        super.onStop();
    }

    private void startScanning() {
        if (beaconAdapter != null && beaconManager != null) {
            beaconAdapter.replaceWith(Collections.emptyList());
            beaconManager.connect(() -> beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGIONS));
        }
    }
}