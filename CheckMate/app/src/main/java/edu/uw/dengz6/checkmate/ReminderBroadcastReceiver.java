package edu.uw.dengz6.checkmate;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
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
            intent1.putExtra("task_id", intent.getExtras().getInt("task_id"));
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
            String flag = "";
            int id = 0;
            if (intent.getExtras() != null) {
                name = intent.getExtras().getString("task_name");
                dueTime = intent.getExtras().getString("task_due");
                assigner = intent.getExtras().getString("task_assigner");
                id = intent.getIntExtra("task_id", 0);
                flag = intent.getStringExtra("flag");

            }

            //Snooze intent
            Intent snoozeReceive = new Intent(context, ReminderBroadcastReceiver.class);
            snoozeReceive.putExtra("id", id);
            snoozeReceive.putExtra("due", dueTime);
            snoozeReceive.putExtra("task_id", id);
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
            NotificationCompat.Builder builder;
            if(flag.equals("day")) {
                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_time)
                        .setContentTitle(name + " is going to be due in less than 24 hour")
                        .setContentText("It is due on " + dueTime)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
            }else if(flag.equals("minutes")){
                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_annoucement)
                        .setContentTitle(name + " is going to be due soon")
                        .setContentText("It is due on " + dueTime + ".\nTask is assigned by " + assigner)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .addAction(R.drawable.ic_snooze, "Snooze", pendingIntentSnooze)
                        .addAction(R.drawable.ic_exit, "Dismiss", pendingIntentDismiss);

            }else{

                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_diss)
                        .setContentTitle(name + " is overdue, but it has yet been marked completed")
                        .setContentText("Have you completed this task yet?")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL);
            }


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
