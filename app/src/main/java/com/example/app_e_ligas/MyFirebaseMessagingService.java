package com.example.app_e_ligas;// MyFirebaseMessagingService.java
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.namespace.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if the message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Create and show a notification.
            sendNotification(title, body);
        }
    }

    private void sendNotification(String title, String body) {
        String channelId = "default_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent;

        if (title.toLowerCase().contains("flood")) {
            intent = new Intent(this, dam_monitoringActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Custom layout for flood notifications
            RemoteViews notificationLayout = new RemoteViews(this.getPackageName(), R.layout.notification_flood_small);
            RemoteViews notificationLayoutExpanded = new RemoteViews(this.getPackageName(), R.layout.notification_flood_large);

            notificationLayout.setTextViewText(R.id.notification_title, title);
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, title);
            notificationLayoutExpanded.setTextViewText(R.id.notification_body, body);

            int bgFlood = R.drawable.flood_yellow;
            if (title.contains("Red")) {
                bgFlood = R.drawable.flood_red;
            } else if (title.contains("Orange")) {
                bgFlood = R.drawable.flood_orange;
            } else if (title.contains("Violet")) {
                bgFlood = R.drawable.flood_violet;
            }

            notificationLayout.setInt(R.id.notification_parent_layout, "setBackgroundResource", bgFlood);
            notificationLayoutExpanded.setInt(R.id.notification_parent_layout, "setBackgroundResource", bgFlood);

            Notification pendingNotif = new NotificationCompat.Builder(this, NotificationHelper.getChannelId())
                    .setSmallIcon(R.drawable.logo_ligas1)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .setColor(ContextCompat.getColor(this, R.color.md_yellow_600)) // Set the color explicitly
                    .setOngoing(false)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(667, pendingNotif);

        } else {
            if (title.equals("Emergency Report Update")) {
                intent = new Intent(this, barangay_emergencyActivity.class);
            } else if (title.toLowerCase().contains("flood")){
                intent = new Intent(this, dam_monitoringActivity.class);
            }else {
                intent = new Intent(this, barangay_servicesActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_ligas1)
                    .setStyle(bigTextStyle);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Create Notification Channel for Oreo and above.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(667, notificationBuilder.build());
        }
    }

}
