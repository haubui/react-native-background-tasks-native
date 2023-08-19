package com.backroundtasks;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class BackgroundTaskModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "ACircularBackgroundTask";
    private static final String UniquePeriodicWork = "ACircularBackgroundTaskUniquePeriodicWork";
    private static final int REPEAT_AFTER = 15;
    private static final int REPEAT_AFTER_FLEXIBLE_TIME = 2;


    private Context mContext;
    private PeriodicWorkRequest workRequest;

    BackgroundTaskModule(ReactApplicationContext context) {
        super(context);
        mContext = context;

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .build();

        workRequest = new PeriodicWorkRequest.Builder(BackgroundWorker.class, REPEAT_AFTER, TimeUnit.MINUTES, REPEAT_AFTER_FLEXIBLE_TIME, TimeUnit.MINUTES)
                .setConstraints(constraints).build();
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void startBackgroundWork() {
        WorkManager.getInstance(mContext).enqueueUniquePeriodicWork(UniquePeriodicWork, ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }

    @ReactMethod
    public void stopBackgroundWork() {
        WorkManager.getInstance(mContext).cancelUniqueWork(UniquePeriodicWork);
    }

    @ReactMethod
    public void createLogEvent(String name, String location) {
        Log.d("ACircularBackgroundTask", "Create event called with name: " + name + " and location: " + location);
    }
}
