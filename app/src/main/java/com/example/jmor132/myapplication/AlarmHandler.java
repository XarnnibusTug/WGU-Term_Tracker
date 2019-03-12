package com.example.jmor132.myapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.logging.SocketHandler;

public class AlarmHandler extends BroadcastReceiver {

    public static final String courseAlarm = "courseAlarms";
    public static final String assessmentAlarm = "assessmentAlarm";
    public static final String alarm = "alarm";
    public static final String nextAlarm = "nextAlarm";



    @Override
    public void onReceive(Context context, Intent intent) {
        String destination = intent.getStringExtra("destination");
        if(destination == null || destination.isEmpty()){
            destination = "";
        }

        int id = intent.getIntExtra("id", 0);
        String alarmName = intent.getStringExtra("alarmName");
        String alarmText = intent.getStringExtra("text");
        int nextAlarmID = intent.getIntExtra("nextAlarmID", getAndAddNextAlarmID(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_calendar_clock)
                .setContentTitle(alarmName)
                .setContentText(alarmText);

        Intent resultIntent;
        Uri uri;
        SharedPreferences sharedPreferences;

        switch(destination){
            case "course":
                Course course = CourseDataManager.getCourse(context, id);
                if (course != null && course.notifications == 1){
                    resultIntent = new Intent(context, CourseViewActivity.class);
                    uri = Uri.parse(CourseProvider.COURSES_URI + "/" + id);
                    resultIntent.putExtra(CourseProvider.COURSE_CONTENT_TYPE, uri);
                }
                else{
                    return;
                }
                break;
                //add assessement

            default:
                resultIntent = new Intent(context, MainActivity.class);
                break;
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent).setAutoCancel(true);
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nextAlarmID, builder.build());


    }

    public static boolean courseAlarm(Context context, long id, long time, String title, String text){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextAlarmID = getNextAlarmID(context);
        Intent intentAlarm = new Intent(context, AlarmHandler.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("title", title);
        intentAlarm.putExtra("text", text);
        intentAlarm.putExtra("destination", "course");
        intentAlarm.putExtra("nextAlarmID", nextAlarmID);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, nextAlarmID, intentAlarm, PendingIntent.FLAG_ONE_SHOT));

        SharedPreferences sharedPreferences = context.getSharedPreferences(courseAlarm, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Long.toString(id), nextAlarmID);
        editor.commit();

        addNextAlarmId(context);
        return true;
    }

    private static int getNextAlarmID(Context context){
        SharedPreferences alarmPreferances;
        alarmPreferances = context.getSharedPreferences(alarm, Context.MODE_PRIVATE);
        int nextAlarmID = alarmPreferances.getInt(nextAlarm, 1);
        return nextAlarmID;
    }

    private static void addNextAlarmId(Context context){
        SharedPreferences alarmPreferances;
        alarmPreferances = context.getSharedPreferences(alarm, Context.MODE_PRIVATE);
        int nextAlarmID = alarmPreferances.getInt(nextAlarm, 1);
        SharedPreferences.Editor alarmEditor = alarmPreferances.edit();
        alarmEditor.putInt(nextAlarm, nextAlarmID + 1);
        alarmEditor.commit();
    }

    private static int getAndAddNextAlarmID(Context context){
        int nextAlarmID = getNextAlarmID(context);
        addNextAlarmId(context);
        return nextAlarmID;


    }
}
