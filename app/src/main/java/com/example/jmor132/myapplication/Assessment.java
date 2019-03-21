package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;

public class Assessment {

    public long assessmentID;
    public long courseID;
    public String assessmentCode;
    public String assessmentName;
    public String assessmentDescription;
    public String dateTime;
    public int notifications;

    public void saveChanges(Context context){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseID);
        values.put(DBOpenHelper.ASSESSMENT_CODE, assessmentCode);
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, assessmentDescription);
        values.put(DBOpenHelper.ASSESSMENT_DATETIME, dateTime);
        values.put(DBOpenHelper.ASSESSMENT_NOTIFICATIONS, notifications);
        context.getContentResolver().update(AssessmentProvider.ASSESSMENT_URI, values, DBOpenHelper.ASSESSMENT_TABLE_ID + " = " + assessmentID, null);
    }

}
