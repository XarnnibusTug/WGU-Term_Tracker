package com.example.jmor132.myapplication;

import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy h:mm a z", Locale.getDefault());


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long getDateTimestamp(String dateInput){
        try {
            Date date = DateUtil.dateFormat.parse(dateInput + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }
        catch(ParseException e){
            return 0;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long todayLong()
    {
        String currentDate = DateUtil.dateFormat.format(new Date());
        return getDateTimestamp(currentDate);

    }

    public static long todayLongWithTime(){
        return System.currentTimeMillis();
    }

}
