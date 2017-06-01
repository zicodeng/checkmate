package edu.uw.dengz6.checkmate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Firebase_Messaging";

    public static final String MENU_FRAGMENT_KEY = "Menu_Fragment_Key";

    // Notification ID
    private static final int SHOPPING_NOTIFICATION_ID = 1;
    private static final int ANNOUNCEMENT_NOTIFICATION_ID = 2;
    private static final int TASKS_NOTIFICATION_ID = 2;

    // Pending intent ID
    private static final int SHOPPING_PENDING_INTENT_ID = 1;
    private static final int ANNOUNCEMENT_PENDING_INTENT_ID = 2;
    private static final int TASKS_PENDING_INTENT_ID = 2;

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();

            // Handle "Shopping Notification"
            if(notificationTitle.equalsIgnoreCase("shopping")) {

                // Create a "Shopping Intent"
                Intent shoppingIntent = new Intent(getApplicationContext(), MainActivity.class);

                // When the user clicks the notification, navigate him to the "Shopping" menu
                shoppingIntent.putExtra(MENU_FRAGMENT_KEY, "Shopping");

                // Create a "Shopping Pending Intent"
                PendingIntent shoppingPendingIntent = PendingIntent.getActivity(
                        this,
                        SHOPPING_PENDING_INTENT_ID,
                        shoppingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                // Build a shopping notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_checkmate_logo)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationBody)
                        .setContentIntent(shoppingPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(SHOPPING_NOTIFICATION_ID, mBuilder.build());

            } else if (notificationTitle.equalsIgnoreCase("tasks")) {

                // Handle "Tasks Notification"

                // Create a "Tasks Intent"
                Intent tasksIntent = new Intent(getApplicationContext(), MainActivity.class);

                // Create a "Tasks Pending Intent"
                PendingIntent tasksPendingIntent = PendingIntent.getActivity(
                        this,
                        TASKS_PENDING_INTENT_ID,
                        tasksIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                // Build a shopping notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_checkmate_logo)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationBody)
                        .setContentIntent(tasksPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(TASKS_NOTIFICATION_ID, mBuilder.build());


            } else if (notificationTitle.equalsIgnoreCase("announcement")) {
                // Handle "Announcement Notification"

                // Create a "Announcement Intent"
                Intent AnnouncementIntent = new Intent(getApplicationContext(), MainActivity.class);

                // When the user clicks the notification, navigate him to the "Shopping" menu
                AnnouncementIntent.putExtra(MENU_FRAGMENT_KEY, "Announcement");

                // Create a "Shopping Pending Intent"
                PendingIntent announcementPendingIntent = PendingIntent.getActivity(
                        this,
                        ANNOUNCEMENT_PENDING_INTENT_ID,
                        AnnouncementIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                // Build a shopping notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_checkmate_logo)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationBody)
                        .setContentIntent(announcementPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(ANNOUNCEMENT_NOTIFICATION_ID, mBuilder.build());
            }
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}