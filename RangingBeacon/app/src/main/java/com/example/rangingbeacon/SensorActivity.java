package com.example.rangingbeacon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.estimote.coresdk.cloud.model.BeaconInfo;
import com.estimote.coresdk.recognition.utils.MacAddress;
import com.estimote.coresdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Connection;
import com.estimote.mgmtsdk.common.exceptions.EstimoteDeviceException;
import com.estimote.mgmtsdk.connection.api.BeaconConnection;
import com.estimote.mgmtsdk.feature.settings.api.MotionState;
import com.estimote.mgmtsdk.feature.settings.mapping.Property;
import com.google.gson.Gson;

public class SensorActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    TextView celsiusTV, stateTV;
    private BeaconConnection connection;
    private MacAddress macAddress;
    private Handler tempHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        celsiusTV = findViewById(R.id.celsius);
        stateTV = findViewById(R.id.state);

        String beaconMacAddress = getIntent().getStringExtra(MainActivity.INTENT_BEACON_MAC_ADDRESS);
        macAddress = new Gson().fromJson(beaconMacAddress, MacAddress.class);
        bindingBeaconConnection();
    }

    private void bindingBeaconConnection() {
        connection = new BeaconConnection(this, macAddress);
        connection.addConnectionCallback(new BeaconConnection.ConnectionCallback() {
            @Override
            public void onAuthorized(BeaconInfo beaconInfo) {
                Log.v(LOG_TAG, "Start auth beacon | MAC: " + beaconInfo.macAddress);
            }

            @Override
            public void onConnected(BeaconInfo beaconInfo) {
                Log.v(LOG_TAG, "Connected with beacon | MAC: " + beaconInfo.macAddress);
                if (connection.isConnected()) {
                    connection.edit().set(connection.motionDetectionEnabled(), true).commit(new BeaconConnection.WriteCallback() {
                        @Override
                        public void onSuccess() {
                            Log.v(LOG_TAG, "Enable Success beacon | MAC: " + beaconInfo.macAddress);
                            setTemp(connection.temperature().get());
                            enableMotionListener(connection);
                            refreshTemp();
                        }

                        @Override
                        public void onError(EstimoteDeviceException exception) {
                            Log.v(LOG_TAG, "Fail to enable motion detecttion beacon | MAC: " + beaconInfo.macAddress + " | Error: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(EstimoteDeviceException exception) {

            }

            @Override
            public void onDisconnected() {

            }
        });

        if (!connection.isConnected()) {
            connection.authenticate();
        }
    }

    private void refreshTemp() {
        connection.temperature().getAsync(new Property.Callback<Float>() {
            @Override
            public void onValueReceived(Float value) {
                if (isDestroyed()) {
                    return;
                }
                setTemp(value);
                tempHandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTemp();
                    }
                }, 1000);
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void setTemp(Float value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                celsiusTV.setText(String.format("%.1 (in Celsius)", value));
            }
        });
    }

    private void enableMotionListener(BeaconConnection connection) {
        connection.setMotionListener(new Property.Callback<MotionState>() {
            @Override
            public void onValueReceived(MotionState value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setMotionText(value);
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setMotionText(MotionState value) {
        if (value != null) {
            stateTV.setText(value.toString());
        }
    }
}