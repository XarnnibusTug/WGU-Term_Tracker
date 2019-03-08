package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CourseDataManager {

    public static Uri insertCourse(Context context, long termID, String courseName, String courseStart, String courseEnd,
                                   String courseMentor, String mentorPhone, String mentorEmail, CourseStatus status  ){

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termID);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, mentorPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());
        Uri courseUri = context.getContentResolver().insert(CourseProvider.COURSES_URI, values);
        return courseUri;

    }

    public static Course getCourse(Context context, long courseID){
        Cursor cursor = context.getContentResolver().query(CourseProvider.COURSES_URI, DBOpenHelper.COURSES_COLUMNS,
                                                           DBOpenHelper.COURSES_TABLE_ID + " = " + courseID,
                                                        null, null );

        cursor.moveToFirst();
        Long termID = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_TERM_ID));
        String courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        String courseDescription = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_DESCRIPTION));
        String courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_START));
        String courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_END));
        CourseStatus courseStatus = CourseStatus.valueOf(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_STATUS)));
        String courseMentor = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR));
        String mentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_PHONE));
        String mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_EMAIL));
        int courseNotification = (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTIFICATIONS)));

        Course course = new Course();
        course.courseID = courseID;
        course.termID = termID;
        course.courseName = courseName;
        course.courseDescription = courseDescription;
        course.courseStart = courseStart;
        course.courseEnd = courseEnd;
        course.status = courseStatus;
        course.mentorName = courseMentor;
        course.mentorPhone = mentorPhone;
        course.mentorEmail  = mentorEmail;
        course.notifications = courseNotification;

        return course;
    }



   

}
