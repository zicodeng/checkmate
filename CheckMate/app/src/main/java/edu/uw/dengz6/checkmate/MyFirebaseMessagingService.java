package edu.uw.dengz6.checkmate;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v(TAG, remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();

            // Handle "Shopping Notification"
            if(notificationTitle.equalsIgnoreCase("shopping")) {

                // Build a shopping notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_checkmate_logo)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationBody);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(1, mBuilder.build());

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