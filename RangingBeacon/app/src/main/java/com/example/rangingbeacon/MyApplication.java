package com.example.rangingbeacon;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

public class MyApplication extends Application  {
    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(getApplicationContext(), "datuet180899-gmail-com-s-y-6l1", "fd4a692e75298a26c77d38514da06628");
    }
}
