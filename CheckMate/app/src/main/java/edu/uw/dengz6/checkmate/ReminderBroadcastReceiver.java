package edu.uw.dengz6.checkmate;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public ReminderBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras() != null){

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_checkmate_logo)
                .setContentTitle("Task reminder")
                .setContentText("task reminder")
                .setAutoCancel(true);
        Intent intentToFire = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intentToFire);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }
}
