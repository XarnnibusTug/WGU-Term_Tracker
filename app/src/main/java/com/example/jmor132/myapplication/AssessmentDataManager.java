package com.example.jmor132.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class AssessmentDataManager {


    public static Uri insertAssessment(Context context, long courseID, String assessmentCode, String assessmentName, String assessmentDescription, String dateTime){

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseID);
        values.put(DBOpenHelper.ASSESSMENT_CODE, assessmentCode);
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, assessmentDescription);
        values.put(DBOpenHelper.ASSESSMENT_DATETIME, dateTime);
        Uri assessmentUri = context.getContentResolver().insert(AssessmentProvider.ASSESSMENT_URI, values);
        return assessmentUri;

    }

    public static Assessment getAssessment(Context context, long assessmentID){
        Cursor  cursor = context.getContentResolver().query(AssessmentProvider.ASSESSMENT_URI, DBOpenHelper.ASSESSMENT_COLUMNS, DBOpenHelper.ASSESSMENT_TABLE_ID + " = " + assessmentID, null, null);
        cursor.moveToFirst();

        long courseID = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COURSE_ID));
        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME));
        String description = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DESCRIPTION));
        String code = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_CODE));
        String dateTime = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATETIME));
        int notifications = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTIFICATIONS));

        Assessment assessment = new Assessment();
        assessment.assessmentID = assessmentID;
        assessment.courseID = courseID;
        assessment.assessmentName = name;
        assessment.assessmentDescription = description;
        assessment.assessmentCode = code;
        assessment.dateTime = dateTime;
        assessment.notifications = notifications;
        return assessment;
    }

    public static boolean deleteAssessment(Context context, long assessmentID){
        context.getContentResolver().delete(AssessmentProvider.ASSESSMENT_URI, DBOpenHelper.ASSESSMENT_TABLE_ID + " = " + assessmentID, null);
        return true;

    }
}
