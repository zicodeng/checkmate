package edu.uw.dengz6.checkmate;

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

    // Pending intent ID
    private static final int SHOPPING_PENDING_INTENT_ID = 1;

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
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationBody)
                        .setContentIntent(shoppingPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(SHOPPING_NOTIFICATION_ID, mBuilder.build());

            } else if (notificationTitle.equalsIgnoreCase("tasks")) {

                // Handle "Tasks Notification"

            } else if (notificationTitle.equalsIgnoreCase("announcement")) {

                // Handle "Announcement Notification"
            }
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}