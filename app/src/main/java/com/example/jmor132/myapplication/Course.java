package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;

public class Course {

    public long courseID;
    public long termID;
    public String courseName;
    public String courseDescription;
    public String courseStart;
    public String courseEnd;
    public CourseStatus status;
    public String mentorName;
    public String mentorPhone;
    public String mentorEmail;
    public int notifications;


    public void saveChange(Context context){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termID);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_DESCRIPTION, courseDescription);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());
        values.put(DBOpenHelper.COURSE_MENTOR, mentorName);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, mentorPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSE_NOTIFICATIONS, notifications);
        context.getContentResolver().update(CourseProvider.COURSES_URI, values, DBOpenHelper.COURSES_TABLE_ID +
                                            " = " + courseID, null);


    }
}
