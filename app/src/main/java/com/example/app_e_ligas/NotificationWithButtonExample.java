package com.example.app_e_ligas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.widget.Button;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;

import com.example.namespace.R;

public class NotificationWithButtonExample {

    private static final String CHANNEL_ID = "lib_name";
    private static final int NOTIFICATION_ID = 666;

    public static void createNotificationWithButton(Context context, int notification_with_button_layout) {
        // Create NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification with Button";
            String description = "Channel for notifications with a button";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // Create custom layout for notification
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), notification_with_button_layout);

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_ligas1)
                .setContent(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}

