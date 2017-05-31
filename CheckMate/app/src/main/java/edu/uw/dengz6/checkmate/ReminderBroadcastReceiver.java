package edu.uw.dengz6.checkmate;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public ReminderBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        Intent intent1 = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent alarmIntent;

        if (action != null && "Snooze".equals(action)) {
            NotificationManagerCompat.from(context).cancel(intent.getIntExtra("id", 0));
            String due = intent.getExtras().getString("due");
            SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
            long timeDue = 0;
            try {
                timeDue = dt.parse(due).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            intent1.putExtra("task_name", intent.getExtras().getString("title"));
            intent1.putExtra("task_due", intent.getExtras().getString("due"));
            intent1.putExtra("task_assigner", intent.getExtras().getString("assigner"));
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT < 19) {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * 5, alarmIntent);
            } else {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * 5,alarmIntent);
            }
        } else if(action != null && "Dismiss".equals(action)){
            NotificationManagerCompat.from(context).cancel(intent.getIntExtra("id", 0));
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(alarmIntent);
        }else{
            String name = "";
            String dueTime = "";
            String assigner = "";
            if (intent.getExtras() != null) {
                name = intent.getExtras().getString("task_name");
                dueTime = intent.getExtras().getString("task_due");
                assigner = intent.getExtras().getString("task_assigner");
            }
            int id = (int) System.currentTimeMillis();

            //Snooze intent
            Intent snoozeReceive = new Intent(context, ReminderBroadcastReceiver.class);
            snoozeReceive.putExtra("id", id);
            snoozeReceive.putExtra("due", dueTime);
            snoozeReceive.putExtra("title", name);
            snoozeReceive.putExtra("assigner", assigner);
            snoozeReceive.setAction("Snooze");
            PendingIntent pendingIntentSnooze = PendingIntent.getBroadcast(context, 12345, snoozeReceive, PendingIntent.FLAG_UPDATE_CURRENT);

            //Dismiss intent
            Intent dismiss = new Intent(context, ReminderBroadcastReceiver.class);
            dismiss.putExtra("id", id);
            dismiss.setAction("Dismiss");
            dismiss.putExtra("due", dueTime);
            PendingIntent pendingIntentDismiss = PendingIntent.getBroadcast(context, 12346, dismiss, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_annoucement)
                    .setContentTitle(name + " is going to be due soon")
                    .setContentText("It is due on " + dueTime + ".\nTask is assigned by " + assigner)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_menu_manage, "Snooze", pendingIntentSnooze)
                    .addAction(R.drawable.ic_menu_send, "Dismiss", pendingIntentDismiss);


            Intent intentToFire = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intentToFire);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat.from(context).notify(id, builder.build());
        }
    }
}
