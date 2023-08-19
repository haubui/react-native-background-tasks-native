package com.backroundtasks;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWorker extends Worker {
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "CircleBackgroundTask_ID";
    private final Context context;

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.w("bg", "Worker do work");

        Bundle extras = new Bundle();
        extras.putLong("time", System.currentTimeMillis());
        // Todo: We need to connect to Bluetooth here and get the data from Circle Ring then pass it in to Bundle
        Intent service = new Intent(this.context, BackgroundHeadlessTaskService.class);
        service.putExtras(extras);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
            getApplicationContext().startForegroundService(service);
        } else {
            this.context.startService(service);
        }
        // background work will take place here
        Log.w("bg", "Worker do work");
        return Result.success();

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        String description = "Circle Background Sync Task";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, "Circle Background Sync Task", importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(channel);

    }
}