package edu.uw.dengz6.checkmate;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public ReminderBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null && "Snooze".equals(action)) {
            Log.v("shuffTest","Pressed Snooze");
        }else {
            String name = "";
            String dueTime = "";
            String assigner = "";
            if (intent.getExtras() != null) {
                name = intent.getExtras().getString("task_name");
                dueTime = intent.getExtras().getString("task_due");
                assigner = intent.getExtras().getString("task_assigner");
            }
            SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
            long timeDue = 0;
            try {
                timeDue = dt.parse(dueTime).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long currentTime = System.currentTimeMillis();
            if(1000 * 60 * 10 >= (timeDue - currentTime) && (timeDue - currentTime) >= 0 ) {
                Intent yesReceive = new Intent(context, ReminderBroadcastReceiver.class);
                yesReceive.setAction("Snooze");
                PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent dismiss = new Intent(context, MainActivity.class);
                yesReceive.setAction("Dismiss");
                PendingIntent pendingIntentDismiss = PendingIntent.getBroadcast(context, 12346, dismiss, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_annoucement)
                        .setContentTitle(name + " is going to be due soon")
                        .setContentText("It is due on " + dueTime + ". Task is assigned by " + assigner)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(R.drawable.ic_add, "Snooze", pendingIntentYes)
                        .addAction(R.drawable.ic_menu_send, "Dismiss", pendingIntentDismiss);

                Intent intentToFire = new Intent(context, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(intentToFire);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
            }else{
                Log.v("Receiver", "overdue");
            }
        }
    }
}
