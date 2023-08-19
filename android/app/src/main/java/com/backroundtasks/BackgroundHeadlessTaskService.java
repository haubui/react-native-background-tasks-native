package com.backroundtasks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

public class BackgroundHeadlessTaskService extends HeadlessJsTaskService {
    private static final int ONGOING_NOTIFICATION_ID = 1001;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "CircleBackgroundTask_ID";
    @Override
    protected @Nullable
    HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                            PendingIntent.FLAG_IMMUTABLE);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_DEFAULT_IMPORTANCE)
                    .setContentTitle("Circle Background Sync Task")
                    .setContentText("Circle Background Sync Task that connect to Circle Ring to get data")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("Circle Background Sync Task")
                    .setOngoing(true)
                    .build();
            startForeground(ONGOING_NOTIFICATION_ID, notification);
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return new HeadlessJsTaskConfig(
                    "ACircleBackgroundHeadlessTask",
                    Arguments.fromBundle(extras),
                    5000, // timeout for the task
                    true // optional: defines whether or not  the task is allowed in foreground. Default is false
            );
        }


        return null;
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